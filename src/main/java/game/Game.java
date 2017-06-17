package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import game.state.State;
import game.state.StateStartingTurn;
import server.Client;
import server.Room;
import util.CommandStrings;
import util.Constants;

public class Game implements Runnable {

	List<List<String>> _tempLeaderCardForEachPlayer = new ArrayList<>();
	
	private List<Player> _players = new ArrayList<>();

	private GameBoard _board;

	private State _state;
	private int _turn;
	private int _phase;
	private boolean _isOver = false;
	private final Room _theRoom;
	
	private List<LeaderCard> _leaders = new ArrayList<>();
	
	/**
	 * Queue with FIFO logic in which the game commands will be put
	 */
	private Deque<String> _commandActionList = new ConcurrentLinkedDeque<>();
	
	private final DynamicAction _dynamicAction;
	
	private boolean _hasPlacedFamiliar = false;
	
	private GameInformation gameInformation;
	
	public GameInformation getGameInformation() {
		return gameInformation;
	}

	public Game(Room room) {
		_theRoom = room;
		_turn = 0;
		_phase = 0;
		for(Client cli : _theRoom.getPlayers()){
			_players.add(new Player(cli));//TODO
		}
		_dynamicAction = new DynamicAction(this);
		gameInformation = new GameInformation(this);
	}
	
	@Override
	public void run() {
		//TODO
		
		setupGame();
		
		_state = new StateStartingTurn(this);
		
		while(!isGameOver()){
			for(int i = 0;i<Constants.NUMBER_OF_FAMILIARS; i++){
				cycleState();
				_hasPlacedFamiliar = false;
				_state = new StateStartingTurn(this);
			}
			_turn++;
		}
		
		List<Player> winners = gameInformation.endOfTheGameFindWinners();
		winners.forEach(player -> player.getClient().getConnectionHandler().sendToClient("GG U WIN"));
	}
	
	//x sonar
	private void cycleState(){
		for(_phase = 0;_phase<_players.size();_phase++)
			while(_state!=null)
				_state = _state.doState();
	}

	public boolean isOver() {
		return _isOver;
	}
	
	private boolean isGameOver(){
		if(_turn < Constants.MAX_TURN && _players.size()>1){
			return false;
		}
		return true;
	}
	
	public Player getCurrentPlayer(){
		return _players.get(_phase);
	}
	
	public GameBoard getGameBoard() {
		return _board;
	}
	
	public String getNextGameAction(){
		//TODO forse...?
		return "";
	}
	
	private void setupGame(){
		//TODO
		_leaders = gameInformation.getLeaderDeck().subList(0, _players.size() * Constants.LEADER_CARDS_PER_PLAYER);
		Collections.shuffle(_leaders);
		
		for(int i = 0;i<_players.size();i++){
			List<String> leadersnames = new ArrayList<>();
			for(int j = i*Constants.LEADER_CARDS_PER_PLAYER;j<(i+1)*Constants.LEADER_CARDS_PER_PLAYER-1;j++){
				leadersnames.add(_leaders.get(j).getName());
			}
			_tempLeaderCardForEachPlayer.add(i, leadersnames);
		}
		
		for(Player p : _players){
			p.getClient().getConnectionHandler().sendToClient(CommandStrings.GAME_STARTED);
		}
	}
	
	public boolean hasPlacedFamiliarYet(){
		return _hasPlacedFamiliar;
	}
	
	public void sendToCurrentPlayer(String message){
		getCurrentPlayer().getClient().getConnectionHandler().sendToClient(message);
	}
	
	public void sendToAllPlayers(String message){
		_players.forEach(player -> player.getClient().getConnectionHandler().sendToClient(message));
		
	}
	
	public void switchLeaderList(Client cli){
		//TODO
		for(int i = 0;i<_players.size();i++){
			if(_players.get(i).getClient().equals(cli)){
				
			}
		}
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
	
	/**
	 * Return the command list, usually to add or pop a command
	 * @return the command list
	 */
	public Deque<String> getActionCommandList(){
		return _commandActionList;
	}
	
	public List<Player> getPlayers() {
		return _players;
	}
	
	public GameBoard getBoard() {
		return _board;
	}
}
