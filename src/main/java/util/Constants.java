package util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Some constants
 * @author Jacopo
 *
 */
public class Constants {
	
	public static final String RMI = "RMI";
	public static final String SOCKET = "Socket";
	
	public static final List<String> CONNECTION_TYPES = Collections.unmodifiableList(
			Arrays.asList(SOCKET, RMI));
	
	public static final String CLI = "CLI";
	public static final  String GUI = "GUI";
	
	public static final List<String> UI_TYPES = Collections.unmodifiableList(
			Arrays.asList(CLI, GUI));
	
	public static final int MIN_PLAYER = 2;
	
	public static final int MAX_PLAYER = 4;

	public static final int LEADER_CARDS_PER_PLAYER = 4;
	
	public static final int NUMBER_OF_FAMILIARS = 4;
	
	public static final int DEFAULT_SOCKET_PORT = 2334;
	
	/**
	 * Private constructor to hide the implicit public one
	 */
	private Constants(){
		
	}
}