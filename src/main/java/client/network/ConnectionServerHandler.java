package client.network;

import java.rmi.RemoteException;
import java.util.List;

import client.UI;
import exceptions.GameException;
import game.FamilyMember;
import game.GameBoard;
import game.LeaderCard;
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
	
	public abstract GameBoard getBoard() throws RemoteException;
	public abstract Player getMe() throws RemoteException;
	public abstract void dropLeaderCard(LeaderCard card) throws GameException, RemoteException;
	public abstract void activateLeaderCard(LeaderCard card) throws GameException, RemoteException;
	public abstract void placeFamiliar(FamilyMember familiar, Position position) throws GameException, RemoteException;
	
	/**
	 * Tells the server that I want to end my turn
	 * @throws GameException I can't end my turn
	 * @throws RemoteException Remote connection error
	 */
	public abstract void endTurn() throws GameException, RemoteException;
}
