package client.gui;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	private Button _fourthButton;//endturn

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
		_GUI.showDropLeaderDialog();
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
