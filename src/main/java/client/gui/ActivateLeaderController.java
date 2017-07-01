package client.gui;

import java.rmi.RemoteException;
import java.util.List;

import exceptions.GameException;
import game.LeaderCard;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
		try {
			synchronized (this) {
				GraphicalUI.getInstance().activateLeaderCard(_choices.getValue());
			}
			_dialog.close();
		} catch (RemoteException e) {
			//TODO
		} catch (GameException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(_dialog);
			alert.setTitle("Cannot perform this operation");
			alert.setHeaderText("Cannot activate this leader card");
			alert.setContentText("You cannot do this. You don't have enough resources to activate this Leader card");
			alert.showAndWait();
		}
	}
	
	@FXML
	private void onCancelClicked(){
		_dialog.close();
	}

}
