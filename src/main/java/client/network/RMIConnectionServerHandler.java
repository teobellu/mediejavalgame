package client.network;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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

	@Override
	public void sendConfigFile() throws RemoteException {
		_connectionHandler.sendConfigFile();
	}

	@Override
	public void activateLeaderCard() throws RemoteException {
		_connectionHandler.activateLeaderCard();
	}

	@Override
	public void ping() throws RemoteException {
		_connectionHandler.ping();		
	}
	
	private ServerRemote _serverRMI;
	private Registry _registry;
	private ConnectionHandlerRemote _connectionHandler;
	private final Logger _logger = Logger.getLogger(RMIConnectionServerHandler.class.getName());
}
