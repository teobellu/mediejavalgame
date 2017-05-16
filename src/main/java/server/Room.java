package server;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import exceptions.GameException;
import game.Game;
import packets.GamePacket;
import packets.Packet;
import util.Constants;

public class Room extends Thread {
	
	public Room() {
		_players = new ArrayList<>();
	}
	
	@Override
	public void run(){
		//TODO
		_theGame = new Game(this);
		
		_theGame.run();
		
		_isRunning = true;
		
		_defaultProperties = new Properties();
	}
	
	public synchronized void addPlayer(Client client) throws GameException {
		if(_players.size() < Constants.MAX_PLAYER){
			_players.add(client);
			if(_players.size()==1){
				askCustomization(client);
			}
		} else{
			throw new GameException("Cannot add player to this room, room is full. What's going on?");
		}
		
	}

	private void askCustomization(Client client) {
		ConnectionHandler ch = client.getConnectionHandler();
		
		Packet message = new GamePacket("");//TODO
		try {
			ch.sendToClient(message);
			message = ch.getFromClient();
		} catch (Exception e) {
			//TODO
			try {
				FileInputStream in = new FileInputStream(Constants.DEFAULT_PROPERTIES);
				_defaultProperties.load(in);
				in.close();
			} catch (IOException io ) {
				// TODO: handle exception
			}
		}
		
	}

	public boolean isFull() {
		if(_players.size() == Constants.MAX_PLAYER){
			return true;
		}
		
		return false;
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

	public synchronized List<Client> getPlayers(){
		return _players;
	}
	
	private Game _theGame;
	private boolean _isRunning = false;
	private List<Client> _players;
	private Properties _defaultProperties;
}
