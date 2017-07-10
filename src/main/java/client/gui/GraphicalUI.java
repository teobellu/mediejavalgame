package client.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.UI;
import client.network.ConnectionServerHandler;
import client.network.ConnectionServerHandlerFactory;
import javafx.application.Application;
import model.DevelopmentCard;
import model.FamilyMember;
import model.GameBoard;
import model.LeaderCard;
import model.Player;
import model.Position;
import model.Resource;
import model.exceptions.GameException;
import util.CommandStrings;

/**
 * Middleware controller for the JavaFX GUI Application
 * @author Jacopo
 * @author Matteo
 */
public class GraphicalUI implements UI {
	
	/**
	 * Queue of commands to the JavaFX Application
	 */
	private ConcurrentLinkedQueue<String> _commandToGui = new ConcurrentLinkedQueue<>();
	
	/**
	 * Queue of objects to the JavaFX Application
	 */
	private ConcurrentLinkedQueue<Object> _fromGraphicalToGUI = new ConcurrentLinkedQueue<>();
	
	/**
	 * Queue of objects from the JavaFX Application
	 */
	private ConcurrentLinkedQueue<Object> _fromGUItoGraphical = new ConcurrentLinkedQueue<>();
	
	/**
	 * the selected customConfigFile
	 */
	private File _xmlFile;
	
	/**
	 * the player's name
	 */
	private String _name;
	
	/**
	 * Instance for singleton constructor
	 */
	private static GraphicalUI _instance = null;
	
    /**
     * the logger
     */
    private Logger _log = Logger.getLogger(GraphicalUI.class.getName());
    
    /**
     * Handles communication with the server
     */
    private ConnectionServerHandler _connectionHandler = null;
    
    /**
     * Cached board to not overload the connection
     */
    private GameBoard _cachedBoard;
    
    /**
     * Cached player to not overload the connection
     */
    private Player _cachedMe;
    
    /**
     * Player's generated unique id
     */
    private String _uuid;
	
	/**
	 * Private constructor
	 */
	private GraphicalUI() {
	}
	
	/**
	 * Get this class instance
	 * @return
	 */
	public static GraphicalUI getInstance(){
		if(_instance==null){
			synchronized (GraphicalUI.class) {
				if(_instance==null){
					_instance = new GraphicalUI();
				}
			}
		}
		return _instance;
	}
	
	@Override
	public void run(){
		Application.launch(GUI.class, new String[0]);
	}

	@Override
	public void setConnection(String connectionType, String host, int port) {
		_connectionHandler = ConnectionServerHandlerFactory.getConnectionServerHandler(connectionType, host, port);
		_connectionHandler.setClient(this);
		
		new Thread(_connectionHandler).start();
		
		sendXML();
	}
	
	public ConnectionServerHandler getConnection(){
		return _connectionHandler;
	}
	
	public void setPlayerName(String name){
		_name = name;
	}
	
	public void prepareXmlFile(File file){
		_xmlFile = file;
	}
	
	public String getPlayerName(){
		return _name;
	}
	
