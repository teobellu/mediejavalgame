package util;

import game.Player;

import server.Client;
import server.ConnectionHandler;
import server.RMIConnectionHandler;

/**
 * A fake player used to tests other classes
 * @author M
 *
 */
public class FakePlayer extends Player{

	/**
	 * A default serial version ID to the selected type
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * A random connection handler, we use RMI
	 */
	private static final ConnectionHandler CH = new RMIConnectionHandler();
	
	/**
	 * A random player's client
	 */
	private static final Client FAKE_CLIENT = new Client(CH, "128 bit UUID", "Nickname");;
	
	/**
	 * Fake player constructor @Override of player protected base constructor
	 * @param client The client of the player, is not used (override Player constructor), we use
	 * the static fake client
	 * @param colour The colour of the player
	 */
	public FakePlayer(Client client, String colour) {
		super(FAKE_CLIENT, colour);
	}

}
