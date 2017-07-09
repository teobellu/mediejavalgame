package misc;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * ServerRMI remote interface
 * @author Jacopo
 *
 */
public interface ServerRemote extends Remote {

	/**
	 * Called on connection
	 * @return the server stub
	 * @throws RemoteException
	 */
	public ConnectionHandlerRemote onConnect() throws RemoteException;
	
	/**
	 * Called on reconnection
	 * @param uuid the id of the client
	 * @return the server stup
	 * @throws RemoteException
	 */
	public ConnectionHandlerRemote onReconnect(String uuid) throws RemoteException;
}
