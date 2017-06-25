package client.cli;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class CommandConstants {
	
	public static final String PLACE_FAMILIAR = "place familiar";
	public static final String ACTIVATE_LEADER = "activate leader card";
	public static final String DROP_LEADER = "drop leader card";
	public static final String SHOW_MY_CARDS = "show my cards";
	public static final String END_TURN = "end turn";
	
	public static final List<String> STANDARD_COMMANDS = Collections.unmodifiableList(
			Arrays.asList(PLACE_FAMILIAR, ACTIVATE_LEADER, DROP_LEADER, SHOW_MY_CARDS, END_TURN));
	
	private CommandConstants(){
		
	}
	
}