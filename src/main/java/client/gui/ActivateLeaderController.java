package client.gui;

import java.rmi.RemoteException;
import java.util.List;

import game.LeaderCard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class ActivateLeaderController extends DialogAbstractController {

	@FXML
	private Button _okButton;
	@FXML
	private Button _cancelButton;
	
	@FXML
	private ChoiceBox<String> _choices;
	
	public void setLeaders(List<LeaderCard> leaderCards) {
		for(LeaderCard lc : leaderCards){
			_choices.getItems().add(lc.getName());		
		}
	}
	
	@FXML
	private void onOkClicked(){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					GraphicalUI.getInstance().activateLeaderCard(_choices.getValue());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		_dialog.close();
	}
	
	@FXML
	private void onCancelClicked(){
		_dialog.close();
	}

}
