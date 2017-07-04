package util;

import game.Player;
import server.Client;
import server.ConnectionHandler;
import server.RMIConnectionHandler;
import server.Room;
public class FakeClient extends Client{

	
	private static ConnectionHandler ch = new RMIConnectionHandler();
	private static Client fakeClient = new Client(ch, "128 bit UUID", "Nickname");;
	

	public FakeClient(ConnectionHandler handler, String uuid, String name) {
		super(handler, uuid, name);
		/*Room room = new Room("");
		room.addPlayer(p);
		ùùù*/
	}

}
