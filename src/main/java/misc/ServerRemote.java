package misc;

import java.rmi.Remote;
import java.rmi.RemoteException;

import server.ConnectionHandler;

@FunctionalInterface
public interface ServerRemote extends Remote {

	public ConnectionHandler onConnect() throws RemoteException;
}
