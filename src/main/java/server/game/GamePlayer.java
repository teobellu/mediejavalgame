package server.game;

import model.Player;
import server.Client;

/**
 * A class that allows Game to use player's constructor
 */
public class GamePlayer extends Player{

	/**
	 * A default serial version ID to the selected type
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Super constructor
	 * @param client
	 * @param colour
	 */
	protected GamePlayer(Client client, String colour) {
		super(client, colour);
	}
}
