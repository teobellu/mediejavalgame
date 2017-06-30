package misc;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import exceptions.GameException;
import game.FamilyMember;
import game.GameBoard;
import game.LeaderCard;
import game.Player;
import game.Position;

public interface ConnectionHandlerRemote extends Remote, Serializable {


	public void ping() throws RemoteException;
	
	public void onConnect() throws RemoteException;

	public boolean addMeToGame(String name) throws RemoteException;

	public void sendConfigFile(String file) throws RemoteException;

	public void setClientRemote(ClientRemote rmiConnectionServerHandler) throws RemoteException;
	
	//NEW:

	public GameBoard getBoard() throws RemoteException;
	
	public Player getMe() throws RemoteException;
	
	public void dropLeaderCard(String leaderName) throws RemoteException, GameException;

	public void activateLeaderCard(LeaderCard card) throws RemoteException, GameException;

	public void placeFamiliar(FamilyMember familiar, Position position) throws RemoteException, GameException;
	
	public void endTurn() throws RemoteException, GameException;

}
