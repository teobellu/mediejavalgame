package server;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import exceptions.GameException;
import game.Game;
import game.GameInformation;
import game.Resource;
import util.Constants;

public class Room extends Thread {
	
	private long startTimeout;
	private final long startTime;
	private Game theGame;
	private boolean isRunning = false;
	private List<Client> clients;
	private ConfigFileHandler fileHandler;
	
	private Logger log = Logger.getLogger(Room.class.getName());
	
	public Room(String configFile) {
		clients = new ArrayList<>();
		
		fileHandler = new ConfigFileHandler();
		
		/*https://stackoverflow.com/questions/19661047/java-convert-string-to-xml-and-parse-node*/
		try {
			Document doc;
			if(configFile!=null && !configFile.isEmpty()){
				doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(configFile)));
			} else {
				doc =  DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("default_settings.xml");
			}
			fileHandler.validate(doc);
			
		} catch (Exception e) {
			
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		startTime = new Date().getTime();
		
		startTimeout = fileHandler.timeoutStart * 1000;
	}
	
	@Override
	public void run(){
		theGame = new Game(this);
		
		for(Client c : clients){
			c.getConnectionHandler().setGame();
		}
		
		setupGame(theGame, fileHandler);
		
		new Thread(theGame).start();
		
		isRunning = true;
		
		log.info("New Room started");
	}
	
	private void setupGame(Game game, ConfigFileHandler fileHandler) {
		//GameBoard board = new GameBoard(fileHandler.SPACE_BONUS);
		GameInformation info = game.getGameInformation();
		//game.setBoard(board);
		info.createBoard(fileHandler.getSpaceBonus());
		info.setDevelopmentDeck(fileHandler.getDevelopmentDeck());
		info.setExcommunicationDeck(fileHandler.getExcommunicationDeck());
		
		Map<String, List<Resource>> map = fileHandler.getBonusPlayerDashboard();
		
		info.setBonusPlayerDashBoard(map);
		
		
		info.setBonusFaith(fileHandler.getBonusFaith());
		game.setTurnTimeout(fileHandler.timeoutTurn * 1000);
		
	}

	public synchronized void addPlayer(Client client) throws GameException {
		if(clients.size() < Constants.MAX_PLAYER){
			client.setRoom(this);
			clients.add(client);
		} else{
			throw new GameException("Cannot add player to this room, room is full. What's going on?");
		}
		
	}

	public boolean isFull() {
		return clients.size() == Constants.MAX_PLAYER;
	}
	
	private synchronized boolean hasEnoughPlayers(){
		return clients.size()>=Constants.MIN_PLAYER;
	}
	
	public boolean isReady(){
		if(hasEnoughPlayers()){
			for(Client player : clients){
				if(!player.isReady()){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean isRunning(){
		return isRunning;
	}
	
	public boolean isOver(){
		return theGame.isOver();
	}

	public List<Client> getPlayers(){
		return clients;
	}
	
	public void setStartTimeout(long time){
		startTimeout = time;
	}
	
	public void getConfig(){
		
	}
	
	public Game getGame(){
		return theGame;
	}
	
	public boolean isTimeoutOver(){
		long currentTime = new Date().getTime();
		return currentTime-startTime > startTimeout;
	}
	
	public synchronized void shutdown(){
		if(clients!=null && !clients.isEmpty()){
			clients.clear();
		}
		
		isRunning = false;
	}
}
