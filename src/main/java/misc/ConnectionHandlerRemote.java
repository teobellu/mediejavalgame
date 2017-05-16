package misc;

import java.rmi.Remote;
import java.rmi.RemoteException;

import packets.Packet;

public interface ConnectionHandlerRemote extends Remote {
	
	public Packet readFromServer() throws RemoteException;
	
	public boolean sendToServer(Packet command) throws RemoteException;
}
