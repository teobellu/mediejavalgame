package client.network;

import java.rmi.RemoteException;
import java.util.List;

import client.Client;

public abstract class ConnectionServerHandler extends Thread {

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
	
	public void setClient(Client client){
		_client = client;
	}
	
	/**
	 * Setup me
	 * @param name the name of the player
	 * @throws RemoteException
	 */
	public abstract void sendInitialInformations(String name) throws RemoteException;
	
	/**
	 * Tell the server that I want to put a familiar somewhere
	 * @return the list of available familiars
	 * @throws RemoteException
	 */
	public abstract List<String> putFamiliar() throws RemoteException;
	
	
	/**
	 * Tell the server which familiar I want to place down
	 * @param familiar the familiar I want to put down
	 * @return list of available places
	 * @throws RemoteException
	 */
	public abstract List<String> putFamiliarWhich(String familiar) throws RemoteException;
	
	/**
	 * Tell the server where I want to put the familiar
	 * @param position where i want to put the familiar
	 * @throws RemoteException
	 */
	public abstract void putFamiliarWhere(String position) throws RemoteException;

	/**
	 * Send the server the custom XML file, 
	 * or an empty {@link String} if he wants to use the default file
	 * @param file the file to send, or an empty {@link String}
	 * @throws RemoteException
	 */
	public abstract void sendConfigFile(String file) throws RemoteException;
	
	/**
	 * Tell the server that you want to activate a leader card
	 * @throws RemoteException
	 */
	public abstract void activateLeaderCard() throws RemoteException;
	
	/**
	 * Tell the server which leader card you want to activate
	 * @param card leader you want to activate
	 * @throws RemoteException
	 */
	public abstract void activateLeaderCard(String card) throws RemoteException;
		
	/**
	 * Ping the server that I'm still connected
	 * @throws RemoteException
	 */
	public abstract void ping() throws RemoteException;
	
	public abstract void onConnect() throws RemoteException;
	
	/**
	 * Add client to a game
	 * @return true if first player of a room, false otherwise
	 * @throws RemoteException
	 */
	public abstract boolean addMeToGame() throws RemoteException;
	
	/**
	 * Tell the server if I want to spend my faith points
	 * @param doI
	 * @throws RemoteException
	 */
	public abstract void doIspendMyFaithPoints(boolean doI) throws RemoteException;
	
	/**
	 * Tells the server that I want to end my turn
	 * @return can I end my turn?
	 * @throws RemoteException
	 */
	public abstract boolean endTurn() throws RemoteException;
	
	/**
	 * Tell the server I want to drop a leader card
	 * @return list of available leader cards
	 * @throws RemoteException
	 */
	public abstract List<String> dropLeaderCard() throws RemoteException;
	
	/**
	 * Tell the server which card I want to drop
	 * @param leadercard the card i want to drop
	 * @throws RemoteException
	 */
	public abstract void dropWhichLeaderCard(String leaderCard) throws RemoteException;
	
	/**
	 * Tell the server how I want to convert the council privilege
	 * @param resource into i want to convert the privilege
	 * @throws RemoteException
	 */
	public abstract void spendCouncilPrivilege(String resource) throws RemoteException;
	
	protected boolean _isRunning = false;
	protected final String _host;
	protected final int _port;
	protected Client _client;
	
}
