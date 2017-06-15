package server;

import java.rmi.RemoteException;
import java.util.List;

import game.Game;

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
	
	public void setGame(){//TODO da chiamare questo metodo alla creazione del gioco(probabilmente nella room)
		if(_client!=null){
			_theGame = _client.getRoom().getGame();
		}
	}
	
	public abstract void onConnect() throws RemoteException;
	
	public abstract String startTurn() throws RemoteException;
	
	public abstract void sendToClient(String message);
	
	public abstract void sendToClient(List<String> messages);
	
	protected Client _client;
	protected volatile boolean _isRunning;
	
	protected String _configFile = null;
	
	protected transient Game _theGame = null;
}
