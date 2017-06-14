package server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.Game;
import misc.ConnectionHandlerRemote;
import util.CommandStrings;
import util.Constants;

public class RMIConnectionHandler extends ConnectionHandler implements ConnectionHandlerRemote, Serializable {

	@Override
	public void run() {
		
	}

	@Override
	public void ping() throws RemoteException {
		_lastPing = Date.from(Instant.now());	}

	@Override
	public void activateLeaderCard() throws RemoteException {
		addToGameCommandList(CommandStrings.ACTIVATE_LEADER_CARD);
	}
	
	@Override
	public void activateLeaderCard(String card) throws RemoteException {
		addToGameCommandList(CommandStrings.ACTIVATE_WHICH_LEADER_CARD);
		addToGameCommandList(card);
	}

	@Override
	public void putFamiliar() throws RemoteException {
		addToGameCommandList(CommandStrings.PUT_FAMILIAR);
	}
	
	private boolean isTimeoutOver(){
		return Date.from(Instant.now()).getTime() > (_lastPing.getTime() + Constants.TIMEOUT_CONNESSION_MILLIS);
	}
	
	@Override
	public void onConnect() throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean addMeToGame() throws RemoteException {
		return Server.getInstance().addMeToGame(this);
	}
	
	@Override
	public void sendConfigFile(String file) throws RemoteException {
		_configFile = file;
	}
	
	public String startTurn(){
		//TODO
		return null;
	}
	
	@Override
	public boolean hasMyTurnStarted() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setGame(){
		if(_client!=null){
			_theGame = _client.getRoom().getGame();
		}
	}
	
	public void doIspendMyFaithPoints(boolean doI) throws RemoteException{
		addToGameCommandList(CommandStrings.SPEND_FAITH_POINTS);
		addToGameCommandList(Boolean.toString(doI));
	}
	
	private void addToGameCommandList(String command){
		synchronized (_theGame.getActionCommandList()) {
			_theGame.getActionCommandList().add(command);
		}
	}
	
	@Override
	public void sendToClient(String[] messages) {
		synchronized (_outQueue) {
			for(String s : messages){
				_outQueue.add(s);
			}
			_outQueue.add(CommandStrings.END_TRANSMISSION);
		}
	}
	
	@Override
	public void sendToClient(String message) {
		sendToClient(new String[]{message});
	}
	
	private String readResponse() throws RemoteException {
		String response;
		synchronized (_outQueue) {
			try {
				response = _outQueue.getFirst();
			} catch (NoSuchElementException e) {
				_log.log(Level.FINE, e.getMessage(), e);
				return null;
			}
		}
		
		return response;
	}
	
	@Override
	public boolean endTurn() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> dropLeaderCard() throws RemoteException {
		_theGame.getActionCommandList().add(CommandStrings.DROP_LEADER_CARD);
		List<String> leaders = new ArrayList<>();
		String leader = new String();
		do {
			leader = readResponse();
			if(leader!=null && !leader.equals(CommandStrings.END_TRANSMISSION)){
				leaders.add(leader);
			}
		} while(leader!=null && !leader.equals(CommandStrings.END_TRANSMISSION));
		
		return leaders;
	}
	
	@Override
	public void dropWhichLeaderCard(String leaderCard) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void spendCouncilPrivilege(String resource) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendInitialInformations(String name) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void putFamiliarWhere(String position) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	private transient Game _theGame = null;
	
	private boolean _hasMyTurnStarted = false;
	private Date _lastPing;
	private transient Logger _log = Logger.getLogger(RMIConnectionHandler.class.getName());
	private LinkedList<String> _outQueue = new LinkedList<>();
}
