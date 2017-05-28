package game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameContants {
	
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
	
	
}
