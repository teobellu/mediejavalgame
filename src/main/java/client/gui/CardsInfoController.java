package client.gui;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CardsInfoController {

	@FXML
	private Button _arrowButton;
	
	@FXML
	private ImageView _card00;
	@FXML
	private ImageView _card01;
	@FXML
	private ImageView _card02;
	@FXML
	private ImageView _card03;
	@FXML
	private ImageView _card04;
	@FXML
	private ImageView _card05;
	
	@FXML
	private ImageView _card10;
	@FXML
	private ImageView _card11;
	@FXML
	private ImageView _card12;
	@FXML
	private ImageView _card13;
	@FXML
	private ImageView _card14;
	@FXML
	private ImageView _card15;
	
	@FXML
	private ImageView _bg;
	
	private boolean _isArrowClicked = false;
	
	@FXML
	private void onArrowClicked(){
		if(_isArrowClicked){
			_arrowButton.setText(">");
			_bg.setImage(new Image(new File("src/main/resources/javafx/images/custom1.jpeg").toURI().toString()));
			//TODO mostrare immagini corrette
		} else {
			_arrowButton.setText("<");
			_bg.setImage(new Image(new File("src/main/resources/javafx/images/custom2.jpeg").toURI().toString()));
			//TODO mostrare immagini corrette
		}
	}
}
