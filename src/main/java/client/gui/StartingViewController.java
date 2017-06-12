package client.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import util.Constants;

public class StartingViewController {

	@FXML
	private TextField _username;
	
	@FXML
	private TextField _address;
	
	@FXML
	private ChoiceBox<String> _connectionType;
	
	@FXML
	private Button _connectButton;
	
	@FXML
	private Button _configButton;
	
	private GUI _GUI;
		
	@FXML
	private void initialize(){
		_connectionType.getItems().addAll(Constants.CONNECTION_TYPES);
		_connectionType.getSelectionModel().selectFirst();
		
		//source: https://stackoverflow.com/questions/12744542/requestfocus-in-textfield-doesnt-work/12744775#12744775
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				_username.requestFocus();
			}
		});
	}
	
	public void setGUI(GUI gui){
		_GUI = gui;
	}
	
	@FXML
	private void onConnectPressed(){
		String username = _username.getText().trim();
		 if(username.isEmpty()){
			 Alert alert = new Alert(AlertType.WARNING);
	        	alert.initOwner(_GUI.getPrimaryStage());
	        	alert.setTitle("No username");
	        	alert.setHeaderText("No username provided");
	        	alert.setContentText("Please choose a username");
	        	alert.showAndWait();
	        	return;
		 } 
		
		String address = _address.getText().trim();
		int port;
		if(address.isEmpty()){
			address = "localhost";
			port = 1099;//TODO porta per connessione
		} else {
			port = Integer.parseInt(address.split(":")[1]);
			address = address.split(":")[0];
		}
		
		String connectionType = _connectionType.getValue();
		
		GraphicalUI.getInstance().setConnection(connectionType, address,port);
		GraphicalUI.getInstance().setName(username);
		
		_GUI.setMainScene();
	}
	
	@FXML
	private void onConfigPressed(){
		boolean okClicked = _GUI.showConfigDialog();
		
		if(okClicked){
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(_GUI.getPrimaryStage());
			alert.setTitle("File OK!");
			alert.setHeaderText("File OK!");
			alert.setContentText("File OK!");
			alert.showAndWait();
		}
	}
}
