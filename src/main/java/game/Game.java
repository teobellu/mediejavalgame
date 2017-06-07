package game;

import java.util.ArrayList;
import java.util.List;

import game.state.InitialState;
import game.state.State;
import game.state.StateStartingTurn;
import game.state.VaticanState;
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
	
	private int _turnDuration;
	
	public Game(Room room) {
		_theRoom = room;
		_turn = 0;
		_phase = 0;
		for(Client cli : _theRoom.getPlayers()){
			_players.add(new Player());//TODO
		}
	}
	
	@Override
	public void run() {
		//TODO
		
		setupGame();
		
		_state = new StateStartingTurn(this);
		
		while(!isGameOver()){
			for(int i = 0;i<Constants.NUMBER_OF_FAMILIARS; i++){
				for(_phase = 0;_phase<_players.size();_phase++){
					do{
						_state = _state.doState();
					} while(_state!=null);
				}
				_state = new StateStartingTurn(this);
			}
			_turn++;
		}
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
	
	public Client getCurrentClient(){
		return _theRoom.getPlayers().get(_phase);
	}

	public GameBoard getGameBoard() {
		return _board;
	}
	
	public String getNextGameAction(){
		//TODO
		return "";
	}
	
	private void setupGame(){
		_board = new GameBoard(_theRoom.getConfig());//TODO
	}
}
