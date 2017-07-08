package game.state;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import game.DynamicAction;
import game.GC;
import game.Game;
import game.GameInformation;
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
	
	private DynamicAction controller;
	private ListenAction listener;
	private GameInformation information;
	
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
		controller = _theGame.getDynamicBar();
		information = _theGame.getGameInformation();
		_theGame.setListener(new ListenAction(_theGame));
		listener = _theGame.getListener();
		setupNewTurn(_player);
		notifyPlayerTurn(_player);
		
	}
	
	public void nextState(){
		
		if (phase == 2 && countTurn % (_players.size() * Constants.NUMBER_OF_FAMILIARS) == 0){
			System.out.println("VATICAN PHASE");
			for (Player p : _players){
				controller.setPlayer(p);
				listener.setPlayer(p);
				if (!p.isAfk())
					controller.showVaticanSupport(age);
				else
					controller.dontShowVaticanSupport(age);
			}
			phase = 0;
			age++;
		}
		//count turn è quello appena passato
		Player nextPlayer = getNextPlayer();
		
		if (countTurn % (_players.size() * Constants.NUMBER_OF_FAMILIARS) == 0){//TODO non e' 4.
			
			System.out.println("NEXT PHASE");
			information.newPhase(age);
			for (Player p : _theGame.getPlayers()){
				controller.setPlayer(p);
				if (!p.isAfk())
					controller.readDices();
			}
			//nextPlayer = _theGame.getPlayers().get(0);
			phase++;
		}
		countTurn++;
		if (age == 4){
			age = 3;
			List<Player> winners = information.endOfTheGameFindWinners();
			String notice = new String();
			for (Player winner : winners)
				notice += winner.getName() + " wins! ";
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
		controller.setPlayer(nextPlayer);
		listener.setPlayer(nextPlayer);
		try {
			if (countTurn > _players.size())
			nextPlayer.getClient().getConnectionHandler()
				.sendInfo("You turn is starting", _theGame.getBoard(), nextPlayer);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!nextPlayer.isAfk())
			controller.startTurn();
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
			// TODO Auto-generated catch block mettero' il player afk?
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the next player according to game rules
	 * @return the next player
	 */
	private Player getNextPlayer(){
		Player current = _player;
		Player shifted = shiftPlayer(current);
		if (existsDelayTarget(GC.RECOVERY) && !existsDelayTarget(GC.RECOVERY - 1)){
			do{
				int delay = shifted.getDelay();
				if (delay == GC.RECOVERY){
					shifted.setDelay(GC.DELAY);
					break;
				}
				shifted = shiftPlayer(shifted);
			}while(true);
		}
		else{
			do{
				int delay = shifted.getDelay();
				if (delay == 0){
					break;
				}else if (shifted.getDelay() == GC.DELAY){
					shifted.setDelay(delay + 1);
				}else if (delay >= 2 && delay <= 4){
					shifted.setDelay(delay + 1);
					break;
				}
				shifted = shiftPlayer(shifted);
			}while(true);
		
		}
		return shifted;
	}
	
	private boolean existsDelayTarget(int target) {
		for (Player p : _players)
			if (p.getDelay() == target)
				return true;
		return false;
	}
	
	private Player shiftPlayer(Player player){
		int currentPlayerIndex = _players.indexOf(player);
		if (currentPlayerIndex == _players.size() - 1)
			return _players.get(0);
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

/**
 * Turn timer
 * @Override TimerTask class java.util
 * @author M
 *
 */
class TimeTimerTask extends TimerTask {

	/**
	 * Turn when timer is callen
	 */
    private final int turn;
    
    /**
     * Game
     */
    private final Game game;
    
    /**
     * Current player when timer is callen
     */
    private final Player player;

    /**
     * Timer task friendly constructor
     * @param turn Turn when timer is callen
     * @param game The game
     * @param player Current player when timer is callen
     */
    TimeTimerTask (int turn, Game game, Player player){
    	this.player = player;
    	this.game = game;
    	this.turn = turn;
    }

    @Override
    public void run() {
    	if (turn == game.getState().countTurn){
			game.broadcastInfo("Player " + player.getName() + " has disconnected. The time available is over.");
			player.setAfk(true);
			game.getState().nextState();
			return;
		}
    }
}