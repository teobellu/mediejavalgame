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
	
	public abstract void sendName(String name) throws RemoteException;
	
	public abstract List<String> putFamiliar() throws RemoteException;
	
	public abstract List<String> putFamiliarWhich(String familiar) throws RemoteException;
	
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
		
	public abstract void ping() throws RemoteException;
	
	public abstract void onConnect() throws RemoteException;
	
	/**
	 * Add client to a game
	 * @return true if first player of a room, false otherwise
	 * @throws RemoteException
	 */
	public abstract boolean addMeToGame() throws RemoteException;
	
	protected boolean _isRunning = false;
	protected final String _host;
	protected final int _port;
	protected Client _client;
	
}
