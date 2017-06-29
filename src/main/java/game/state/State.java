package game.state;

import java.rmi.RemoteException;
import java.util.List;

import exceptions.GameException;
import game.FamilyMember;
import game.Game;
import game.ListenAction;
import game.Player;
import server.Client;
import server.ConnectionHandler;

public abstract class State {
	
	protected int age; //1,2,3
	protected int phase; //
	protected int countTurn;
	protected final Game _theGame; //TODO
	protected Player _player = null;
	protected Client _client;
	private List<Player> _players;
	
	public State(Game game){
		_theGame = game;
		//_player = _theGame.getCurrentPlayer();
		_players = _theGame.getPlayers();
	}
	
	public void setupState() {
		_player = _players.get(0);
		age = 1;
		phase = 1;
		countTurn = 1;
		_theGame.getDynamicBar().setPlayer(_players.get(0));
		_theGame.setListener(new ListenAction(_theGame));
		_theGame.getListener().setPlayer(_players.get(0));
		try {
			_player.getClient().getConnectionHandler().startTurn(_theGame.getBoard(), _player);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doState(){
		
	}
	
	public void nextState(){
		Player nextPlayer = getNextPlayer();
		countTurn++;
		if (countTurn % (_players.size() * 4) == 0){
			if (phase == 2){
				age++;
				System.out.println("VATICAN PHASE");
				for (Player p : _players){
					_theGame.getDynamicBar().setPlayer(p);
					_theGame.getListener().setPlayer(p);
					notifyPlayerTurn(_player);
				}
				phase = 0;
			}
			phase++;
			System.out.println("NEXT PHASE");
			_theGame.getGameInformation().newPhase(age);
			nextPlayer = _theGame.getPlayers().get(0);
		}
		if (age == 4){
			age = 3;
			List<Player> players = _theGame.getGameInformation().endOfTheGameFindWinners();
			players.forEach(player -> System.out.println(player.getName() + " win"));//TODO
			
		}
		_theGame.getDynamicBar().setPlayer(nextPlayer);
		//refresho listener list
		_theGame.getListener().setPlayer(nextPlayer);
		//avviso che è il suo turno
		_player = nextPlayer;
		//se il player è nella lista tail gli faccio saltare il turno e lo metto nella lista tail 2TODO
		//alla fine faccio fare il turno ai giocatori nella lista tail 2TODO
		
		notifyPlayerTurn(_player);
	}
	
	//TODO DICE AL PLAYER CHE E' IL SUO TURNO
	private void notifyPlayerTurn(Player player){
		ConnectionHandler handler = _player.getClient().getConnectionHandler();
		try {
			handler.startTurn(_theGame.getBoard(), player);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		return _players.get(currentPlayerIndex + 1);
	}
	
	public abstract List<String> dropLeaderCard() throws GameException;
	
	public abstract void dropWhichLeaderCard(String leader) throws GameException;
	
	public abstract boolean endTurn() throws GameException;
	
	public abstract List<String> activateLeaderCard() throws GameException;

	//TODO alcune carte richiedono interazione
	public abstract void activateWhichLeaderCard(String leader) throws GameException;
	
	public abstract List<FamilyMember> placeFamiliar() throws GameException;
	
	public abstract List<String> placeWhichFamiliar(String familiar) throws GameException;
	
	//TODO string?
	public abstract void placeWhereFamiliar(String position) throws GameException;

	
	
	
	public Player getCurrenPlayer() {
		return _player;
	}

	
}