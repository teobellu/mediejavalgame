package exceptions;

public class ConnectionException extends Exception {
	
	/**
	 * Default serial version ID to the selected type.
	 * Added only to make Eclipse happy
	 */
	private static final long serialVersionUID = 1L;

	public ConnectionException(String message) {
		super(message);
	}
}
