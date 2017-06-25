package misc;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import exceptions.GameException;
import game.FamilyMember;
import game.GameBoard;
import game.LeaderCard;
import game.Player;
import game.Position;

public interface ConnectionHandlerRemote extends Remote, Serializable {


	public void ping() throws RemoteException;

	public List<String> activateLeaderCard() throws RemoteException;
	
	public void activateLeaderCard(String card) throws RemoteException;

	public List<FamilyMember> putFamiliar() throws RemoteException;
	
	public List<String> putFamiliarWhich(String familiar) throws RemoteException;
	
	public void putFamiliarWhere(String position) throws RemoteException;
	
	public void onConnect() throws RemoteException;

	public boolean addMeToGame(String name) throws RemoteException;

	public void sendConfigFile(String file) throws RemoteException;

	//public boolean endTurn() throws RemoteException;

	public void doIspendMyFaithPoints(boolean doI) throws RemoteException;
	
	public List<String> dropLeaderCard() throws RemoteException;

	public void dropWhichLeaderCard(String leaderCard) throws RemoteException;

	public void sendInitialInformations(String name) throws RemoteException;

	public void setClientRemote(ClientRemote rmiConnectionServerHandler) throws RemoteException;

	public void sendChosenInitialCardLeader(String leader) throws RemoteException;
	
	//NEW:

	public GameBoard getBoard() throws RemoteException;
	
	public Player getMe() throws RemoteException;
	
	public void dropLeaderCard(LeaderCard card) throws RemoteException, GameException;

	public void activateLeaderCard(LeaderCard card) throws RemoteException, GameException;

	public void placeFamiliar(FamilyMember familiar, Position position) throws RemoteException, GameException;
	
	public void endTurn() throws RemoteException, GameException;

}
