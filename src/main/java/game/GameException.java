package game;

/**
 * This exception is triggered when you cannot do a game move
 * because it is irregular or goes against the game rules
 */
public class GameException extends Exception {
	
	/**
	 * Default serial version ID to the selected type.
	 * Added only to make Eclipse happy
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Exception constructor
	 */
	public GameException() {
		
	}
	
	public GameException(String str) {
		super(str);
	}
}