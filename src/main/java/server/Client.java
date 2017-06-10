package server;

import game.Player;

public class Client {

	public Client(ConnectionHandler handler, String uuid) {
		_connectionHandler = handler;
		_uuid = uuid;
	}

	public void setName(String name){
		_name = name;
	}
	
	public ConnectionHandler getConnectionHandler(){
		return _connectionHandler;
	}
	
	public String getUUID(){
		return _uuid;
	}
	
	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Room getRoom(){
		return _room;
	}
	
	private Room _room;
	private final ConnectionHandler _connectionHandler;
	private final String _uuid;
	private String _name;
}
