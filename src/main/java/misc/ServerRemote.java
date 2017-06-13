package misc;

import java.rmi.Remote;
import java.rmi.RemoteException;

@FunctionalInterface
public interface ServerRemote extends Remote {

	public ConnectionHandlerRemote onConnect() throws RemoteException;
}
