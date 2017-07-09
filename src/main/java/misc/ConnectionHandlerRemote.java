package misc;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import game.Position;

/**
 * RMIConnectionHandler remote interface
 * @author Jacopo
 *
 */
public interface ConnectionHandlerRemote extends Remote, Serializable {
	
	/**
	 * Add me to a game
	 * @param name player's name
	 * @return am I in a game?
	 * @throws RemoteException
	 */
	public boolean addMeToGame(String name) throws RemoteException;

	/**
	 * Send a custom configuration file
	 * @param file the file
	 * @throws RemoteException
	 */
	public void sendConfigFile(String file) throws RemoteException;

	/**
	 * Associate client and remote interface
	 * @param rmiConnectionServerHandler
	 * @throws RemoteException
	 */
	public void setClientRemote(ClientRemote rmiConnectionServerHandler) throws RemoteException;
	
	/**
	 * Drop a leader card
	 * @param leaderName the card
	 * @throws RemoteException
	 */
	public void dropLeaderCard(String leaderName) throws RemoteException;

	/**
	 * Activate a leader card
	 * @param leaderName the card
	 * @throws RemoteException
	 */
	public void activateLeaderCard(String leaderName) throws RemoteException;

	/**
	 * Place a familiar
	 * @param familiarColour the familiar
	 * @param position where to put it
	 * @throws RemoteException
	 */
	public void placeFamiliar(String familiarColour, Position position) throws RemoteException;
	
	/**
	 * End the turn
	 * @throws RemoteException
	 */
	public void endTurn() throws RemoteException;

	/**
	 * Tell the server that you want to show your support to the Vatican
	 * @param resource your faith points
	 * @throws RemoteException
	 */
	public void showVaticanSupport(Integer resource) throws RemoteException;

}
