package server;

import java.util.ArrayList;
import java.util.List;

import util.Constants;

public class Room extends Thread {
	
	public Room() {
		_players = new ArrayList<>();
		
		_isReady = true;
	}
	
	@Override
	public void run(){
		//TODO
		
		_isRunning = true;
	}
	
	public void addPlayer(Client client) {
		// TODO Auto-generated method stub
		
	}

	public boolean isFull() {
		if(_players.size() == Constants.MAX_PLAYER){
			return true;
		}
		
		return false;
	}
	
	public boolean isReady(){
		return _isReady;
	}
	
	public boolean isRunning(){
		return _isRunning;
	}
	
	public boolean isOver(){
		return _isOver;
	}

	private boolean _isOver = false;
	private boolean _isRunning = false;
	private boolean _isReady = false;
	private List<Client> _players;
}
