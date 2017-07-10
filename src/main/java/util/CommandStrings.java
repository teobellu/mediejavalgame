package util;

/**
 * Commands for communication requiring processing of some kind
 *
 */
public final class CommandStrings {
	
	/**
	 * Client unique id
	 */
	public static final String UUID = "uuid";
	
	/**
	 * Reconnect me
	 */
	public static final String RECONNECT = "reconnect";
	
	/**
	 * Send gameboard
	 */
	public static final String GAME_BOARD = "gameboard";
	/**
	 * Send player
	 */
	public static final String PLAYER = "player";
	
	/**
	 * Add me to a game
	 */
	public static final String ADD_TO_GAME = "add_to_game";
	/**
	 * Sen a config file
	 */
	public static final String ASK_FOR_CONFIG = "ask_config";
	
	/**
	 * Choose to convert resources
	 */
	public static final String CHOOSE_CONVERT = "choose_convert";
	/**
	 * Choose a familiar
	 */
	public static final String CHOOSE_FAMILIAR = "choose_familiar";
	
	/**
	 * Choose initial leader
	 */
	public static final String INITIAL_LEADER = "initial_leader";
	/**
	 * Choose a leader
	 */
	public static final String CHOOSE_LEADER = "choose_leader";
	
	/**
	 * Start turn
	 */
	public static final String START_TURN = "start_turn";
	
	/**
	 * Activate a leader card
	 */
	public static final String ACTIVATE_LEADER_CARD = "activate_leader_card";
	
	/**
	 * Drop a leader card
	 */
	public static final String DROP_LEADER_CARD = "drop_leader_card";
	
	/**
	 * Place a familiar
	 */
	public static final String PLACE_FAMILIAR = "put_familiar";
	
	/**
	 * End turn
	 */
	public static final String END_TURN = "end_turn";
	
	/**
	 * Convert a council privilege
	 */
	public static final String HANDLE_COUNCIL = "handle_council";
	
	/**
	 * Connection error
	 */
	public static final String CONNECTION_ERROR = "connection_error";
	
	/**
	 * Choose initial personal bonus
	 */
	public static final String INITIAL_PERSONAL_BONUS = "initial_personal_bonus";
	
	/**
	 * Ask an integere
	 */
	public static final String ASK_INT = "ask_int";
	/**
	 * Ask a yes/no question
	 */
	public static final String ASK_BOOLEAN = "ask_boolean";
	
	/**
	 * Send an info
	 */
	public static final String INFO = "info";
	/**
	 * Send an info with board update
	 */
	public static final String INFO_BOARD = "info_board";
	/**
	 * Send an info with player update
	 */
	public static final String INFO_PLAYER = "info_player";
	/**
	 * Send an info with board and player update
	 */
	public static final String INFO_BOARD_PLAYER = "info_board_player";
	
	/**
	 * Choose between two or more costs
	 */
	public static final String CHOOSE_COST = "choose_cost";
	
	/**
	 * Show your support to the Vatican
	 */
	public static final String SHOW_VATICAN_SUPPORT = "show_vatican_support";
	/**
	 * Activate a Once Per Turn leader card
	 */
	public static final String ACTIVATE_OPT_LEADERS = "activate_opt_leaders";
	
	/**
	 * Hide constructor
	 */
	private CommandStrings(){
	}
}
