package server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.GameException;
import game.Resource;
import misc.ClientRemote;
import misc.ConnectionHandlerRemote;
import util.CommandStrings;
import util.Constants;

public class RMIConnectionHandler extends ConnectionHandler implements ConnectionHandlerRemote, Serializable {

	@Override
	public void run() {
		
	}
	
	@Override
	public void setClient(ClientRemote rmiConnectionServerHandler) throws RemoteException {
		_clientConnectionHandler = rmiConnectionServerHandler;
	}

	@Override
	public void ping() throws RemoteException {
		_lastPing = Date.from(Instant.now());	}

	@Override
	public List<String> activateLeaderCard() throws RemoteException {
		try {
			return _theGame.getState().activateLeaderCard();
		} catch (GameException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			return new ArrayList<>();
		}
	}
	
	@Override
	public void activateLeaderCard(String card) throws RemoteException {
		try {
			_theGame.getState().activateWhichLeaderCard(card);
		} catch (GameException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
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
	
	public void startTurn(){
		try {
			_clientConnectionHandler.startTurn();
		} catch (RemoteException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	@Override
	public void sendInitialLeaderList(List<String> leadersList) throws RemoteException {
		_clientConnectionHandler.sendInitialLeaderList(leadersList);
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
	
	@Override
<<<<<<< Updated upstream
	public int spendCouncil(List<Resource> councilRewards) throws RemoteException {
		return _clientConnectionHandler.spendCouncil(councilRewards);
=======
	public void sendChosenInitialCardLeader(String leader) throws RemoteException {
		_theGame.manipulateInitialLeaderList(_client, leader);
>>>>>>> Stashed changes
	}
	
	private ClientRemote _clientConnectionHandler;
	private Date _lastPing;
	private transient Logger _log = Logger.getLogger(RMIConnectionHandler.class.getName());
}
