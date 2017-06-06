package client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class MainViewController {

	private GUI _GUI;
	
	@FXML
	private Button _firstButton;
	@FXML
	private Button _secondButton;
	@FXML
	private Button _thirdButton;
	@FXML
	private Button _fourthButton;

	@FXML
	private AnchorPane _buttonPane;
	
	@FXML
	private void initialize(){		
		
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
	}
	
	@FXML
	private void onFourthButtonClicked(){
		
	}
}
