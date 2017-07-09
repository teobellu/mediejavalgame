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
	
	/**
	 * The timeout before starting the game
	 */
	private long startTimeout;
	/**
	 * When the room has been created
	 */
	private final long startTime;
	/**
	 * The game of this room
	 */
	private Game theGame;
	/**
	 * Am i running?
	 */
	private boolean isRunning = false;
	/**
	 * List of clients of this room
	 */
	private List<Client> clients;
	/**
	 * Parser of the config file
	 */
	private ConfigFileHandler fileHandler;
	/**
	 * The logger
	 */
	private Logger log = Logger.getLogger(Room.class.getName());
	
	/**
	 * Create a room, which handles the creation of a game
	 * @param configFile
	 */
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
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
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
	
	/**
	 * Initializes the game
	 * @param game the game
	 * @param fileHandler the config file parser
	 */
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

	/**
	 * Add a player to the room
	 * @param client the client
	 * @throws GameException
	 */
	public synchronized void addPlayer(Client client) throws GameException {
		if(clients.size() < Constants.MAX_PLAYER){
			client.setRoom(this);
			clients.add(client);
		} else{
			throw new GameException("Cannot add player to this room, room is full. What's going on?");
		}
		
	}

	/**
	 * Player > 4?
	 * @return yes or no
	 */
	public boolean isFull() {
		return clients.size() == Constants.MAX_PLAYER;
	}
	
	/**
	 * Player > 2?
	 * @return yes or no
	 */
	private synchronized boolean hasEnoughPlayers(){
		return clients.size()>=Constants.MIN_PLAYER;
	}
	
	/**
	 * Am i ready?
	 * @return yes or no
	 */
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
	
	/**
	 * Am i running?
	 * @return yes or no
	 */
	public boolean isRunning(){
		return isRunning;
	}
	
	/**
	 * Is the game over?
	 * @return yes or no
	 */
	public boolean isOver(){
		return theGame.isOver();
	}

	/**
	 * Get the list of players
	 * @return
	 */
	public List<Client> getPlayers(){
		return clients;
	}
	
	/**
	 * Get the game of the room
	 * @return the game
	 */
	public Game getGame(){
		return theGame;
	}
	
	/**
	 * Is the timeout over?
	 * @return yes or no
	 */
	public boolean isTimeoutOver(){
		long currentTime = new Date().getTime();
		return currentTime-startTime > startTimeout;
	}
	
	/**
	 * Shutdown the room
	 */
	public synchronized void shutdown(){
		if(clients!=null && !clients.isEmpty()){
			clients.clear();
		}
		
		isRunning = false;
	}
}
