package server;

import java.rmi.RemoteException;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import util.CommandStrings;

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
	
	public abstract void onConnect() throws RemoteException;
	
	public abstract String startTurn() throws RemoteException;
	
	public void sendToClient(String message){
		sendToClient(new String[]{message});
	}
	
	public void sendToClient(String[] messages){
		for(String s : messages){
			_outputQueue.add(s);
		}
		_outputQueue.add(CommandStrings.END_TRANSMISSION);
	}
	
	protected Client _client;
	protected boolean _isRunning;
	protected Deque<String> _outputQueue = new ConcurrentLinkedDeque<>();
}
