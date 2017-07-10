package server.game;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.GC;
import model.Player;
import server.ConnectionHandler;
import util.Constants;

/**
 * State of the game
 * @author M
 * @author Jacopo
 */
public class State {
	
	/**
	 * Logger
	 */
	private Logger _log = Logger.getLogger(State.class.getName());
	
	/**
	 * Current age
	 */
	protected int age;
	
	/**
	 * Current phase
	 */
	protected int phase;
	
	/**
	 * Current turn, means each single turn, not the turn in the game rule
	 */
	protected int countTurn;
	
	/**
	 * Game
	 */
	protected final Game game;
	
	/**
	 * Current player
	 */
	protected Player _player = null;
	
	/**
	 * Lists of players from Game
	 */
	private List<Player> players;
	
	/**
	 * Player "inside" controller
	 */
	private DynamicAction controller;
	
	/**
	 * Player "from outside" controller
	 */
	private ListenAction listener;
	
	/**
	 * Game information of game
	 */
	private GameInformation information;
	
	/**
	 * Base constructor of a space
	 * @param game The game
	 */
	public State(Game game){
		this.game = game;
		players = game.getPlayers();
	}
	
	/**
	 * Setup first state
	 */
	public void setupState() {
		_player = players.get(0);
		age = 1;
		phase = 1;
		countTurn = 1;
		controller = game.getDynamicBar();
		information = game.getGameInformation();
		game.setListener(new ListenAction(game));
		listener = game.getListener();
		setupNewTurn(_player);
		notifyPlayerTurn(_player);
		
	}
	
	/**
	 * Vatican phase, show support or not to vatican for each player
	 */
	private void vaticanPhase(){
		for (Player p : players){
			controller.setPlayer(p);
			listener.setPlayer(p);
			controller.showVaticanSupport(age);
			p.setVaticanSupport(false);
		} 
		
	}
	
	/**
	 * Setup a new state
	 */
	public void nextState(){
		if (countTurn % (players.size() * Constants.NUMBER_OF_FAMILIARS) == 0){
			if (phase == 2){
				vaticanPhase();
				phase = 0;
				age++;
			}
			game.getPlayers().forEach(player -> player.setOPTActivated(false));
			information.newPhase(age);
			players = game.getPlayers();
			phase++;
		}
		//count turn è quello appena passato
		Player nextPlayer = getNextPlayer();
		countTurn++;
		if (age == 4){
			age = 3;
			List<Player> winners = information.endOfTheGameFindWinners();
			String notice = new String();
			for (Player winner : winners)
				notice += winner.getName() + " wins! ";
			if ("".equals(notice))
				notice += "Noboy wins!";
			game.broadcastInfo(notice);
			game.closeGame();
			return;
		}
		_player = nextPlayer;
		setupNewTurn(_player);
		
		notifyPlayerTurn(_player);
	}
	
	/**
	 * Setup a nuw turn
	 * @param nextPlayer
	 */
	public void setupNewTurn(Player nextPlayer){
		controller.setPlayer(nextPlayer);
		listener.setPlayer(nextPlayer);
	}
	
	/**
	 * Tells player that it's his turn
	 * @param player
	 */
	private void notifyPlayerTurn(Player player){
		if (player.isAfk()){
			game.otherPlayersInfo("The player " + player.getName() + " still afk, turn is skipped!", player);
			nextState();
			return;
		}
		
		TimerTask timerTask = new TimeTimerTask(countTurn, game, player);
        

        Timer timer = new Timer("MyTimer");
        
        timer.schedule(timerTask, game.getTurnTimeout());
		
		ConnectionHandler handler = _player.getClient().getConnectionHandler();
		try {
			handler.startTurn(game.getBoard(), player);
		} catch (RemoteException e) {
			player.setAfk(true);
			_log.log(Level.SEVERE, e.getMessage(), e);
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
	
	/**
	 * Find if the are players this a specific delay target
	 * @param target
	 * @return
	 */
	private boolean existsDelayTarget(int target) {
		for (Player p : players)
			if (p.getDelay() == target)
				return true;
		return false;
	}
	
	/**
	 * Get the next player (A ruota)
	 * @param player
	 * @return
	 */
	private Player shiftPlayer(Player player){
		int currentPlayerIndex = players.indexOf(player);
		if (currentPlayerIndex == players.size() - 1)
			return players.get(0);
		return players.get(currentPlayerIndex + 1);
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