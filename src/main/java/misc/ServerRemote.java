package misc;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRemote extends Remote {

	public ConnectionHandlerRemote onConnect() throws RemoteException;
	
	public ConnectionHandlerRemote onReconnect(String uuid) throws RemoteException;
}
