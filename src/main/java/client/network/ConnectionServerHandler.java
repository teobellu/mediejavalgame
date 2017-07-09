package client.network;

import java.rmi.RemoteException;

import client.UI;
import model.Position;
import model.exceptions.GameException;

public abstract class ConnectionServerHandler extends Thread {
	
	protected boolean _isRunning = false;
	protected final String _host;
	protected int _port;
	protected UI _ui;

	@Override
	public void run() {}
	
	/**
	 * Constructor
	 * @param host server's address
	 * @param port server's port
	 */
	public ConnectionServerHandler(String host, int port) {
		_host = host;
		_port = port;
	}
	
	/**
	 * No-args constructor, because of sonarqube compliance
	 */
	public ConnectionServerHandler() {
		_host = "";
		_port = 0;
	}
	
	/**
	 * Am I running?
	 * @return true or false
 	 */
	public boolean isRunning(){
		return _isRunning;
	}
	
	/**
	 * Shut me down
	 */
	public void shutdown(){
		_isRunning = false;
	}
	
	/**
	 * Set the client to this handler
	 * @param client
	 */
	public void setClient(UI client){
		_ui = client;
	}
	

	/**
	 * Send the server the custom XML file, 
	 * or an empty {@link String} if he wants to use the default file
	 * @param file the file to send, or an empty {@link String}
	 * @throws RemoteException
	 */
	public abstract void sendConfigFile(String file) throws RemoteException;
		
	/**
	 * Add client to a game
	 * @param username 
	 * @return true if first player of a room, false otherwise
	 * @throws RemoteException
	 */
	public abstract boolean addMeToGame(String username) throws RemoteException;
	
	/**
	 * Tells the server that I want to drop (discard for gain a council privilege) a Leader Card
	 * @param card The card I want to drop/discard
	 * @throws GameException I can't discard this card
	 * @throws RemoteException Remote connection error
	 */
	public abstract void dropLeaderCard(String leaderName) throws RemoteException;
	
	/**
	 * Tells the server that I want to activate a Leader Card
	 * @param card The card I want to activate
	 * @throws GameException I can't activate this card
	 * @throws RemoteException Remote connection error
	 */
	public abstract void activateLeaderCard(String leaderName) throws RemoteException;
	
	/**
	 * Tells the server that I want to place a familiar
	 * @param familiar Which familiar I want to place
	 * @param position In that position I want to place
	 * @throws GameException I can't place that familiar in that position
	 * @throws RemoteException Remote connection error
	 */
	public abstract void placeFamiliar(String familiarColour, Position position) throws RemoteException;
	
	/**
	 * Tells the server that I want to end my turn
	 * @throws GameException I can't end my turn
	 * @throws RemoteException Remote connection error
	 */
	public abstract void endTurn() throws RemoteException;

	/**
	 * Try to reconnect to the server after a RemoteException
	 * @param _uuid my unique uuid
	 * @throws RemoteException
	 */
	public abstract void attemptReconnection(String _uuid) throws RemoteException;

	public abstract void showVaticanSupport() throws RemoteException;

	public abstract void activateOPTLeaders() throws RemoteException;
}
