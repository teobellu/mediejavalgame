package client.gui;

import java.io.File;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class MainViewController {

	private GUI _GUI;
	
	@FXML
	private ImageView _backgroundImage;
	
	@FXML
	private ImageView _leaderCard1;
	@FXML
	private ImageView _leaderCard2;
	@FXML
	private ImageView _leaderCard3;
	@FXML
	private ImageView _leaderCard4;
	@FXML
	private ImageView _leaderCard5;
	@FXML
	private ImageView _leaderDeck;
	
	@FXML
	private Button _firstButton;
	@FXML
	private Button _secondButton;
	@FXML
	private Button _thirdButton;
	@FXML
	private Button _fourthButton;//endturn

	@FXML
	private AnchorPane _buttonPane;
	
	@FXML
	private void initialize(){		
		File file = new File("src/main/resources/javafx/images/gameboard_f_c.jpeg");
		Image bg = new Image(file.toURI().toString());
		
		_backgroundImage.setImage(bg);
	}
	
	public void setGUI(GUI gui){
		_GUI = gui;
	}
	
	@FXML
	private void onFirstButtonClicked(){
		
	}
	
	@FXML
	private void onSecondButtonClicked(){
		
	}
	
	@FXML
	private void onThirdButtonClicked(){
		_GUI.showDropLeaderDialog(
				GraphicalUI.getInstance().dropLeaderCard());
	}
	
	@FXML
	private void onFourthButtonClicked(){
		try {
			if(GraphicalUI.getInstance().getConnection().endTurn()){
				//TODO finisci il turno?
			}
		} catch (RemoteException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
	}
	
	private Logger _log = Logger.getLogger(MainViewController.class.getName());
}
