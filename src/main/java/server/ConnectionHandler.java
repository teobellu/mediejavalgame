package server;

import java.rmi.RemoteException;

public abstract class ConnectionHandler implements Runnable {
	public void setClient(Client client){
		_client = client;
	}
	
	public boolean isRunning(){
		return _isRunning;
	}
	
	public void shutdown(){
		_isRunning = false;
	}
	
	public String getConfigFile(){
		if(_configFile==null){
			return "";
		} else {
			return _configFile;
		}
	}
	
	public abstract void onConnect() throws RemoteException;
	
	public abstract String startTurn() throws RemoteException;
	
	public abstract void sendToClient(String message);
	
	public abstract void sendToClient(String[] messages);
	
	protected Client _client;
	protected volatile boolean _isRunning;
	
	protected String _configFile = null;
}
