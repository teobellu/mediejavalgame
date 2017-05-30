package misc;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ConnectionHandlerRemote extends Remote {

	public void sendName(String name) throws RemoteException;

	public void ping() throws RemoteException;

	public List<String> activateLeaderCard() throws RemoteException;
	
	public void activateLeaderCard(String card) throws RemoteException;

	public void sendConfigFile() throws RemoteException;

	public void putFamiliar() throws RemoteException;
	
	public void onConnect() throws RemoteException;

	public boolean addMeToGame() throws RemoteException;

	public void sendConfigFile(String file) throws RemoteException;
	
}
