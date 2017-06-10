package util;

public class Constants {
	
	/**
	 * Private constructor to hide the implicit public one
	 */
	private Constants(){
		
	}
	
	
	public static final String RMI = "RMI";
	public static final String SOCKET = "Socket";
	
	protected static final String[] CONNECTION_TYPES = new String[]{
		RMI,
		SOCKET
	};
	
	public static final String CLI = "CLI";
	public static final  String GUI = "GUI";
	
	protected static final String[] USER_INTERFACE_TYPES = new String[]{
			CLI,
			GUI
	};
	
	public static final int MAX_PLAYER = 4;
	
	public static final int MAX_TURN = 3;
	
	public static final long TIMEOUT_CONNESSION_MILLIS = 30000;
	
	public static final int NUMBER_OF_FAMILIARS = 4;
	
	public static final int DEFAULT_START_ROOM_TIME_MILLIS = 20000; 
}