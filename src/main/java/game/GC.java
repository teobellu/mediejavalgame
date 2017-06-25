package game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import game.effect.Effect;
import game.effect.behaviors.EffectDoNothing;

public class GC{
	
	public static final String DEFAULT = "default";
	public static final String LEADER_CARD = "leader card";
	public static final String ACTION_SPACE = "action space";
	
	public static final String NEVER = "never";
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
	public static final String RES_COUNCIL = "councils";
	public static final String RES_VICTORYPOINTS = "victory_points";
	public static final String RES_MILITARYPOINTS = "military_points";
	public static final String RES_FAITHPOINTS = "faith_points";
	
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
	
	public static final List<String> FM_TYPE = Collections.unmodifiableList(
			Arrays.asList(FM_BLACK, FM_WHITE, FM_ORANGE, FM_TRANSPARENT));
	
	public static final String MARKET = "market";
	public static final String HARVEST = "harvest";
	public static final String PRODUCTION = "production";
	public static final String COUNCIL_PALACE = "council_palace";
	public static final String TOWER = "tower";
	
	public static final List<String> SPACE_TYPE = Collections.unmodifiableList(
			Arrays.asList(MARKET, HARVEST, PRODUCTION, COUNCIL_PALACE, TOWER));
	
	public static final String DEV_TERRITORY = "territory";
	public static final String DEV_BUILDING = "building";
	public static final String DEV_CHARACTER = "character";
	public static final String DEV_VENTURE = "venture";
	
	public static final List<String> DEV_TYPES = Collections.unmodifiableList(
			Arrays.asList(DEV_TERRITORY, DEV_CHARACTER, DEV_BUILDING, DEV_VENTURE));
	
	public static final Resource TAX_TOWER = new Resource(RES_COINS, 3);
	public static final Resource FIRST_PRIZE_MILITARY = new Resource(RES_VICTORYPOINTS, 5);
	public static final Resource SECOND_PRIZE_MILITARY = new Resource(RES_VICTORYPOINTS, 2);
	
	public static final Integer MAX_DEVELOPMENT_CARDS = 6;
	
	public static final Integer END_REWARD_RESOURCE = 5;

	public static final Effect NIX = new Effect(NEVER, new EffectDoNothing());
	
	public static final Resource COUNCIL_REW0 = new Resource(RES_WOOD, 1);
	public static final Resource COUNCIL_REW1 = new Resource(COUNCIL_REW0, RES_STONES, 1);
	public static final Resource COUNCIL_REW2 = new Resource(RES_SERVANTS, 2);
	public static final Resource COUNCIL_REW3 = new Resource(RES_COINS, 2);
	public static final Resource COUNCIL_REW4 = new Resource(RES_MILITARYPOINTS, 2);
	public static final Resource COUNCIL_REW5 = new Resource(RES_FAITHPOINTS, 1);
	
	public static final List<Resource> COUNCIL_REWARDS = Collections.unmodifiableList(
		Arrays.asList(COUNCIL_REW1, COUNCIL_REW2, COUNCIL_REW3, COUNCIL_REW4, COUNCIL_REW5));
	
	/**
	 * Private constructor to hide the implicit public one
	 */
	private GC(){
		
	}
}
