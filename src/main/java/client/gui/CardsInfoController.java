package client.gui;

import java.io.File;
import java.util.List;

import game.GC;
import game.Player;
import game.development.DevelopmentCard;
import javafx.fxml.FXML;
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
		/*_gridPane = new GridPane();
		
		for (int col = 0; col < 6; col ++){
			Node image = new ImageView();
			_gridPane.add(image, col, 0);
			Node image2 = new ImageView();
			_gridPane.add(image2, col, 1);
		}*/
		_bg.setImage(new Image(new File("src/main/resources/javafx/images/custom1.jpg").toURI().toString()));
	}
	
	@FXML
	private void onArrowClicked(){
		if(_isArrowClicked){
			_arrowButton.setText(">");
			_bg.setImage(new Image(new File("src/main/resources/javafx/images/custom1.jpg").toURI().toString()));
			
			List<DevelopmentCard> territoryCards = _player.getDevelopmentCards(GC.DEV_TERRITORY);
			List<DevelopmentCard> buildingCards = _player.getDevelopmentCards(GC.DEV_BUILDING);
			
			setCardImages(0, territoryCards);
			setCardImages(1, buildingCards);
			
			_isArrowClicked = false;
		} else {
			_arrowButton.setText("<");
			_bg.setImage(new Image(new File("src/main/resources/javafx/images/custom2.jpg").toURI().toString()));
			
			List<DevelopmentCard> characterCards = _player.getDevelopmentCards(GC.DEV_CHARACTER);
			List<DevelopmentCard> achievementCards = _player.getDevelopmentCards(GC.DEV_VENTURE);
			
			setCardImages(0, characterCards);
			setCardImages(1, achievementCards);
			
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
			System.out.println(_gridPane);
			//TODO nullpointerexception code:
			ImageView iv = (ImageView) GuiUtil.getNodeFromGridPane(_gridPane, cards.indexOf(dc), row);
			iv.setImage(new Image(new File("src/main/resources/javafx/images/devel_cards/devcards_f_en_c_"+dc.getId()+".png").toURI().toString()));
			//prova:
			//ImageView node = new ImageView(new Image(new File("src/main/resources/javafx/images/devel_cards/devcards_f_en_c_"+dc.getId()+".png").toURI().toString()));
			//_gridPane.add(node, cards.indexOf(dc), row);
			// ????????????????????? TODO
		}
	}
}
