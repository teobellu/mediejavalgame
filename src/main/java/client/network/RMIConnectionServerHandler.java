package client.network;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.Resource;
import misc.ClientRemote;
import misc.ConnectionHandlerRemote;
import misc.ServerRemote;
import util.Constants;

public class RMIConnectionServerHandler extends ConnectionServerHandler implements ClientRemote {

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
			
			_connectionHandler.setClient(this);
			
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
	public void sendInitialLeaderList(List<String> leadersList) throws RemoteException {
		_ui.showInitialLeaderList(leadersList);
	}
	
	@Override
	public List<String> putFamiliar() throws RemoteException {
		_connectionHandler.putFamiliar();
		//TODO
		return null;
	}
	
	@Override
	public List<String> putFamiliarWhich(String familiar) throws RemoteException {
		//TODO
		return null;
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
	public void onConnect() throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean addMeToGame() throws RemoteException {
		return _connectionHandler.addMeToGame();
	}
	
	@Override
	public void sendConfigFile(String file) throws RemoteException {
		_connectionHandler.sendConfigFile(file);
	}
	
	@Override
	public void sendInitialInformations(String name) throws RemoteException {
		_connectionHandler.sendInitialInformations(name);
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
		_connectionHandler.dropWhichLeaderCard( leaderCard);
	}

	@Override
	public void spendCouncilPrivilege(String resource) throws RemoteException {
		_connectionHandler.spendCouncilPrivilege(resource);
	}
	
	@Override
	public void startTurn() throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	
//<<<<<<< Updated upstream
	@Override
	public int spendCouncil(List<Resource> councilRewards) throws RemoteException {
		return _ui.spendCouncil(councilRewards);
	}
//=======
	public void sendChosenInitialCardLeader(String leader) throws RemoteException {
		_connectionHandler.sendChosenInitialCardLeader(leader);
//>>>>>>> Stashed changes
	}
	
	private ConnectionHandlerRemote _connectionHandler;
	private final Logger _log = Logger.getLogger(RMIConnectionServerHandler.class.getName());
	
}
