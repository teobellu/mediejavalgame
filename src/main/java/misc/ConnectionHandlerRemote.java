package misc;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import game.GameBoard;
import game.Player;
import game.Position;

public interface ConnectionHandlerRemote extends Remote, Serializable {
	
	public boolean addMeToGame(String name) throws RemoteException;

	public void sendConfigFile(String file) throws RemoteException;

	public void setClientRemote(ClientRemote rmiConnectionServerHandler) throws RemoteException;
	
	public GameBoard getBoard() throws RemoteException;
	
	public Player getMe() throws RemoteException;
	
	public void dropLeaderCard(String leaderName) throws RemoteException;

	public void activateLeaderCard(String leaderName) throws RemoteException;

	public void placeFamiliar(String familiarColour, Position position) throws RemoteException;
	
	public void endTurn() throws RemoteException;

}
