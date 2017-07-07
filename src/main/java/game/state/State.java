package game.state;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import client.cli.CommandConstants;
import exceptions.GameException;
import game.Game;
import game.ListenAction;
import game.Player;
import server.Client;
import server.ConnectionHandler;
import util.CommandStrings;
import util.Constants;

public abstract class State {
	
	protected int age; //1,2,3
	protected int phase; //
	protected int countTurn;
	protected final Game _theGame; //TODO
	protected Player _player = null;
	protected Client _client;
	private List<Player> _players;
	
	private long startTime;
	
	public State(Game game){
		_theGame = game;
		_players = _theGame.getPlayers();
	}
	
	public void setupState() {
		_player = _players.get(0);
		age = 1;
		phase = 1;
		countTurn = 1;
		_theGame.getDynamicBar().setPlayer(_player);
		_theGame.setListener(new ListenAction(_theGame));
		_theGame.getListener().setPlayer(_player);
		try {
			_player.getClient().getConnectionHandler().startTurn(_theGame.getBoard(), _player);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void nextState(){
		//count turn è quello appena passato
		Player nextPlayer = getNextPlayer();
		
		if (countTurn % (_players.size() * Constants.NUMBER_OF_FAMILIARS) == 0){//TODO non e' 4.
			for(Player p : _theGame.getGameInformation().getLatePlayersTurn()){
				setupNewTurn(p);
				_theGame.getGameInformation().getLatePlayersTurn().removeIf(item -> item ==p);
				_theGame.getGameInformation().getTailPlayersTurn().add(p);
				notifyPlayerTurn(p);
			}
			if (phase == 2){
				System.out.println("VATICAN PHASE");
				for (Player p : _players){
					_theGame.getDynamicBar().setPlayer(p);
					_theGame.getListener().setPlayer(p);
					_theGame.getDynamicBar().showVaticanSupport(age);
				}
				phase = 0;
				age++;
			}
			System.out.println("NEXT PHASE");
			_theGame.getGameInformation().newPhase(age);
			nextPlayer = _theGame.getPlayers().get(0);
			phase++;
		}
		countTurn++;
		if (age == 4){
			age = 3;
			List<Player> winners = _theGame.getGameInformation().endOfTheGameFindWinners();
			String notice = new String();
			for (Player winner : winners)
				notice += winner.getName() + " win! ";
			if ("".equals(notice))
				notice += "Noboy wins!";
			_theGame.broadcastInfo(notice);
			return;
		}
		_player = nextPlayer;
		setupNewTurn(_player);
		//se il player è nella lista tail gli faccio saltare il turno e lo metto nella lista tail 2TODO
		//alla fine faccio fare il turno ai giocatori nella lista tail 2TODO
		
		notifyPlayerTurn(_player);
	}
	
	public void setupNewTurn(Player nextPlayer){
		_theGame.getDynamicBar().setPlayer(nextPlayer);
		_theGame.getListener().setPlayer(nextPlayer);
		_theGame.getDynamicBar().startTurn();
	}
	
	//TODO DICE AL PLAYER CHE E' IL SUO TURNO
	private void notifyPlayerTurn(Player player){
		startTime = new Date().getTime();
		
		ConnectionHandler handler = _player.getClient().getConnectionHandler();
		try {
			handler.startTurn(_theGame.getBoard(), player);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boolean exit = false;
		while (!exit){
			if (isTimeoutOver()){
				//_theGame.movePlayerAfk();
				//avviso il client
				_theGame.broadcastInfo("Player " + _player + " timer has expired");
				//nextState();
				exit = true;
			}
		}
	}
	
	/**
	 * TODO OTTIENI IL PROSSIMO GIOCATORE, A RUOTA
	 * @return 
	 */
	private Player getNextPlayer(){
		int currentPlayerIndex = _players.indexOf(_player);
		if (currentPlayerIndex == _players.size() - 1)
			return _players.get(0);
		Player next = _players.get(currentPlayerIndex + 1);
		if (_theGame.getGameInformation().getTailPlayersTurn().contains(next)){
			_theGame.getGameInformation().getTailPlayersTurn().removeIf(player -> player == next);
			_theGame.getGameInformation().getLatePlayersTurn().add(next);
			_player = next;
			countTurn++;
			return getNextPlayer();
		}
		return _players.get(currentPlayerIndex + 1);
	}
	
	public boolean isTimeoutOver(){
		long currentTime = new Date().getTime();
		return currentTime-startTime > _theGame.getTurnTimeout();
	}
	
	public Player getCurrenPlayer() {
		return _player;
	}

	
}