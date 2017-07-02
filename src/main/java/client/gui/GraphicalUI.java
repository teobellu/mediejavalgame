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
				_commandToGui.wait();
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
	public int selectInitialLeaders(List<LeaderCard> leaders) {
		// TODO Auto-generated method stub
		//TODO da cambiare con showInitialLeaderList (forse?)
		return 0;
	}

	@Override
	public int spendCouncil(List<Resource> options) {
		try {
			System.out.println("\nChiamato spendCouncil in GraphicalUI\n");
			addFromGraphicalToGUI(options);
			addToCommandToGui(CommandStrings.HANDLE_COUNCIL);
			
			synchronized (_commandToGui) {
				_commandToGui.wait();
			}
			
			return (int) returnFirstAndCleanCommand();
		} catch (InterruptedException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			return 0;
		}
	}

	@Override
	public int chooseCardCost(DevelopmentCard card) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void activateLeaderCard(String leaderName) throws RemoteException {
		_connectionHandler.activateLeaderCard(leaderName);
	}

	@Override
	public int chooseFamiliar(List<FamilyMember> familiars, String message) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void startTurn(GameBoard board, Player me) {
		addFromGraphicalToGUI(board, me);
		addToCommandToGui(CommandStrings.START_TURN);
	}

	@Override
	public int chooseLeader(String context, List<LeaderCard> tempList) {
		if(context.equals(CommandStrings.INITIAL_LEADER)){
			try {
				return showInitialLeaderList(tempList);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
			}
		}else if(context.equals(CommandStrings.CHOOSE_LEADER)){
			//TODO
		}
		System.out.println("ERRORE");
		return 0;
	}
	
	private int showInitialLeaderList(List<LeaderCard> leadersList) throws Exception {
		List<String> tempList = new ArrayList<>();
		
		for(LeaderCard s : leadersList){
			tempList.add(s.getName());
		}
		
		addFromGraphicalToGUI(tempList);
		addToCommandToGui(CommandStrings.INITIAL_LEADER);
		
		synchronized (_commandToGui) {
			_commandToGui.wait();
		}
		
		return (int) returnFirstAndCleanCommand();
	}

	@Override
	public int askInt(String message, int min, int max) {
		try {
			addFromGraphicalToGUI(message, min, max);
			addToCommandToGui(CommandStrings.ASK_INT);
			
			synchronized (_commandToGui) {
				_commandToGui.wait();
			}
			
			return (int) returnFirstAndCleanCommand();
		} catch (InterruptedException e) {
			// TODO: handle exception
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		System.out.println("\n###ERRORE###\n");
		return 0;
	}
	
	@Override
	public boolean askBoolean(String message) {
		try {
			addFromGraphicalToGUI(message);
			addToCommandToGui(CommandStrings.ASK_BOOLEAN);
			
			synchronized (_commandToGui) {
				_commandToGui.wait();
			}
			
			return (boolean) returnFirstAndCleanCommand();
		} catch (InterruptedException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		System.out.println("\n###ERRORE IN ASK BOOLEAN###\n");
		return false;
	}
	
	@Override
	public void placeFamiliar(String familiarColour, Position position) throws RemoteException{
		_connectionHandler.placeFamiliar(familiarColour, position);
	}
	
	@Override
	public void dropLeaderCard(String leaderName) throws RemoteException {
		_connectionHandler.dropLeaderCard(leaderName);
	}
	
	@Override
	public void endTurn() throws RemoteException{
		_connectionHandler.endTurn();
	}
	
	@Override
	public void showInfo(String str) {
		addFromGraphicalToGUI(str);
		addToCommandToGui(CommandStrings.INFO);
	}

	@Override
	public void showInfoWithBoardUpdate(String info, GameBoard board) {
		addFromGraphicalToGUI(info, board);
		addToCommandToGui(CommandStrings.INFO_BOARD);
	}
	
	private Object returnFirstAndCleanCommand(){
		synchronized (this) {
			Object obj = getFirstFromGUIToGraphical();
			_commandToGui.poll();
			System.out.println("\nCommand cleaned\n");
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
}
