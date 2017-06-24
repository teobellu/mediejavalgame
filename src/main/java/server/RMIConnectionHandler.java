package server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.GameException;
import game.FamilyMember;
import game.LeaderCard;
import game.Resource;
import misc.ClientRemote;
import misc.ConnectionHandlerRemote;

public class RMIConnectionHandler extends ConnectionHandler implements ConnectionHandlerRemote, Serializable {

	private ClientRemote _clientConnectionHandler;
	private Date _lastPing;
	private transient Logger _log = Logger.getLogger(RMIConnectionHandler.class.getName());
	
	@Override
	public void run() {
		
	}
	
	@Override
	public void setClientRemote(ClientRemote rmiConnectionServerHandler) throws RemoteException {
		_clientConnectionHandler = rmiConnectionServerHandler;
	}

	@Override
	public void ping() throws RemoteException {
		_lastPing = Date.from(Instant.now());	
	}

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
	public void onConnect() throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean addMeToGame(String name) throws RemoteException {
		return Server.getInstance().addMeToGame(this, name);
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
	public int sendInitialLeaderList(List<String> leadersList) throws RemoteException {
		return _clientConnectionHandler.sendInitialLeaderList(leadersList);
	}
	
	public void setGame(){//TODO da chiamare questo metodo alla creazione del gioco(probabilmente nella room)
		if(_client!=null){
			_theGame = _client.getRoom().getGame();
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
	public void sendInitialInformations(String name) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void putFamiliarWhere(String position) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int spendCouncil(List<Resource> councilRewards) throws RemoteException {
		return _clientConnectionHandler.spendCouncil(councilRewards);
	}

	public void sendChosenInitialCardLeader(String leader) throws RemoteException {
		//_theGame.manipulateInitialLeaderList(_client, leader);TODO
	}
	
	@Override
	public int chooseFamiliar(List<FamilyMember> familiars, String message) throws RemoteException {
		return _clientConnectionHandler.chooseFamiliar(familiars, message);
	}

	@Override
	public boolean ask(String message) throws RemoteException {
		return _clientConnectionHandler.ask(message);
	}

	@Override
	public int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions) throws RemoteException {
		return _clientConnectionHandler.chooseConvert(realPayOptions, realGainOptions);
	}
	
	@Override
	public int chooseLeader(List<LeaderCard> tempList) throws RemoteException{
		for(LeaderCard lc : tempList){
			System.out.println(lc.getName());
		}
		return _clientConnectionHandler.chooseLeader(tempList);
	}

	@Override
	public void doIspendMyFaithPoints(boolean doI) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<FamilyMember> putFamiliar() throws RemoteException {
		try {
			return _theGame.getState().placeFamiliar();
		} catch (GameException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			return Collections.EMPTY_LIST;
		}
	}
	
	@Override
	public List<String> putFamiliarWhich(String familiar) throws RemoteException {
		try {
			return _theGame.getState().placeWhichFamiliar(familiar);
		} catch (GameException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			return Collections.EMPTY_LIST;
		}
	}
	
	@Override
	public int chooseDashboardBonus(Map<String, List<Resource>> bonus) throws RemoteException {
		return _clientConnectionHandler.chooseDashboardBonus(bonus);
	}

	@Override
	public void notifyTurn() throws RemoteException {
		_clientConnectionHandler.notifyTurn();
	}
}
