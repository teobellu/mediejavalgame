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
	
<<<<<<< HEAD
	public final static String GREEN = "green";
	public final static String PURPLE = "purple";
	public final static String BLUE = "blue";
	public final static String YELLOW = "yellow";
	
	public static final String[] DEVELOPMENT_COLOURS = new String[]{
			GREEN,
			PURPLE,
			BLUE,
			YELLOW
	};
	
	public final static int FIRST_FLOOR = 1;
	public final static int SECOND_FLOOR = FIRST_FLOOR + 2;
	public final static int THIRD_FLOOR = SECOND_FLOOR + 2;
	public final static int FOURTH_FLOOR = THIRD_FLOOR + 2;
	
	public static final int[] FLOORS = new int[]{
		FIRST_FLOOR,
		SECOND_FLOOR,
		THIRD_FLOOR,
		FOURTH_FLOOR
	};
=======
	public static final int MAX_PLAYER = 4;
>>>>>>> b7aee1d36b848a9b43a76ea51a1e3e186c69404e
}