package client.network;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.FamilyMember;
import game.LeaderCard;
import game.Resource;
import misc.ClientRemote;
import misc.ConnectionHandlerRemote;
import misc.ServerRemote;
import util.Constants;

public class RMIConnectionServerHandler extends ConnectionServerHandler implements ClientRemote {
	
	private ConnectionHandlerRemote _connectionHandler;
	private final Logger _log = Logger.getLogger(RMIConnectionServerHandler.class.getName());

	public RMIConnectionServerHandler(String host, int port) {
		super(host, port);
		
		if(port==0){
			_port = Registry.REGISTRY_PORT;
		}
		
		try {
			Registry _registry = LocateRegistry.getRegistry(_host, _port);
			
			try{
				UnicastRemoteObject.exportObject((ClientRemote)this, Constants.DEFAULT_SOCKET_PORT+1);//TODO da rivedere
			} catch(ExportException e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
				UnicastRemoteObject.exportObject((ClientRemote)this, Constants.DEFAULT_SOCKET_PORT+2);
			}
			
			ServerRemote _serverRMI = (ServerRemote) _registry.lookup(Constants.RMI);
			
			_connectionHandler = (ConnectionHandlerRemote) _serverRMI.onConnect();
			
			_connectionHandler.setClientRemote(this);
			
			_log.info("RMIConnection is up");
			
			_isRunning = true;
			
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	@Override
	public void run() {
		
	}
	
	@Override
	public int sendInitialLeaderList(List<String> leadersList) throws RemoteException {
		try {
			System.out.println("METODO SBAGLIATO CHIAMATO");//TODO
			return _ui.showInitialLeaderList(leadersList);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			return 0;
			//TODO
		}
	}
	
	@Override
	public List<FamilyMember> putFamiliar() throws RemoteException {
		return _connectionHandler.putFamiliar();
	}
	
	@Override
	public List<String> putFamiliarWhich(String familiar) throws RemoteException {
		return _connectionHandler.putFamiliarWhich(familiar);
	}

	@Override
	public void putFamiliarWhere(String position) throws RemoteException {
		_connectionHandler.putFamiliarWhere(position);
	}

	/* (non-Javadoc)
	 * @see client.network.ConnectionServerHandler#activateLeaderCard()
	 */
	@Override
	public List<String> activateLeaderCard() throws RemoteException {
		return _connectionHandler.activateLeaderCard();
	}
	
	/* (non-Javadoc)
	 * @see client.network.ConnectionServerHandler#activateLeaderCard(java.lang.String)
	 */
	@Override
	public void activateLeaderCard(String card) throws RemoteException{
		_connectionHandler.activateLeaderCard(card);
	}

	@Override
	public void ping() throws RemoteException {
		_connectionHandler.ping();		
	}
	
	@Override
	public boolean addMeToGame(String name) throws RemoteException {
		return _connectionHandler.addMeToGame(name);
	}
	
	@Override
	public void sendConfigFile(String file) throws RemoteException {
		_connectionHandler.sendConfigFile(file);
	}

	@Override
	public void doIspendMyFaithPoints(boolean doI) throws RemoteException {
		_connectionHandler.doIspendMyFaithPoints(doI);
	}

	@Override
	public boolean endTurn() throws RemoteException {
		return _connectionHandler.endTurn();
	}

	@Override
	public List<String> dropLeaderCard() throws RemoteException {
		return _connectionHandler.dropLeaderCard();
	}

	@Override
	public void dropWhichLeaderCard(String leaderCard) throws RemoteException {
		_connectionHandler.dropWhichLeaderCard(leaderCard);
	}
	
	@Override
	public void startTurn() throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int spendCouncil(List<Resource> councilRewards) throws RemoteException {
		return _ui.spendCouncil(councilRewards);
	}

	public void sendChosenInitialCardLeader(String leader) throws RemoteException {
		_connectionHandler.sendChosenInitialCardLeader(leader);
	}
	
	
	
	@Override
	public int chooseFamiliar(List<FamilyMember> familiars, String message) throws RemoteException {
		return _ui.chooseFamiliar(familiars, message);
	}

	@Override
	public boolean ask(String message) throws RemoteException {
		return _ui.answerToAQuestion(message);
	}

	@Override
	public int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions) throws RemoteException {
		return _ui.chooseConvert(realPayOptions, realGainOptions);
	}

	@Override
	public int chooseLeader(List<LeaderCard> tempList) throws RemoteException {
		return _ui.chooseLeader(tempList);
		
		/*
		List<String> names = new ArrayList<>(); 
		 for(LeaderCard lc : tempList){
			 System.out.println(lc.getName());
			 names.add(lc.getName());
		 }
		 
		try {
			return _ui.showInitialLeaderList(names);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			return 0;
		}*/
	}

	@Override
	public int chooseDashboardBonus(Map<String, List<Resource>> bonus) throws RemoteException {
		return _ui.chooseDashboardBonus(bonus);
	}

	@Override
	public void notifyTurn() throws RemoteException {
		_ui.notifyTurn();
	}
	
}
