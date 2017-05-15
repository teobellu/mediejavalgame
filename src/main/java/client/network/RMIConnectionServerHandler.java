package client.network;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

import misc.ConnectionHandlerRemote;
import misc.ServerRemote;
import server.RMIConnectionHandler;
import util.Constants;
import util.Packet;

public class RMIConnectionServerHandler extends ConnectionServerHandler {

	public RMIConnectionServerHandler(String host, int port) {
		super(host, port);
	}
	
	@Override
	public void run() {
		try {
			Registry _registry = LocateRegistry.getRegistry(_host, _port);

			//TODO aprire registro rmi
			
			ServerRemote _serverRMI = (ServerRemote) _registry.lookup(Constants.RMI);
			
			_connectionHandler = (ConnectionHandlerRemote) _serverRMI.onConnect();
			
			
			_log.info("RMIConnection is up");
			
			_isRunning = true;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
	}
	
	@Override
	public void sendToServer(Packet command) {
		_connectionHandler.sendToServer(command);
	}

	@Override
	public Packet readFromServer() {
		Packet command = _connectionHandler.readFromServer();
		return command;
	}
	
	private ServerRemote _serverRMI;
	private Registry _registry;
	private ConnectionHandlerRemote _connectionHandler;
	private final Logger _log = Logger.getLogger(RMIConnectionServerHandler.class.getName());
}
