package util;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class Constants {
	
	public static final String RMI = "RMI";
	public static final String SOCKET = "Socket";
	
	public static final List<String> CONNECTION_TYPES = Collections.unmodifiableList(
			Arrays.asList(RMI, SOCKET));
	
	public static final String CLI = "CLI";
	public static final  String GUI = "GUI";
	
	public static final List<String> USER_INTERFACE_TYPES = Collections.unmodifiableList(
			Arrays.asList(CLI, GUI));
	
	public static final int MAX_PLAYER = 4;
	
	public static final int MAX_TURN = 3;
	
	public static final long TIMEOUT_CONNESSION_MILLIS = 30000;
	
	public static final int NUMBER_OF_FAMILIARS = 4;
	
	public static final int DEFAULT_START_ROOM_TIME_MILLIS = 20000; 
	
	/**
	 * Private constructor to hide the implicit public one
	 */
	private Constants(){
		
	}
}