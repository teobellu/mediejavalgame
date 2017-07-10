package client.gui;

import java.io.File;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import model.DevelopmentCard;
import model.GC;
import model.Player;

/**
 * Controller for the cards info dialog
 *
 */
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
	
	private final int CHAR_TERRITORY_ROW = 1;
	
	private final int BUILDINGS_VENTURE_ROW = 0;
	
	/**
	 * FXML method
	 */
	@FXML
	private void initialize(){
		_bg.setImage(new Image(new File("src/main/resources/javafx/images/custom1.jpg").toURI().toString()));
	}

	/**
	 * Called on Arrow button clicked
	 */
	@FXML
	private void onArrowClicked(){
		for(Node n : _gridPane.getChildren()){
			if(n instanceof ImageView){
				((ImageView)n).setImage(null);
			}
		}
		
		if(_isArrowClicked){
			_arrowButton.setText(">");
			_bg.setImage(new Image(new File("src/main/resources/javafx/images/custom1.jpg").toURI().toString()));
			
			List<DevelopmentCard> territoryCards = _player.getDevelopmentCards(GC.DEV_TERRITORY);
			List<DevelopmentCard> buildingCards = _player.getDevelopmentCards(GC.DEV_BUILDING);
			
			setCardImages(CHAR_TERRITORY_ROW, territoryCards);
			setCardImages(BUILDINGS_VENTURE_ROW, buildingCards);
			
			_isArrowClicked = false;
		} else {
			_arrowButton.setText("<");
			_bg.setImage(new Image(new File("src/main/resources/javafx/images/custom2.jpg").toURI().toString()));
			
			List<DevelopmentCard> characterCards = _player.getDevelopmentCards(GC.DEV_CHARACTER);
			List<DevelopmentCard> achievementCards = _player.getDevelopmentCards(GC.DEV_VENTURE);
			
			setCardImages(CHAR_TERRITORY_ROW, characterCards);
			setCardImages(BUILDINGS_VENTURE_ROW, achievementCards);
			
			_isArrowClicked = true;
		}
	}
	
	/**
	 * Called on OK button clicked
	 */
	@FXML
	private void onOkClicked(){
		_dialog.close();
	}
	
	/**
	 * Set background images
	 * @param row the row
	 * @param cards the cards
	 */
	private void setCardImages(int row, List<DevelopmentCard> cards){
		
		for(DevelopmentCard dc : cards){
			ImageView iv = (ImageView) GuiUtil.getNodeFromGridPane(_gridPane, cards.indexOf(dc), row);
			iv.setImage(new Image(new File("src/main/resources/javafx/images/devel_cards/devcards_f_en_c_"+dc.getId()+".png").toURI().toString()));
		}
	}
	/**
	 * Initial setup
	 * @param me the player
	 */
	public void setPlayer(Player me) {
		_player = me;
		
		List<DevelopmentCard> territoryCards = _player.getDevelopmentCards(GC.DEV_TERRITORY);
		List<DevelopmentCard> buildingCards = _player.getDevelopmentCards(GC.DEV_BUILDING);
		
		setCardImages(CHAR_TERRITORY_ROW, territoryCards);
		setCardImages(BUILDINGS_VENTURE_ROW, buildingCards);
	}
}
