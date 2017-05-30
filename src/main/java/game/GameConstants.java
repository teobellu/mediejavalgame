package game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameConstants {
	
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
	
	public static final String FM_COLOR = "color";
	public static final String FM_BLACK = "black";
	public static final String FM_WHITE = "white";
	public static final String FM_ORANGE = "orange";
	public static final String FM_TRANSPARENT = "transparent";
	
	public static final String DEV_TERRITORY = "territory";
	public static final String DEV_BUILDING = "building";
	public static final String DEV_CHARACTER = "character";
	public static final String DEV_VENTURE = "venture";
	
	public static final List<String> DEV_TYPES = Collections.unmodifiableList(
			Arrays.asList(DEV_TERRITORY, DEV_BUILDING, DEV_CHARACTER, DEV_VENTURE));
	
	public static final Resource TAX_TOWER = new Resource(RES_COINS, 3);
	
	
	
}
