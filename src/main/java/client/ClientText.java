package client;

import java.util.ArrayList;
import java.util.List;

import game.GC;

public class ClientText {
	
	public static final String ASK_IF_CONFIG_FILE = "If you want to provide a custom configuration file, write the path to it.\n"
			+ "If left empty, default config file will be used.";
	
	public static final String ASK_SERVER_ADDRESS = "Enter the address of the server you want to connect to.";
	
	public static List<String> getTowerAndPositionsList(){
		List<String> list = new ArrayList<>();
		
		for(int i = 0;i<GC.DEV_TYPES.size();i++){
			for(int j = 0;j<GC.TOWER_HEIGHT;j++){
				StringBuilder sb = new StringBuilder(GC.DEV_TYPES.get(i).substring(0, 1).toUpperCase());
				sb.append(GC.DEV_TYPES.get(i).substring(1));
				sb.append(" - "+j);
				list.add(sb.toString());
			}
		}
		
		return list;
	}
	
	/**
	 * Private constructor to hide the implicit public one
	 */
	private ClientText(){
		
	}
	
	
}
