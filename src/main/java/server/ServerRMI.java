package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import misc.ConnectionHandlerRemote;
import misc.ServerRemote;
import util.Constants;

/**
 * @author Jacopo
 *
 */
public class ServerRMI extends Thread implements ServerRemote {

	public void stopServer() {
		_IS_RUNNING = false;
	}
	
	@Override
	public void run() {
		
		try{
			try{
				_registry = LocateRegistry.getRegistry();
				
				/* Piccolo trick per risolvere un problema.
				 * Bisogna inizializzare il registro RMI da shell, cosa abbastanza sbatti da fare.
				 * _registry.list triggera un'eccezione se non e' stato avviato il registro RMI da shell,
				 * ed entrando nella catch viene creato ed inizializzato il registro RMI.
				 * */
				_registry.list();
			}
			catch(Exception e){
				_log.log(Level.OFF, e.getMessage(), e);
				_registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
			}
			ServerRemote stub = (ServerRemote) UnicastRemoteObject.exportObject(this, 0);
			_registry.bind(Constants.RMI, stub);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		_IS_RUNNING = true;
		
		_log.log(Level.INFO, "ServerRMI avviato");
	}

	public boolean isRunning() {
		return _IS_RUNNING;
	}
	
	@Override
	public ConnectionHandlerRemote onConnect(){
		if(_IS_RUNNING){
			try {
				RMIConnectionHandler connectionHandler = new RMIConnectionHandler();
				
				return (ConnectionHandlerRemote) UnicastRemoteObject.exportObject(connectionHandler, 0);
			} catch (RemoteException e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
				return null;
			}
		} else {
			return null;
		}
	}
	
	@Override
	public ConnectionHandlerRemote onReconnect(String uuid) throws RemoteException {
		for(Room room : Server.getInstance().getRooms()){
			for(Client client : room.getPlayers()){
				if(client.getUUID().equals(uuid)){
					return (ConnectionHandlerRemote) client.getConnectionHandler();
				}
			}
		}
		throw new RemoteException();
	}
	
	private Registry _registry = null;
	private transient Logger _log = Logger.getLogger(ServerRMI.class.getName());
	private boolean _IS_RUNNING = false;
}
