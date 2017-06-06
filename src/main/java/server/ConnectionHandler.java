package server;

import java.rmi.RemoteException;

public abstract class ConnectionHandler {
	public void setClient(Client client){
		_client = client;
	}
	
	public boolean isRunning(){
		return _isRunning;
	}
	
	public void shutdown(){
		_isRunning = false;
	}
	
	public abstract void onConnect() throws RemoteException;
	
	protected Client _client;
	protected boolean _isRunning;
	
	public abstract String startTurn() throws RemoteException;
}
