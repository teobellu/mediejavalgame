package exceptions;

/**
 * @author Matteo
 *
 */
public class GameException extends Exception {
	
	/**
	 * Default serial version ID to the selected type.
	 * Added only to make Eclipse happy
	 */
	private static final long serialVersionUID = 1L;
	
	public GameException(String message) {
		super(message);
	}
}
