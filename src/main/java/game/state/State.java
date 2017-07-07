package game.state;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import game.Game;
import game.ListenAction;
import game.Player;
import server.Client;
import server.ConnectionHandler;
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
		
		_theGame.setListener(new ListenAction(_theGame));
		setupNewTurn(_player);
		notifyPlayerTurn(_player);
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
			_theGame.closeGame();
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
		if (player.isAfk()){
			_theGame.otherPlayersInfo("The player " + player.getName() + " still afk, turn is skipped!", player);
			nextState();
			return;
		}
		
		//startTime = new Date().getTime();
		System.out.println("time tasking!");
		
		
		TimerTask timerTask = new TimeTimerTask(countTurn, _theGame, player);
        

        Timer timer = new Timer("MyTimer");

        
        timer.schedule(timerTask, _theGame.getTurnTimeout());
		
		ConnectionHandler handler = _player.getClient().getConnectionHandler();
		try {
			handler.startTurn(_theGame.getBoard(), player);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("I'm to going in!");
		
		/*
		int oldCounter = countTurn;
		long start = System.currentTimeMillis();
		long now = System.currentTimeMillis();
		while (now - start < _theGame.getTurnTimeout())
		{
		    now = System.currentTimeMillis();
		}
		
		System.out.println("I'm out!");
		System.out.println(countTurn);
		System.out.println(_theGame.getState().countTurn);
		
		if (countTurn == _theGame.getState().countTurn){
			_theGame.broadcastInfo("Player " + _player.getName() + " timer has expired and will be disconnected.");
			_player.setAfk(true);
			nextState();
			return;
		}
		*/
		
        //timer.scheduleAtFixedRate(timerTask, 30, 3000);//this line starts the timer at the same time its executed
		
		/*
		boolean exit = false;
		while (!exit){
			if (isTimeoutOver()){
				//avviso il client
				_theGame.broadcastInfo("Player " + _player.getName() + " timer has expired and will be disconnected.");
				_player.setAfk(true);
				nextState();
				return;
			}
		}*/
	}
	
	/**
	 * Get the next player according to game rules
	 * @return the next player
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

class TimeTimerTask extends TimerTask {

    private final int turn;
    private final Game _theGame;
    private final Player player;

    TimeTimerTask (int turn, Game game, Player player)
    {
    	this.player = player;
    	_theGame = game;
    	this.turn = turn;
    }

    public void run() {
    	if (turn == _theGame.getState().countTurn){
			_theGame.broadcastInfo("Player " + player.getName() + " timer has expired and will be disconnected.");
			player.setAfk(true);
			_theGame.getState().nextState();
			return;
		}
    }
}