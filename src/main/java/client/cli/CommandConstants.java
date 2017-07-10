package client.cli;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Constants of commands only for cli
 *
 */
public abstract class CommandConstants {
	
	public static final String PRINT_BOARD = "print game board";
	public static final String PLACE_FAMILIAR = "place familiar";
	public static final String ACTIVATE_LEADER = "activate leader card";
	public static final String DROP_LEADER = "drop leader card";
	public static final String SHOW_MY_CARDS = "show my cards";
	public static final String SHOW_SUPPORT = "show vatican support";
	public static final String PLAY_OPT_LEADERS = "play OPT leader cards";
	public static final String END_TURN = "end turn";
	
	public static final List<String> STANDARD_COMMANDS = Collections.unmodifiableList(
			Arrays.asList(
					PRINT_BOARD,
					PLACE_FAMILIAR, 
					ACTIVATE_LEADER, 
					DROP_LEADER, 
					SHOW_MY_CARDS, 
					SHOW_SUPPORT,
					PLAY_OPT_LEADERS,
					END_TURN
			)
	);
	
	
	/**
	 * Hide private constructor
	 */
	private CommandConstants(){
		
	}
	
}