	private void sendXML(){
		FileReader customConfig = null;
		try {
			if(_xmlFile!=null){
				customConfig = new FileReader(_xmlFile);
				
				StringBuilder sb = new StringBuilder();
				BufferedReader br = new BufferedReader(customConfig);
				String line;
				while((line = br.readLine() ) != null) {
				    sb.append(line);
				}
				
				br.close();
				_connectionHandler.sendConfigFile(sb.toString());
			} else {
				_connectionHandler.sendConfigFile("");
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		finally{
			if (customConfig != null)
				try {
					customConfig.close();
				} catch (IOException e) {
					_log.log(Level.SEVERE, e.getMessage(), e);
				}
		}
	}
	
	@Override
	public void addMeToGame(String username) throws GameException {
		try {
			if(!_connectionHandler.addMeToGame(username)){
				throw new GameException("Name already taken");
			}
		} catch (RemoteException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	@Override
	public int chooseDashboardBonus(Map<String, List<Resource>> bonus) {
		addFromGraphicalToGUI(bonus);
		addToCommandToGui(CommandStrings.INITIAL_PERSONAL_BONUS);
		
		try{
			synchronized (_commandToGui) {
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_commandToGui.wait();
					break;
				}
			}
			
			return (int) returnFirstAndCleanCommand();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return 0;
	}
	
	public synchronized ConcurrentLinkedQueue<String> getCommandToGui(){
		return _commandToGui;
	}
	
	public void notifyCommandToGui(){
		synchronized (_commandToGui) {
			_commandToGui.notify();
		}
	}

	@Override
	public int spendCouncil(List<Resource> options) {
		try {
			addFromGraphicalToGUI(options);
			addToCommandToGui(CommandStrings.HANDLE_COUNCIL);
			
			synchronized (_commandToGui) {
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_commandToGui.wait();
					break;
				}
			}
			
			return (int) returnFirstAndCleanCommand();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			_log.log(Level.SEVERE, e.getMessage(), e);
			return 0;
		}
	}

	@Override
	public void startTurn(GameBoard board, Player me) {
		_cachedBoard = board;
		_cachedMe = me;
		addFromGraphicalToGUI(board, me);
		addToCommandToGui(CommandStrings.START_TURN);
	}

	@Override
	public int chooseLeader(String context, List<LeaderCard> tempList) {
		
		try {
			List<String> leadersList = new ArrayList<>();
			
			for(LeaderCard s : tempList){
				leadersList.add(s.getName());
			}
			
			addFromGraphicalToGUI(leadersList);
			addToCommandToGui(context);
			
			synchronized (_commandToGui) {
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_commandToGui.wait();
					break;
				}
			}
			
			return (int) returnFirstAndCleanCommand();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			_log.log(Level.SEVERE, e.getMessage(), e);
			return 0;
		}
	}

	@Override
	public int askInt(String message, int min, int max) {
		try {
			addFromGraphicalToGUI(message, min, max);
			addToCommandToGui(CommandStrings.ASK_INT);
			
			synchronized (_commandToGui) {
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_commandToGui.wait();
					break;
				}
			}
			
			return (int) returnFirstAndCleanCommand();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return 0;
	}
	
	@Override
	public boolean askBoolean(String message) {
		try {
			addFromGraphicalToGUI(message);
			addToCommandToGui(CommandStrings.ASK_BOOLEAN);
			
			synchronized (_commandToGui) {
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_commandToGui.wait();
					break;
				}
			}
			
			return (boolean) returnFirstAndCleanCommand();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		return false;
	}
	
	@Override
	public int chooseCardCost(DevelopmentCard card) {
		try {
			addFromGraphicalToGUI(card);
			addToCommandToGui(CommandStrings.CHOOSE_COST);
			
			synchronized (_commandToGui) {
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_commandToGui.wait();
					break;
				}
			}
			
			return (int) returnFirstAndCleanCommand();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return 0;
	}

	@Override
	public int chooseFamiliar(List<FamilyMember> familiars, String message) {
		try {
			addFromGraphicalToGUI(familiars, message);
			addToCommandToGui(CommandStrings.CHOOSE_FAMILIAR);
			
			synchronized (_commandToGui) {
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_commandToGui.wait();
					break;
				}
			}
			
			return (int) returnFirstAndCleanCommand();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return 0;
	}

	@Override
	public int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions) {
		try {
			addFromGraphicalToGUI(realPayOptions, realGainOptions);
			addToCommandToGui(CommandStrings.CHOOSE_CONVERT);
			
			synchronized (_commandToGui) {
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_commandToGui.wait();
					break;
				}
			}
			
			return (int) returnFirstAndCleanCommand();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return 0;
	}
	
	@Override
	public void showInfo(String str) {
		addFromGraphicalToGUI(str);
		addToCommandToGui(CommandStrings.INFO);
	}

	@Override
	public void showInfo(String info, GameBoard board) {
		_cachedBoard = board;
		addFromGraphicalToGUI(info);
		addToCommandToGui(CommandStrings.INFO_BOARD);
	}
	
	@Override
	public void showInfo(String message, Player me) {
		_cachedMe = me;
		addFromGraphicalToGUI(message);
		addToCommandToGui(CommandStrings.INFO_PLAYER);
	}

	@Override
	public void showInfo(String message, GameBoard board, Player me) {
		_cachedBoard = board;
		_cachedMe = me;
		addFromGraphicalToGUI(message);
		addToCommandToGui(CommandStrings.INFO_BOARD_PLAYER);
	}
	
	private Object returnFirstAndCleanCommand(){
		synchronized (this) {
			Object obj = getFirstFromGUIToGraphical();
			_commandToGui.poll();
			return obj;
		}
	}
	
	/**
	 * Put some objects in the queue to the GUI
	 * @param objects the objects
	 */
	public synchronized void addFromGraphicalToGUI(Object...objects){
		for(Object obj : objects){
			_fromGraphicalToGUI.add(obj);
		}
	}
	
	/**
	 * Put some objects in the queue from the GUI
	 * @param objects the objects
	 */
	public synchronized void addFromGUIToGraphical(Object...objects){
		for(Object obj : objects){
			_fromGUItoGraphical.add(obj);
		}
	}
	
	/**
	 * Fetch an object from the queue to the GUI
	 * @return
	 */
	public synchronized Object getFirstFromGraphicalToGUI(){
		return _fromGraphicalToGUI.poll();
	}
	
	/**
	 * Fetch an object from the queue starting from the GUI
	 * @return
	 */
	public synchronized Object getFirstFromGUIToGraphical(){
		return _fromGUItoGraphical.poll();
	}
	
	/**
	 * Put some objects in the commands queue to the GUI
	 * @param command
	 */
	public synchronized void addToCommandToGui(String command){
		_commandToGui.add(command);
	}
	
	/**
	 * Get the cached board
	 * @return the board
	 */
	public GameBoard getCachedBoard(){
		return _cachedBoard;
	}
	
	/**
	 * Get the cached player
	 * @return the player
	 */
	public Player getCachedMe(){
		return _cachedMe;
	}
	
	/* (non-Javadoc)
	 * @see client.UI#activateLeaderCard(java.lang.String)
	 */
	@Override
	public void activateLeaderCard(String leaderName) {
		try {
			_connectionHandler.activateLeaderCard(leaderName);
		} catch (RemoteException e) {
			_log.log(Level.INFO, e.getMessage(), e);
			addToCommandToGui(CommandStrings.CONNECTION_ERROR);
		}
	}
	
	/* (non-Javadoc)
	 * @see client.UI#placeFamiliar(java.lang.String, model.Position)
	 */
	@Override
	public void placeFamiliar(String familiarColour, Position position) {
		try {
			_connectionHandler.placeFamiliar(familiarColour, position);
		} catch (RemoteException e) {
			_log.log(Level.INFO, e.getMessage(), e);
			addToCommandToGui(CommandStrings.CONNECTION_ERROR);
		}
	}
	
	/* (non-Javadoc)
	 * @see client.UI#dropLeaderCard(java.lang.String)
	 */
	@Override
	public void dropLeaderCard(String leaderName) {
		try {
			_connectionHandler.dropLeaderCard(leaderName);
		} catch (RemoteException e) {
			_log.log(Level.INFO, e.getMessage(), e);
			addToCommandToGui(CommandStrings.CONNECTION_ERROR);
		}
	}
	
	/* (non-Javadoc)
	 * @see client.UI#endTurn()
	 */
	@Override
	public void endTurn() {
		try {
			_connectionHandler.endTurn();
		} catch (RemoteException e) {
			_log.log(Level.INFO, e.getMessage(), e);
			addToCommandToGui(CommandStrings.CONNECTION_ERROR);
		}
	}

	/* (non-Javadoc)
	 * @see client.UI#attemptReconnection()
	 */
	@Override
	public void attemptReconnection() {
		try {
			addFromGraphicalToGUI("Attempting reconnection...");
			addToCommandToGui(CommandStrings.INFO);
			
			_connectionHandler.attemptReconnection(_uuid);
		} catch (RemoteException e) {
			_log.log(Level.INFO, e.getMessage(), e);
		}
	}
	
	/* (non-Javadoc)
	 * @see client.UI#reconnected()
	 */
	@Override
	public void reconnected(){
		addFromGraphicalToGUI("Reconnected succesfully");
		addToCommandToGui(CommandStrings.INFO);
	}

	/* (non-Javadoc)
	 * @see client.UI#setUUID(java.lang.String)
	 */
	@Override
	public void setUUID(String uuid) {
		_uuid = uuid;
	}
	
	/**
	 * Shutdown the GraphicalUI
	 */
	public void shutdown(){
		_connectionHandler.shutdown();
	}

	/* (non-Javadoc)
	 * @see client.UI#showVaticanSupport()
	 */
	@Override
	public void showVaticanSupport() {
		try{
			_connectionHandler.showVaticanSupport();
		} catch (RemoteException e) {
			_log.log(Level.INFO, e.getMessage(), e);
			addToCommandToGui(CommandStrings.CONNECTION_ERROR);
		}
	}

	/* (non-Javadoc)
	 * @see client.UI#activateOPTLeaders()
	 */
	@Override
	public void activateOPTLeaders() {
		try{
			_connectionHandler.activateOPTLeaders();
		} catch (RemoteException e) {
			_log.log(Level.INFO, e.getMessage(), e);
			addToCommandToGui(CommandStrings.CONNECTION_ERROR);
		}
	}
}
