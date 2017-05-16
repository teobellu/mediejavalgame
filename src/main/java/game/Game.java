package game;

import java.util.List;

import game.state.InitialState;
import game.state.State;
import game.state.VaticanState;
import server.Client;
import server.Room;
import util.Constants;

public class Game implements Runnable {

	private List<Client> _players;
	private GameBoard board;
	private State _state;
	private int _turn;
	private int _phase;
	private boolean _isOver = false;
	private final Room _theRoom;
	
	public Game(Room room) {
		_theRoom = room;
		_turn = 0;
		_phase = 0;
		_players = room.getPlayers();
	}
	
	@Override
	public void run() {
		//TODO
		
		_state = new InitialState(this);
		
		while(!isGameOver()){
	        _phase = 0;
	        while(_phase < Constants.MAX_PLAYER){
	            while(_state!=null){
	            	//_theRoom.getPlayers().get(_phase) è il player corrente
	                _state = _state.doState();
	            }
	            _phase++;
	        }
	        _turn++;
			if(_turn % 2 == 0){
				_phase = 0;
				while(_phase < Constants.MAX_PLAYER){
					_state = new VaticanState(this);
					_state = _state.doState();
				}
			}
		}
		
		_isOver = true;
	}

	public boolean isOver() {
		return _isOver;
	}
	
	private boolean isGameOver(){
		if(_turn < Constants.MAX_TURN){
			return false;
		}
		return true;
	}
	
	public Player getCurrentPlayer(){
		//TODO
		return null;
	}
	
	public Client getCurrentClient(){
		return _theRoom.getPlayers().get(_phase);
	}

	public GameBoard getGameBoard() {
		return board;
	}
}
