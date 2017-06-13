package game;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import game.state.State;
import game.state.StateStartingTurn;
import server.Client;
import server.Room;
import util.Constants;

public class Game implements Runnable {

	private List<Player> _players = new ArrayList<>();

	private GameBoard _board;
	

	private State _state;
	private int _turn;
	private int _phase;
	private boolean _isOver = false;
	private final Room _theRoom;
	
	private Deque<String> _commandActionList = new ConcurrentLinkedDeque<>();
	
	private final DynamicAction _dynamicAction;
	
	private boolean _hasPlacedFamiliar = false;
	
	private int _turnDuration;
	private GameInformation gameInformation;
	
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
		//TODO
		return "";
	}
	
	private void setupGame(){
		//TODO
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
	
	public DynamicAction getDynamicBar(){
		return _dynamicAction;
	}
	
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
