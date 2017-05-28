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
	
	public void setName(String name){
		_name = name;
	}
	
	public String getName(){
		return _name;
	}
	
	private String _name;
	private final ConnectionHandler _connectionHandler;
	private final String _uuid;
	
}
