package client.gui;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.GameException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class DropLeaderController extends DialogAbstractController{

	@FXML
	private ChoiceBox<String> _choiceBox;
	
	@FXML
	private Button _okButton;
	@FXML
	private Button _cancelButton;
	
	private List<String> _leaderList;
	
	public void setLeaderList(List<String> leaders){
		_leaderList = leaders;
	}
	
	@FXML
	private void initialize(){
		_choiceBox.getItems().addAll(_leaderList);
	}
	
	@FXML
	private void onOkClicked(){
		try {
			GraphicalUI.getInstance().dropLeaderCard(_choiceBox.getValue());
			_dialog.close();
		} catch (RemoteException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		} catch (GameException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(_dialog);
			alert.setTitle("Cannot perform this operation");
			alert.setHeaderText("Cannot drop this leader card");
			alert.setContentText("You cannot do this. You don't have this leader card");
			alert.showAndWait();
		}
	}
	
	@FXML
	private void onCancelClicked(){
		_dialog.close();
	}
	
	private Logger _log = Logger.getLogger(DropLeaderController.class.getName());
}
