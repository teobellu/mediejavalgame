package client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class DisconnectedController extends DialogAbstractController{

	@FXML
	private Button _reconnectButton;
	
	@FXML
	private void onReconnectClicked(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				GraphicalUI.getInstance().attemptReconnection();
			}
		}).start();
		
		_dialog.close();
	}
}
