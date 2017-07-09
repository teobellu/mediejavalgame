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
import exceptions.GameException;
import game.FamilyMember;
import game.GameBoard;
import game.LeaderCard;
import game.Player;
import game.Position;
import game.Resource;
import game.development.DevelopmentCard;
import javafx.application.Application;
import util.CommandStrings;

public class GraphicalUI implements UI {
	
	private ConcurrentLinkedQueue<String> _commandToGui = new ConcurrentLinkedQueue<>();
	
	private ConcurrentLinkedQueue<Object> _fromGraphicalToGUI = new ConcurrentLinkedQueue<>();
	
	private ConcurrentLinkedQueue<Object> _fromGUItoGraphical = new ConcurrentLinkedQueue<>();
	
	private File _xmlFile;
	
	private String _name;
	
	private static GraphicalUI _instance = null;
	
    private Logger _log = Logger.getLogger(GraphicalUI.class.getName());
    
    private ConnectionServerHandler _connectionHandler = null;
    
    private GameBoard _cachedBoard;
    
    private Player _cachedMe;
    
    private String _uuid;
	
	private GraphicalUI() {
	}
	
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
	
	public synchronized void addFromGraphicalToGUI(Object...objects){
		for(Object obj : objects){
			_fromGraphicalToGUI.add(obj);
		}
	}
	
	public synchronized void addFromGUIToGraphical(Object...objects){
		for(Object obj : objects){
			_fromGUItoGraphical.add(obj);
		}
	}
	
	public synchronized Object getFirstFromGraphicalToGUI(){
		return _fromGraphicalToGUI.poll();
	}
	
	public synchronized Object getFirstFromGUIToGraphical(){
		return _fromGUItoGraphical.poll();
	}
	
	public synchronized void addToCommandToGui(String command){
		_commandToGui.add(command);
	}
	
	public GameBoard getCachedBoard(){
		return _cachedBoard;
	}
	
	public Player getCachedMe(){
		return _cachedMe;
	}
	
	@Override
	public void activateLeaderCard(String leaderName) {
		try {
			_connectionHandler.activateLeaderCard(leaderName);
		} catch (RemoteException e) {
			_log.log(Level.INFO, e.getMessage(), e);
			addToCommandToGui(CommandStrings.CONNECTION_ERROR);
		}
	}
	
	@Override
	public void placeFamiliar(String familiarColour, Position position) {
		try {
			_connectionHandler.placeFamiliar(familiarColour, position);
		} catch (RemoteException e) {
			_log.log(Level.INFO, e.getMessage(), e);
			addToCommandToGui(CommandStrings.CONNECTION_ERROR);
		}
	}
	
	@Override
	public void dropLeaderCard(String leaderName) {
		try {
			_connectionHandler.dropLeaderCard(leaderName);
		} catch (RemoteException e) {
			_log.log(Level.INFO, e.getMessage(), e);
			addToCommandToGui(CommandStrings.CONNECTION_ERROR);
		}
	}
	
	@Override
	public void endTurn() {
		try {
			_connectionHandler.endTurn();
		} catch (RemoteException e) {
			_log.log(Level.INFO, e.getMessage(), e);
			addToCommandToGui(CommandStrings.CONNECTION_ERROR);
		}
	}

	@Override
	public void attemptReconnection() {
		try {
			addFromGraphicalToGUI("Attempting reconnection...");
			addToCommandToGui(CommandStrings.INFO);
			
//			boolean reconnected = 
					_connectionHandler.attemptReconnection(_uuid);
			
//			if(reconnected){
//				addFromGraphicalToGUI("Reconnected succesfully!");
//				addToCommandToGui(CommandStrings.INFO);
//				
//			} else {
//				addFromGraphicalToGUI("Failed to reconnect.");
//				addToCommandToGui(CommandStrings.INFO);
//				
//			}
		} catch (RemoteException e) {
			//TODO
			_log.log(Level.INFO, e.getMessage(), e);
		}
	}
	
	@Override
	public void reconnected(){
		addFromGraphicalToGUI("Reconnected succesfully");
		addToCommandToGui(CommandStrings.INFO);
	}

	@Override
	public void setUUID(String uuid) {
		_uuid = uuid;
	}
	
	public void shutdown(){
		_connectionHandler.shutdown();
	}

	@Override
	public void showVaticanSupport() {
		try{
			_connectionHandler.showVaticanSupport();
		} catch (RemoteException e) {
			_log.log(Level.INFO, e.getMessage(), e);
			addToCommandToGui(CommandStrings.CONNECTION_ERROR);
		}
	}

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
