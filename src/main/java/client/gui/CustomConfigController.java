package client.gui;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class CustomConfigController extends DialogAbstractController{

	private boolean _okClicked = false;
	
	@FXML
	private Button _okButton;
	@FXML
	private Button _cancelButton;
	
	@FXML
	private Button _fileButton;;

	public boolean isOkClicked() {
		return _okClicked;
	}
	
	@FXML
	private void handleFileButtonClicked(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML Files", "*.xml"));
		File selectedFile = fileChooser.showOpenDialog(_dialog);
		if(selectedFile!=null && selectedFile.exists()){
			_fileButton.setText(selectedFile.getAbsolutePath());
			GraphicalUI.getInstance().prepareXmlFile(selectedFile);
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(_dialog);
			alert.setTitle("Unable to open file");
			alert.setHeaderText("Cannot open the config file");
			alert.setContentText("Using the default config file...");
			GraphicalUI.getInstance().prepareXmlFile(null);
		}
	}
	
	@FXML
	private void handleCancelClicked(){
		_dialog.close();
	}
	
	@FXML
	private void handleOkClicked(){
		_okClicked = true;
        _dialog.close();
	}
}
