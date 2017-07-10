package controller;

import model.Player;
import server.Client;

/**
 * A fake player used to tests other classes
 *
 */
public class FakePlayer extends Player{

	/**
	 * A default serial version ID to the selected type
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Fake player constructor @Override of player protected base constructor
	 * @param client The client of the player, is not used (override Player constructor), we use
	 * the static fake client
	 * @param colour The colour of the player
	 */
	public FakePlayer(Client client, String colour) {
		super(new FakeClient(null, null, null), colour);
	}

}
