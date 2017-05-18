package server;

import util.Packet;

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
	
	public abstract void sendToClient(Packet packet);
	
	public abstract Packet getFromClient() throws Exception;
	
	protected Client _client;
	protected boolean _isRunning;
}
