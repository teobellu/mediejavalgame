package server;

/**
 * Gimmicky client on the server
 *
 */
public class Client {
	
	private Room _room = null;
	private ConnectionHandler _connectionHandler;
	private final String _uuid;
	private final String _name;

	/**
	 * Constructor
	 * @param handler connection handler
	 * @param uuid unique id of the client
	 * @param name player name
	 */
	public Client(ConnectionHandler handler, String uuid, String name) {
		_connectionHandler = handler;
		_uuid = uuid;
		_name = name;
	}
	
	/**
	 * return the connection handler
	 * @return the connection handler
	 */
	public ConnectionHandler getConnectionHandler(){
		return _connectionHandler;
	}
	
	/**
	 * set the connection handler
	 * @param handler the connection handler
	 */
	public void setConnectionHandler(ConnectionHandler handler){
		_connectionHandler = handler;
	}
	
	/**
	 * return the uuid of the player
	 * @return the uuid
	 */
	public String getUUID(){
		return _uuid;
	}
	
	/**
	 * get the player's name
	 * @return the name
	 */
	public String getName(){
		return _name;
	}
	
	/**
	 * Am I ready?
	 * @return yes or no
	 */
	public boolean isReady() {
		if(_room!= null && _connectionHandler!=null){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Get the room associated to this client
	 * @return the room
	 */
	public Room getRoom(){
		return _room;
	}
	
	/**
	 * set a room associated
	 * @param room the room
	 */
	public void setRoom(Room room){
		if(_room==null){
			_room = room;
		}
	}
	
	
}
