package server;

public class Client {

	public Client(ConnectionHandler handler, String uuid) {
		_connectionHandler = handler;
		_uuid = uuid;
		//TODO manda al client il suo id per la riconnessione
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
