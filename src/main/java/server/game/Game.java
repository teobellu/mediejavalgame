package server.game;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.GC;
import model.GameBoard;
import model.LeaderCard;
import model.Player;
import model.Resource;
import server.Client;
import server.Room;
import util.CommandStrings;
import util.Constants;

/**
 * @author Matteo
 * @author Jacopo
 */
public class Game implements Runnable {
	
	Logger _log = Logger.getLogger(Game.class.getName());
	
	private ListenAction _listener;
	
	private List<Player> _players = new ArrayList<>();

	private GameBoard _board;

	private long turnTimeout;
	private State _state;
	private boolean _isOver = false;
	private final Room _theRoom;
	
	private final DynamicAction _dynamicAction;
	
	private GameInformation gameInformation;
	
	public GameInformation getGameInformation() {
		return gameInformation;
	}

	public Game(Room room) {
		_theRoom = room;
		
		List<String> playerColours = new ArrayList<>(Arrays.asList(GC.getPlayerColours()));
		
		Collections.shuffle(playerColours);
		
		for(Client cli : _theRoom.getPlayers()){
			_players.add(new GamePlayer(cli, playerColours.remove(0)));//TODO
		}
		_dynamicAction = new DynamicAction(this);
		gameInformation = new GameInformation(this);
	}
	
	@Override
	public void run() {
		setupGame();
	}

	public boolean isOver() {
		return _isOver;
	}
	
	public Player getCurrentPlayer(){
		return _state.getCurrenPlayer();
	}
	
	public GameBoard getGameBoard() {
		return _board;
	}
	
	private void setupGame(){
		Collections.shuffle(_players);
		Collections.shuffle(gameInformation.getDevelopmentDeck());
		gameInformation.setExcommunicationTitlesOnBoard();
		try {
			setupLeaderCards();
			setupDashboardBonus();
		} catch (RemoteException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			closeGame();
		}
		setInitialResourcePack();
		gameInformation.newPhase(1);
		_state = new State(this);
		_state.setupState();
	}
	
	private void setInitialResourcePack(){
		int n = 5;
		for (Player p : _players){
			p.gain(new Resource(GC.RES_WOOD, 2));
			p.gain(new Resource(GC.RES_STONES, 2));
			p.gain(new Resource(GC.RES_SERVANTS, 3));
			p.gain(new Resource(GC.RES_COINS, n));
			n++;
		}
	}
	
	/**
	 * Initial setup leader card
	 * @throws RemoteException
	 */
	private void setupLeaderCards() throws RemoteException{
		
		List<LeaderCard> leaders = gameInformation.getLeaderDeck();
        
		Collections.shuffle(leaders);
		
		List<List<LeaderCard>> playerLists = new ArrayList<>();
		
		//estrae n mazzi da 4 carte, dove n = numero dei giocatori, e li salva
		for (@SuppressWarnings("unused") Player player : _players){
			List<LeaderCard> miniDeck = new ArrayList<>();
			for (int i = 0; i < Constants.LEADER_CARDS_PER_PLAYER; i++)
				miniDeck.add(leaders.remove(0));
			playerLists.add(miniDeck);
		}
		
		//riceve gli input
		for (int i = 0; i < Constants.LEADER_CARDS_PER_PLAYER; i++){
			int x = 0;
			for (Player player : _players){
				List<LeaderCard> miniDeck = playerLists.get(x);
				int selection = player.getClient().getConnectionHandler().chooseLeader(CommandStrings.INITIAL_LEADER, miniDeck);
				player.addLeaderCard(miniDeck.get(selection));
				miniDeck.remove(selection);
				x++;
			}
			playerLists.add(playerLists.remove(0));
		}
	}
	
	/**
	 * Initial personal bonus choose
	 * @throws RemoteException 
	 */
	private void setupDashboardBonus() throws RemoteException{
		//ottengo i bonus della plancia giocatore
        Map<String, List<Resource>> bonus = gameInformation.getBonusPlayerDashBoard();
		
        //creo una lista di player invertita (vedi regolamento)
		List<Player> reversePlayers = new ArrayList<>(_players);
		Collections.reverse(reversePlayers);
        
		//chiedo ai giocatori di scegliere il loro bonus
		for (Player p : reversePlayers){
			int selection = 0;
			selection = p.getClient().getConnectionHandler().chooseDashboardBonus(bonus);
			p.setHarvestBonus(bonus.get(GC.HARVEST).get(selection));
			p.setProductionBonus(bonus.get(GC.PRODUCTION).get(selection));
			bonus.get(GC.HARVEST).remove(selection);
			bonus.get(GC.PRODUCTION).remove(selection);
		}
	}
	
	public synchronized void otherPlayersInfo(String message, Player excluded){
		_players.stream()
			.filter(player -> player.getName() != excluded.getName())
			.forEach(player -> {
				try {
					player.getClient().getConnectionHandler().sendInfo(message);
				} catch (RemoteException e) {
					_log.log(Level.SEVERE, e.getMessage(), e);
					setAFK(player);
				}
			});
	}
	
	public synchronized void broadcastInfo(String message){
		_players.forEach(player -> {
			try {
				player.getClient().getConnectionHandler().sendInfo(message);
			} catch (RemoteException e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
				setAFK(player);
			}
		});
	}
	
	
	/**
	 * Set a player afk
	 * @param player the player to set afk
	 */
	private synchronized void setAFK(Player player) {
		player.setAfk(true);
	}

	public State getState(){
		return _state;
	}
	
	public void setState(State state){
		_state = state;
	}
	
	public DynamicAction getDynamicBar(){
		return _dynamicAction;
	}
	
	
	public List<Player> getPlayers() {
		return _players;
	}
	
	public GameBoard getBoard() {
		return _board;
	}
	
	public void setBoard(GameBoard board) {
		this._board = board;
	}

	public ListenAction getListener() {
		return _listener;
	}

	public void setListener(ListenAction listener) {
		this._listener = listener;
	}

	protected void setPlayers(List<Player> nextPlayersTurn) {
		_players = nextPlayersTurn;
	}
	
	public void setMeAFK(String name){
		for(Player p : _players){
			if(p.getName().equals(name)){
				setAFK(p);
			}
		}
	}

	public long getTurnTimeout() {
		return turnTimeout;
	}

	public void setTurnTimeout(long timeout) {
		this.turnTimeout = timeout;
	}

	public void closeGame() {
		broadcastInfo("Game ended, thanks for play!");
		_isOver = true;
		_dynamicAction.setPlayer(null);
		_listener.setPlayer(null);
		_players.clear();
		_theRoom.shutdown();
	}
}
