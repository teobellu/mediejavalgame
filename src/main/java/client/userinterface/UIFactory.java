package client.userinterface;

import util.Constants;

public class UIFactory {

	public static UI getUserInterface(int i){
		String uiType = Constants.USER_INTERFACE_TYPES[i];
		
		if(uiType == Constants.CLI){
			return new CommandLineUI();
		} else if(uiType == Constants.GUI){
			return new GraphicalUI();
		}
		
		return null;
	}
}
