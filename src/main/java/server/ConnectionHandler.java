package server;

import java.rmi.RemoteException;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

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
	
	public abstract String startTurn() throws RemoteException;
	
	public void sendToClient(String message){
		
	}
	
	protected Client _client;
	protected boolean _isRunning;
	protected Deque<String> _outputQueue = new ConcurrentLinkedDeque<>();
}
