package server;

import util.packets.Packet;

public class Client {

	public Client(ConnectionHandler handler, String uuid) {
		_connectionHandler = handler;
		_uuid = uuid;
	}
	
	public ConnectionHandler getConnectionHandler(){
		return _connectionHandler;
	}
	
	public String getUUID(){
		return _uuid;
	}
	
	public void processMessage(Packet message){
		
	}
	
	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private final ConnectionHandler _connectionHandler;
	private final String _uuid;
	
}
