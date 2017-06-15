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

import exceptions.GameException;
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
	
	public void setGame(){//TODO da chiamare questo metodo alla creazione del gioco(probabilmente nella room)
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
	public void sendToClient(List<String> messages) {
		synchronized (_outQueue) {
			for(String s : messages){
				_outQueue.add(s);
			}
			_outQueue.add(CommandStrings.END_TRANSMISSION);
		}
	}
	
	@Override
	public void sendToClient(String message) {
		List<String> list = new ArrayList<>();
		list.add(message);
		sendToClient(list);
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
		try {
			return _theGame.getState().endTurn();
		} catch (GameException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			return false;
		}
	}

	@Override
	public List<String> dropLeaderCard() throws RemoteException {
		try {
			return _theGame.getState().dropLeaderCard();
		} catch (GameException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			return new ArrayList<>();
		}
	}
	
	@Override
	public void dropWhichLeaderCard(String leaderCard) throws RemoteException {
		try {
			_theGame.getState().dropWhichLeaderCard(leaderCard);
		} catch (GameException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
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
	
	private Date _lastPing;
	private transient Logger _log = Logger.getLogger(RMIConnectionHandler.class.getName());
	private LinkedList<String> _outQueue = new LinkedList<>();
}
