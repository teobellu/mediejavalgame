package client.network;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import misc.ConnectionHandlerRemote;
import misc.ServerRemote;
import util.Constants;

public class RMIConnectionServerHandler extends ConnectionServerHandler {

	public RMIConnectionServerHandler(String host, int port) {
		super(host, port);
	}
	
	@Override
	public void run() {
		try {
			Registry _registry = LocateRegistry.getRegistry(_host, _port);
			
			ServerRemote _serverRMI = (ServerRemote) _registry.lookup(Constants.RMI);
			
			_connectionHandler = (ConnectionHandlerRemote) _serverRMI.onConnect();
			
			_logger.info("RMIConnection is up");
			
			_isRunning = true;
			
		} catch (Exception e) {
			_logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	@Override
	public void sendName(String name) throws RemoteException {
		_connectionHandler.sendName(name);
	}

	@Override
	public void putFamiliar() throws RemoteException {
		_connectionHandler.putFamiliar();
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
	
	/*°***************metodi che attendono una risposta dal server*********************/
	
	public boolean hasMyTurnStarted() throws RemoteException{
		return _connectionHandler.hasMyTurnStarted();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private ServerRemote _serverRMI;
	private Registry _registry;
	private ConnectionHandlerRemote _connectionHandler;
	private final Logger _logger = Logger.getLogger(RMIConnectionServerHandler.class.getName());
	
	/****************/
	
	@Override
	public List<String> putFamiliarWhich(String familiar) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putFamiliarWhere(String position) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}
