package client.gui;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class CustomConfigController {

	private boolean _okClicked = false;
	
	@FXML
	private Button _okButton;
	@FXML
	private Button _cancelButton;
	
	@FXML
	private TextField _textField;
	
	private Stage _dialogStage;
	
	public void setDialogStage(Stage dialog) {
		_dialogStage = dialog;
	}

	public boolean isOkClicked() {
		return _okClicked;
	}
	
	@FXML
	private void handleTextFieldClicked(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML Files", "*.xml"));
		File selectedFile = fileChooser.showOpenDialog(_dialogStage);
		if(selectedFile.exists()){
			_textField.setText(selectedFile.getAbsolutePath());
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(_dialogStage);
			alert.setTitle("File Not Found");
			alert.setHeaderText("No File Selected");
			alert.setContentText("Cannot open the config file");
		}
	}
	
	@FXML
	private void handleCancelClicked(){
		_dialogStage.close();
	}
	
	@FXML
	private void handleOkClicked(){
		_okClicked = true;
        _dialogStage.close();
	}
}
