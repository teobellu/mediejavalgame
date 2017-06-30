package client.network;

import java.rmi.RemoteException;

import client.UI;
import exceptions.GameException;
import game.FamilyMember;
import game.GameBoard;
import game.Player;
import game.Position;

public abstract class ConnectionServerHandler extends Thread {
	
	protected boolean _isRunning = false;
	protected final String _host;
	protected int _port;
	protected UI _ui;

	@Override
	public void run() {}
	
	public ConnectionServerHandler(String host, int port) {
		_host = host;
		_port = port;
	}
	
	public boolean isRunning(){
		return _isRunning;
	}
	
	public void shutdown(){
		_isRunning = false;
	}
	
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
	 * Ping the server that I'm still connected
	 * @throws RemoteException
	 */
	public abstract void ping() throws RemoteException;
		
	/**
	 * Add client to a game
	 * @param username 
	 * @return true if first player of a room, false otherwise
	 * @throws RemoteException
	 */
	public abstract boolean addMeToGame(String username) throws RemoteException;
	
	/**
	 * Ask the game board of my room to the server, generally used immediately after asking it, 
	 * from the user interface to show general information
	 * @return
	 * @throws RemoteException
	 */
	public abstract GameBoard getBoard() throws RemoteException;
	
	/**
	 * Ask my class Player to the server, generally used immediately after asking it, 
	 * from the user interface to show general information
	 * @return My class Player
	 * @throws RemoteException Remote connection error
	 */
	public abstract Player getMe() throws RemoteException;
	
	/**
	 * Tells the server that I want to drop (discard for gain a council privilege) a Leader Card
	 * @param card The card I want to drop/discard
	 * @throws GameException I can't discard this card
	 * @throws RemoteException Remote connection error
	 */
	public abstract void dropLeaderCard(String leaderName) throws GameException, RemoteException;
	
	/**
	 * Tells the server that I want to activate a Leader Card
	 * @param card The card I want to activate
	 * @throws GameException I can't activate this card
	 * @throws RemoteException Remote connection error
	 */
	public abstract void activateLeaderCard(String leaderName) throws GameException, RemoteException;
	
	/**
	 * Tells the server that I want to place a familiar
	 * @param familiar Which familiar I want to place
	 * @param position In that position I want to place
	 * @throws GameException I can't place that familiar in that position
	 * @throws RemoteException Remote connection error
	 */
	public abstract void placeFamiliar(FamilyMember familiar, Position position) throws GameException, RemoteException;
	
	/**
	 * Tells the server that I want to end my turn
	 * @throws GameException I can't end my turn
	 * @throws RemoteException Remote connection error
	 */
	public abstract void endTurn() throws GameException, RemoteException;
}
