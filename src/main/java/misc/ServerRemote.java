package misc;

import java.rmi.Remote;
import java.rmi.RemoteException;

import server.RMIConnectionHandler;

@FunctionalInterface
public interface ServerRemote extends Remote {

	public RMIConnectionHandler onConnect() throws RemoteException;
}
