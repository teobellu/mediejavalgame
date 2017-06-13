package server;

import java.util.ArrayList;
import java.util.List;

import exceptions.GameException;
import game.Game;
import util.Constants;

public class Room extends Thread {
	
	public Room(String configFile) {
		_players = new ArrayList<>();
	}
	
	@Override
	public void run(){
		//TODO
		
		
		
		while(!isReady()){
			//TODO
		}
		
		_theGame = new Game(this);
		
		_theGame.run();
		
		_isRunning = true;
	}
	
	public synchronized void addPlayer(Client client) throws GameException {
		if(_players.size() < Constants.MAX_PLAYER){
			_players.add(client);
		} else{
			throw new GameException("Cannot add player to this room, room is full. What's going on?");
		}
		
	}

	public boolean isFull() {
		return _players.size() == Constants.MAX_PLAYER;//TODO
	}
	
	public boolean isReady(){
		if(isFull()){
			for(Client player : _players){
				if(!player.isReady()){
					return false;
				}
			}
			return true;
		}
		
		return false;
	}
	
	public boolean isRunning(){
		return _isRunning;
	}
	
	public boolean isOver(){
		return _theGame.isOver();
	}

	public List<Client> getPlayers(){
		return _players;
	}
	
	public void broadcastMessage(){
		for(Client p : _players){
			//TODO
		}
	}
	
	public void setStartTime(long time){
		_startTimeSeconds = time;
	}
	
	public void getConfig(){
		
	}
	
	public Game getGame(){
		return _theGame;
	}
	
	private long _startTimeSeconds = Constants.DEFAULT_START_ROOM_TIME_MILLIS;
	private Game _theGame;
	private boolean _isRunning = false;
	private List<Client> _players;
}
