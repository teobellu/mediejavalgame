package game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GC {
	
	public static final String DEFAULT = "default";
	public static final String LEADER_CARD = "leader card";
	public static final String ACTION_SPACE = "action space";
	
	public static final String IMMEDIATE = "immediate";
	public static final String ON_CALL = "on call";
	public static final String ONCE_PER_TURN = "opt";
	public static final String WHEN_END = "when end";
	public static final String WHEN_FIND_VALUE_ACTION = "when find value action";
	public static final String WHEN_GAIN = "when gain";
	public static final String WHEN_INCREASE_WORKER = "when increase worker";
	public static final String WHEN_JOINING_SPACE = "when joining space";
	public static final String WHEN_PAY_TAX_TOWER = "when pay tax tower";
	public static final String WHEN_PLACE_FAMILIAR_MARKET = "when place familiar market";
	public static final String WHEN_SET_FAMILIAR_START_POWER = "when set familiar start power";
	public static final String WHEN_FIND_COST_CARD = "when find cost card";
	public static final String WHEN_PAY_REQUIREMENT = "when pay requirement";
	public static final String WHEN_GET_TOWER_BONUS = "when get tower bonus";
	public static final String WHEN_SHOW_SUPPORT = "when show support";
	public static final String WHEN_ROLL = "when roll";
	
	public static final String RES_COINS = "coins";
	public static final String RES_WOOD = "wood";
	public static final String RES_STONES = "stones";
	public static final String RES_SERVANTS = "servants";
	public static final String RES_COUNCIL = "council";
	public static final String RES_VICTORYPOINTS = "victorypoints";
	public static final String RES_MILITARYPOINTS = "militarypoints";
	public static final String RES_FAITHPOINTS = "faithpoints";
	
	public static final List<String> RES_TYPES = Collections.unmodifiableList(
			Arrays.asList(RES_COINS, RES_WOOD, RES_STONES, RES_SERVANTS, RES_COUNCIL, 
					RES_VICTORYPOINTS, RES_MILITARYPOINTS, RES_FAITHPOINTS));
	
	public static final List<Integer> REQ_TERRITORY = Collections.unmodifiableList(
			Arrays.asList(0, 0, 3, 7, 12, 18));
	
	public static final List<Integer> REW_TERRITORY = Collections.unmodifiableList(
			Arrays.asList(0, 0, 1, 4, 10, 20));
	
	public static final List<Integer> REW_CHARACTER = Collections.unmodifiableList(
			Arrays.asList(1, 3, 6, 10, 15, 21));
	
	public static final String FM_COLOR = "color";
	public static final String FM_BLACK = "black";
	public static final String FM_WHITE = "white";
	public static final String FM_ORANGE = "orange";
	public static final String FM_TRANSPARENT = "transparent";
	
	public static final String HARVEST = "harvest";
	public static final String PRODUCTION = "production";
	
	public static final String DEV_TERRITORY = "territory";
	public static final String DEV_BUILDING = "building";
	public static final String DEV_CHARACTER = "character";
	public static final String DEV_VENTURE = "venture";
	
	public static final List<String> DEV_TYPES = Collections.unmodifiableList(
			Arrays.asList(DEV_TERRITORY, DEV_BUILDING, DEV_CHARACTER, DEV_VENTURE));
	
	public static final Resource TAX_TOWER = new Resource(RES_COINS, 3);
	public static final Resource FIRST_PRIZE_MILITARY = new Resource(RES_VICTORYPOINTS, 5);
	public static final Resource SECOND_PRIZE_MILITARY = new Resource(RES_VICTORYPOINTS, 2);
	
	public static final Integer MAX_DEVELOPMENT_CARDS = 6;
	
	public static final Integer END_REWARD_RESOURCE = 5;

	/**
	 * Private constructor to hide the implicit public one
	 */
	private GC(){
		
	}
	
}