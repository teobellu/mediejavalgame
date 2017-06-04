package client.gui;

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
	
	private GUI _GUI;
	
	@FXML
	private void initialize(){
		_connectionType.getItems().addAll(Constants.CONNECTION_TYPES);
		_connectionType.getSelectionModel().selectFirst();
	}
	
	public void setGUI(GUI gui){
		_GUI = gui;
	}
	
	public void onConnectPressed(){
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
		if(address.isEmpty()){
			address = "localhost";//TODO porta per connessione
		}
		
		int port = Integer.parseInt(address.split(":")[1]);
		address = address.split(":")[0];
		
		String connectionType = _connectionType.getValue();
		
		System.out.println(connectionType);
		System.out.println(address);
		System.out.println(port);
		
		GraphicalUI.getInstance().getConnection(connectionType, address,port);
		GraphicalUI.getInstance().setName(username);
	}
}
