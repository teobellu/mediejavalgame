package client;

import client.cli.CommandLineUI;
import client.gui.GraphicalUI;
import util.Constants;

/**
 * This interface aims to create new user interface instances
 * @Factory_Method
 * @author Jacopo
 * @author Matteo
 */
public interface UIFactory {

	/**
	 * Returns an instance of the selected user interface using factory method
	 * @Factory_Method
	 * @param i User Interface chosen by the player
	 * @return New instance of selected UI
	 */
	public static UI getUserInterface(int choose){
		String uiType = Constants.UI_TYPES.get(choose);
		
		switch(uiType){
			case Constants.CLI : return CommandLineUI.getInstance();
			case Constants.GUI : return GraphicalUI.getInstance();
			default : return null;
		}
	}
}
