package client;

import client.cli.CommandLineUI;
import client.gui.GraphicalUI;
import util.Constants;

public class UIFactory {
	
	/**
	 * Private constructor to hide the implicit public one
	 */
	private UIFactory(){
		
	}

	public static UI getUserInterface(int i){
		String uiType = Constants.USER_INTERFACE_TYPES[i];
		
		switch(uiType){
			case Constants.CLI : return new CommandLineUI();
			case Constants.GUI : return GraphicalUI.getInstance();
			default : return null;
		}
	}
}
