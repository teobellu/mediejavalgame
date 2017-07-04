package util;

import game.Player;
import server.Client;
import server.ConnectionHandler;
import server.RMIConnectionHandler;
public class FakePlayer extends Player{

	private static ConnectionHandler ch = new RMIConnectionHandler();
	private static Client fakeClient = new Client(ch, "128 bit UUID", "Nickname");;
	
	public FakePlayer(Client client, String colour) {
		super(fakeClient, colour);
		// TODO Auto-generated constructor stub
	}

}
