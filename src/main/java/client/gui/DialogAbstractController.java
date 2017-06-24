package client.gui;

import javafx.stage.Stage;

public abstract class DialogAbstractController {
	protected Stage _dialog;
	
	public void setDialog(Stage dialog){
		_dialog = dialog;
	}
}
