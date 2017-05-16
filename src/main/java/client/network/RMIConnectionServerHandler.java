package client.network;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

import misc.ConnectionHandlerRemote;
import misc.ServerRemote;
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
			
			ServerRemote _serverRMI = (ServerRemote) _registry.lookup(Constants.RMI);
			
			_connectionHandler = (ConnectionHandlerRemote) _serverRMI.onConnect();
			
			_logger.info("RMIConnection is up");
			
			_isRunning = true;
		} catch (Exception e) {
			_logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	@Override
	public void sendToServer(Packet command) {
		try{
			if(!_connectionHandler.sendToServer(command)){
				
				Thread.sleep(500);
				
				if(!_connectionHandler.sendToServer(command)){
					throw new ConnectException("Cannot send message to server. What's going on?");
				}
			}
		} catch(InterruptedException e){
			_logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (RemoteException e) {
			_logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	public Packet readFromServer() {
		Packet command = null;
		try {
			command = _connectionHandler.readFromServer();
		} catch (RemoteException e) {
			_logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return command;
	}
	
	private ServerRemote _serverRMI;
	private Registry _registry;
	private ConnectionHandlerRemote _connectionHandler;
	private final Logger _logger = Logger.getLogger(RMIConnectionServerHandler.class.getName());
}
