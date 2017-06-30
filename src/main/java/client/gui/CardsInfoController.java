package client.gui;

import java.io.File;
import java.util.List;

import game.GC;
import game.Player;
import game.development.DevelopmentCard;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class CardsInfoController extends DialogAbstractController{

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
	
	@FXML
	private GridPane _gridPane;
	
	private boolean _isArrowClicked = false;
	
	private Player _player;
	
	@FXML
	private void initialize(){
		_bg.setImage(new Image(new File("src/main/resources/javafx/images/custom1.jpg").toURI().toString()));
	}
	
	@FXML
	private void onArrowClicked(){
		if(_isArrowClicked){
			_arrowButton.setText(">");
			_bg.setImage(new Image(new File("src/main/resources/javafx/images/custom1.jpg").toURI().toString()));
			//TODO mostrare immagini corrette
			
			_isArrowClicked = false;
		} else {
			_arrowButton.setText("<");
			_bg.setImage(new Image(new File("src/main/resources/javafx/images/custom2.jpg").toURI().toString()));
			//TODO mostrare immagini corrette
			_isArrowClicked = true;
		}
	}
	
	@FXML
	private void onOkClicked(){
		_dialog.close();
	}

	public void setPlayer(Player me) {
		_player = me;
		
		List<DevelopmentCard> territoryCards = _player.getDevelopmentCards(GC.DEV_TERRITORY);
		List<DevelopmentCard> buildingCards = _player.getDevelopmentCards(GC.DEV_BUILDING);
		
		setCardImages(0, territoryCards);
		setCardImages(1, buildingCards);
	}
	
	private void setCardImages(int row, List<DevelopmentCard> cards){
		
		for(DevelopmentCard dc : cards){
			ImageView iv = (ImageView) getNodeFromGridPane(_gridPane, cards.indexOf(dc), row);
			iv.setImage(new Image(new File("src/main/resources/javafx/images/devel_cards/devcards_f_en_c_"+dc.getId()+".png").toURI().toString()));
		}
	}
	
	private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
	    for (Node node : gridPane.getChildren()) {
	        if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
	            return node;
	        }
	    }
	    return null;
	}
}
