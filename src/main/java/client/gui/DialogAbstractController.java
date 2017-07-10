package client.gui;

import javafx.stage.Stage;

/**
 * Base implementation of a base dialog
 * @author Jacopo
 *
 */
public abstract class DialogAbstractController {
	/**
	 * The dialog currently showed
	 */
	protected Stage _dialog;
	
	/**
	 * Set the dialog, to close it
	 * @param dialog the dialog
	 */
	public void setDialog(Stage dialog){
		_dialog = dialog;
	}
}
