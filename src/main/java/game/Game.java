package game;

import java.util.List;

import game.state.InitialState;
import game.state.State;
import game.state.VaticanState;
import server.Client;
import server.Room;
import util.Constants;

public class Game implements Runnable {

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
				_state = _state.doState(_theRoom.getPlayers().get(_phase));
				_phase++;
			}
			_turn++;
			if(_turn%2 == 0){
				_phase = 0;
				while(_phase < Constants.MAX_PLAYER){
					_state = new VaticanState(this);
					_state = _state.doState(_theRoom.getPlayers().get(_phase));
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
	
	public Client getCurrentPlayer(){
		//TODO
		return null;
	}
	
	private List<Client> _players;
	
	private State _state;
	
	private int _turn;
	
	private int _phase;
	
	private boolean _isOver = false;
	
	private final Room _theRoom;
}
