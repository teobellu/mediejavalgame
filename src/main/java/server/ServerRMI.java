package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import misc.ServerRemote;
import util.Constants;

/**
 * @author Jacopo
 *
 */
public class ServerRMI extends Thread implements ServerRemote {

	public void stopServer() {
		
	}
	
	@Override
	public void run() {
		
		try{
			try{
				_registry = LocateRegistry.getRegistry();
				
				/* Piccolo trick per risolvere un problema.
				 * Bisogna inizializzare il registro RMI da shell, cosa abbastanza sbatti da fare.
				 * _registry.list triggera un'eccezione se non è stato avviato il registro RMI da shell,
				 * ed entrando nella catch viene creato ed inizializzato il registro RMI.
				 * */
				
				if(_registry==null){
					System.out.println("###FAIL###");
				}
				
				System.out.println(_registry.toString());
				
				_registry.list();
			}
			catch(Exception e){
				_registry = LocateRegistry.createRegistry(1099);
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
	
	
	/**
	 * A client should call this method to connect to the server.
	 * 
	 */
	public void onConnect(){
		/*TODO 
		 * Client chiama questo metodo. Viene creato un nuovo connectionhandler. Server cerca il registro del client.
		 * Viene chiamato onConnect() di Server.
		 * */
		ConnectionHandler connectionHandler = new RMIConnectionHandler();
		
		Server.getInstance().onConnect(connectionHandler);
	}
	
	private Registry _registry = null;
	private Logger _log = Logger.getLogger(ServerRMI.class.getName());
	private boolean _IS_RUNNING = false;
}
