package util;

public class Constants {
	public static final String RMI = "RMI";
	public static final String SOCKET = "Socket";
	
	public static final String[] CONNECTION_TYPES = new String[]{
		RMI,
		SOCKET
	};
	
	public final static String CLI = "CLI";
	public final static String GUI = "GUI";
	
	public static final String[] USER_INTERFACE_TYPES = new String[]{
			CLI,
			GUI
	};
	
	public static final int MAX_PLAYER = 4;
}