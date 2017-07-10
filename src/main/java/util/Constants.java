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
	
	/**
	 * RMI connection
	 */
	public static final String RMI = "RMI";
	/**
	 * Socket connection
	 */
	public static final String SOCKET = "Socket";
	
	/**
	 * RMI or Socket
	 */
	public static final List<String> CONNECTION_TYPES = Collections.unmodifiableList(
			Arrays.asList(RMI, SOCKET));
	
	/**
	 * Command line user interface
	 */
	public static final String CLI = "CLI";
	/**
	 * Graphical user interface
	 */
	public static final  String GUI = "GUI";
	
	/**
	 * Command line user interface or Graphical user interface
	 */
	public static final List<String> UI_TYPES = Collections.unmodifiableList(
			Arrays.asList(CLI, GUI));
	
	/**
	 * Min player in a game
	 */
	public static final int MIN_PLAYER = 2;
	
	/**
	 * Max player in a game
	 */
	public static final int MAX_PLAYER = 4;

	/**
	 * Maximum leader cards per player
	 */
	public static final int LEADER_CARDS_PER_PLAYER = 4;
	
	/**
	 * Number of familiars per player
	 */
	public static final int NUMBER_OF_FAMILIARS = 4;
	
	/**
	 * Default port for socket connection
	 */
	public static final int DEFAULT_SOCKET_PORT = 2334;
	
	/**
	 * Private constructor to hide the implicit public one
	 */
	private Constants(){
		
	}
}