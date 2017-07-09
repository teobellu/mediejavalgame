package client.gui;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.GameException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Constants;

/**
 * Controller for starting view scene
 * @author Jacopo
 *
 */
public class StartingViewController {

	@FXML
	private ImageView _backgroundImage;
	
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
		
	private final Logger _log = Logger.getLogger(StartingViewController.class.getName());
	
	/**
	 * FXML method
	 */
	@FXML
	private void initialize(){
		_connectionType.getItems().addAll(Constants.CONNECTION_TYPES);
		_connectionType.getSelectionModel().selectFirst();
		
		File file = new File("src/main/resources/javafx/images/punchboard_b_c_04.jpg");
		Image bg = new Image(file.toURI().toString());
		
		_backgroundImage.setImage(bg);
		
		//source: https://stackoverflow.com/questions/12744542/requestfocus-in-textfield-doesnt-work/12744775#12744775
		Platform.runLater( () -> _username.requestFocus() );
	}
	
	/**
	 * Called on Config button pressed
	 */
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
	
	/**
	 * Called on Connect Button pressed
	 */
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
			port = 0;
		} else {
			
			try {
				port = Integer.parseInt(address.split(":")[1]);
				address = address.split(":")[0];
			} catch (NumberFormatException e) {
				Alert alert = new Alert(AlertType.WARNING);
	        	alert.initOwner(_GUI.getPrimaryStage());
	        	alert.setTitle("Invalid Address");
	        	alert.setHeaderText("Cannot parse address");
	        	alert.setContentText("Please write an address with the following pattern: 'address:port'");
	        	alert.showAndWait();
	        	return;
			}
		}
		
		String connectionType = _connectionType.getValue();
		
		GraphicalUI.getInstance().setConnection(connectionType, address,port);
		GraphicalUI.getInstance().setPlayerName(username);
		try {
			GraphicalUI.getInstance().addMeToGame(username);
		} catch (GameException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			Alert alert = new Alert(AlertType.ERROR);
        	alert.initOwner(_GUI.getPrimaryStage());
        	alert.setTitle("Name already taken");
        	alert.setHeaderText("Cannot join game");
        	alert.setContentText("This name is already taken. Try something different");
        	alert.showAndWait();
			return;
		}
		
		_GUI.setMainScene();
	}
	
	/**
	 * Set the GUI
	 * @param gui the gui
	 */
	public void setGUI(GUI gui){
		_GUI = gui;
	}
}
