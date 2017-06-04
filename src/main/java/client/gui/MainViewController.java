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
		double buttonsDistance = _buttonPane.getPrefHeight()/4;
		
		_firstButton.layoutYProperty().set(buttonsDistance);
		
		_secondButton.layoutYProperty().set(2*buttonsDistance);
		
		_thirdButton.layoutYProperty().set(3*buttonsDistance);
		
		_fourthButton.layoutYProperty().set(4*buttonsDistance);
		
		System.out.println(buttonsDistance);
		
		System.out.println(_firstButton.layoutYProperty().doubleValue());
		System.out.println(_secondButton.layoutYProperty().doubleValue());
		System.out.println(_thirdButton.layoutYProperty().doubleValue());
		System.out.println(_fourthButton.layoutYProperty().doubleValue());
	}
	
	public void setGUI(GUI gui){
		_GUI = gui;
	}
}
