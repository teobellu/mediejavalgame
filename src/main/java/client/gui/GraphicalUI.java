package client.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
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
import game.Resource;
import game.development.DevelopmentCard;
import javafx.application.Application;

public class GraphicalUI implements UI {
	
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
		_connectionHandler.setClient(getInstance());
		
		new Thread(_connectionHandler).start();
		
		sendXML();
	}
	
	public ConnectionServerHandler getConnection(){
		return _connectionHandler;
	}

	@Override
	public String askForConfigFile() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setName(String name){
		_name = name;
	}
	
	public void prepareXmlFile(File file){
		_xmlFile = file;
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
	public List<String> dropLeaderCard(){
		List<String> leaders = new ArrayList<>();
		try {
			leaders = _connectionHandler.dropLeaderCard();
		} catch (RemoteException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		return leaders;
	}
	
	//TODO override?
	public void dropWhichLeaderCard(String leader){
		try {
			_connectionHandler.dropWhichLeaderCard(leader);
		} catch (RemoteException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	public void addMeToGame(String username) throws GameException {// TODO override?
		try {
			if(!_connectionHandler.addMeToGame(username)){
				throw new GameException("Name already taken");
			}
		} catch (RemoteException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	@Override
	public synchronized int showInitialLeaderList(List<String> leadersList) throws Exception {
		_returnObject = leadersList;
		
		System.out.println("Waiting for leader card...");
		_returnObject.wait();
		
		for(String s : leadersList){
			if(((List<String>) _returnObject).contains(s)){
				continue;
			} else {
				return leadersList.indexOf(s);
			}
		}
		
		throw new Exception("Cannot find element in list. What's going on");
	}
	
	public Object getReturnObject(){
		return _returnObject;
	}
	
	public void setReturnObject(Object obj){
		_returnObject = obj;
		_returnObject.notify();
	}
	
	public void setGUI(GUI gui){
		_GUI = gui;
	}
	
	@Override
	public void showInfo(String str) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showBoard(GameBoard board) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showWhatIHave(Player me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyPutFamiliar(FamilyMember familiar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyDiscardLeaderCard(String playerName, String card) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int selectInitialLeaders(List<LeaderCard> leaders) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int spendCouncil(List<Resource> options) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int chooseCardCost(DevelopmentCard card) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean activateLeaderCard(LeaderCard card) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int chooseFamiliar(List<FamilyMember> familiars, String message) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean answerToAQuestion(String message) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private Object _returnObject = new Object();
	
	private GUI _GUI;
	
	private File _xmlFile;
	
	private String _name;
	
	private static GraphicalUI _instance = null;
	
    private Logger _log = Logger.getLogger(GraphicalUI.class.getName());
    
    private ConnectionServerHandler _connectionHandler = null;
}
