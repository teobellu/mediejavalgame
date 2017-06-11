package exceptions;

public class ControllerException extends Exception {
	
	/**
	 * Default serial version ID to the selected type.
	 * Added only to make Eclipse happy
	 */
	private static final long serialVersionUID = 1L;

	public ControllerException(String message) {
		super(message);
	}
}
