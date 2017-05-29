package client.network;

import java.rmi.RemoteException;

import client.Client;

public abstract class ConnectionServerHandler implements Runnable {

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
	
	public abstract void putFamiliar() throws RemoteException;

	public abstract void sendConfigFile() throws RemoteException;
	
	public abstract void activateLeaderCard() throws RemoteException;
	
	public abstract void ping() throws RemoteException;
	
	public abstract void onConnect() throws RemoteException;
	
	public abstract boolean addMeToGame() throws RemoteException;
	
	protected boolean _isRunning = false;
	protected final String _host;
	protected final int _port;
	protected Client _client;
}
