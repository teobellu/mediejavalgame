package server;

import java.util.ArrayList;
import java.util.List;

import util.CommandStrings;

public class Client {

	public Client(ConnectionHandler handler, String uuid) {
		_connectionHandler = handler;
		_uuid = uuid;
		//manda al client il suo id per la riconnessione
		List<String> list = new ArrayList<>();
		list.add(CommandStrings.UUID);
		list.add(_uuid);
		_connectionHandler.sendToClient(list);
	}
	
	public ConnectionHandler getConnectionHandler(){
		return _connectionHandler;
	}
	
	public String getUUID(){
		return _uuid;
	}
	
	public boolean isReady() {
		if(_room!= null && _connectionHandler!=null){
			return true;
		} else {
			return false;
		}
	}
	
	public Room getRoom(){
		return _room;
	}
	
	public void setRoom(Room room){
		if(_room==null){
			_room = room;
		}
	}
	
	private Room _room = null;
	private final ConnectionHandler _connectionHandler;
	private final String _uuid;
}
