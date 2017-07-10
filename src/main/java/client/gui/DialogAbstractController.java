package client.gui;

import javafx.stage.Stage;

/**
 * Base implementation of a base dialog
 *
 */
public abstract class DialogAbstractController {
	protected Stage _dialog;
	
	/**
	 * Set the dialog, to close it
	 * @param dialog the dialog
	 */
	public void setDialog(Stage dialog){
		_dialog = dialog;
	}
}
