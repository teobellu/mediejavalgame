package client.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class DropLeaderController {

	@FXML
	private ChoiceBox<String> _choiceBox;
	
	@FXML
	private Button _okButton;
	
	private Stage _dialogStage;
	
	public void setDialogStage(Stage dialog) {
		_dialogStage = dialog;
	}
	
	@FXML
	private void initialize(){
		List<String> leaders = new ArrayList<>();
		_choiceBox.getItems().addAll(leaders);
	}
	
	@FXML
	private void onOkClicked(){
		
	}
}
