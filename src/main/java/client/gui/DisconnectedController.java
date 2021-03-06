package client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller for the disconnected dialog
 * @author Jacopo
 * @author Matteo
 */
public class DisconnectedController extends DialogAbstractController{

	/**
	 * Reconnect Button
	 */
	@FXML
	private Button reconnectButton;
	
	/**
	 * Called on Reconnect button clicked
	 */
	@FXML
	private void onReconnectClicked(){
		Runnable run = () -> GraphicalUI.getInstance().attemptReconnection();
		new Thread(run).start();
		
		_dialog.close();
	}
}
