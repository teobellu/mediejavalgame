package server;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import exceptions.GameException;
import game.Game;
import util.Constants;

public class Room extends Thread {
	
	public Room(String configFile) {
		_clients = new ArrayList<>();
		
		/*https://stackoverflow.com/questions/19661047/java-convert-string-to-xml-and-parse-node*/
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(configFile)));
			//TODO usare questo xml
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		_startTime = new Date().getTime();
		
		//TODO prendere dallo xml il valore del timeout e settarlo
	}
	
	@Override
	public void run(){
		_theGame = new Game(this);
		
		//TODO passare config a game
		
		new Thread(_theGame).start();
		
		_isRunning = true;
		
		_log.info("New Room started");
	}
	
	public synchronized void addPlayer(Client client) throws GameException {
		if(_clients.size() < Constants.MAX_PLAYER){
			client.setRoom(this);
			_clients.add(client);
		} else{
			throw new GameException("Cannot add player to this room, room is full. What's going on?");
		}
		
	}

	public boolean isFull() {
		return _clients.size() == Constants.MAX_PLAYER;//TODO
	}
	
	public boolean isReady(){
		if(isFull()){
			for(Client player : _clients){
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
		return _clients;
	}
	
	public void broadcastMessage(String message){
		for(Client p : _clients){
			p.getConnectionHandler().sendToClient(message);
		}
	}
	
	public void setStartTimeout(long time){
		_startTimeout = time;
	}
	
	public void getConfig(){
		
	}
	
	public Game getGame(){
		return _theGame;
	}
	
	public boolean isTimeoutOver(){
		long currentTime = new Date().getTime();
		return currentTime-_startTime > _startTimeout;
	}
	
	public void shutdown(){
		if(_theGame!=null){
			//TODO devo spegnere/togliere cose dal game?
		}
		
		if(_clients!=null && !_clients.isEmpty()){
			for(Client c : _clients){
				//TODO togliere cose in client?
				_clients.remove(c);
			}
		}
		
		_isRunning = false;
	}
	
	private long _startTimeout = Constants.DEFAULT_START_ROOM_TIME_MILLIS;//TODO
	private final long _startTime;
	private Game _theGame;
	private boolean _isRunning = false;
	private List<Client> _clients;
	
	private Logger _log = Logger.getLogger(Room.class.getName());
}
