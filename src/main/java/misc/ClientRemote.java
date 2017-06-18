package misc;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientRemote extends Remote {
	
	public void startTurn() throws RemoteException;

	public void sendInitialLeaderList(List<String> leadersList) throws RemoteException;
	
}
