package model.exceptions;

/**
 * Exception of the game, like you can't pay or it's not your turn
 * @author Matteo
 * @author Jacopo
 *
 */
public class GameException extends Exception {
	
	/**
	 * Default serial version ID to the selected type.
	 * Added only to make Eclipse happy
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Game exception constructor with a message to use
	 * @param message
	 */
	public GameException(String message) {
		super(message);
	}
}
