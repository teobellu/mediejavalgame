package misc;

import java.rmi.Remote;
import java.rmi.RemoteException;

import util.Packet;

public interface ConnectionHandlerRemote extends Remote {
	public Packet readFromServer() throws RemoteException;
	
	public void sendToServer(Packet command) throws RemoteException;
}
