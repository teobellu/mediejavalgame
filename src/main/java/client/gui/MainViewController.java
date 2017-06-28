package client.gui;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.GC;
import game.GameBoard;
import game.Player;
import game.Space;
import game.development.DevelopmentCard;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MainViewController {

	@FXML
	private ImageView _backgroundImage;
	
	@FXML
	private ImageView _goldWoodBg;
	@FXML
	private ImageView _stoneServantBg;
	
	@FXML
	private ImageView _leaderCard0;
	@FXML
	private ImageView _leaderCard1;
	@FXML
	private ImageView _leaderCard2;
	@FXML
	private ImageView _leaderCard3;
	
	@FXML
	private ImageView _excCard1;
	@FXML
	private ImageView _excCard2;
	@FXML
	private ImageView _excCard3;
	
	@FXML
	private ImageView _CouncilPalaceServant1;
	@FXML
	private ImageView _CouncilPalaceServant2;
	@FXML
	private ImageView _CouncilPalaceServant3;
	@FXML
	private ImageView _CouncilPalaceServant4;
	
	@FXML
	private GridPane _towersCardsGridPane;
	@FXML
	private GridPane _towersFamiliarsGridPane;
	
	@FXML
	private Button _firstButton;//place familiar
	@FXML
	private Button _secondButton;//activate leader card
	@FXML
	private Button _thirdButton;//drop leader card
	@FXML
	private Button _fourthButton;//endturn
	@FXML
	private Button _fifthButton;//show your cards

	@FXML
	private AnchorPane _buttonPane;
	
	@FXML
	private ScrollPane _infoScrollPane;
	@FXML
	private TextFlow _infoTextFlow;
	
	@FXML
	private Text _blackDiceValue;
	@FXML
	private Text _whiteDiceValue;
	@FXML
	private Text _orangeDiceValue;
	
	@FXML
	private Text _goldValue;
	@FXML
	private Text _woodValue;
	@FXML
	private Text _stoneValue;
	@FXML
	private Text _servantValue;
	
	private ArrayList<ImageView> _leaderCards = new ArrayList<>();
	
	private GUI _GUI;
	
	public void initialSetupController(){
		_infoTextFlow.getChildren().addListener((ListChangeListener<Node>) ((change) -> {
			_infoTextFlow.layout();
			_infoScrollPane.layout();
			_infoScrollPane.setVvalue(1.0f);
        }));
		_infoScrollPane.setContent(_infoTextFlow);		
		
		changeImageView("src/main/resources/javafx/images/gameboard_f_c.jpeg", _backgroundImage);
		
		_leaderCards.add(_leaderCard0);
		_leaderCards.add(_leaderCard1);
		_leaderCards.add(_leaderCard2);
		_leaderCards.add(_leaderCard3);
		
		for(ImageView iv : _leaderCards){
		changeImageView("src/main/resources/javafx/images/leaders/leaders_b_c_00.jpg", iv);
		}
		
		_buttonPane.setDisable(true);
		
		changeImageView("src/main/resources/javafx/images/risorse2.jpg", _goldWoodBg);
		changeImageView("src/main/resources/javafx/images/risorse1.jpg", _stoneServantBg);
		
		appendToInfoText("\nWaiting game initial setup...", 24);

	}
	
	@FXML
	private void initialize(){
		
		
	}
	
	/*https://stackoverflow.com/q/28243156*/
	private void appendToInfoText(String msg){
		appendToInfoText(msg, 18);
	}
	
	private synchronized void appendToInfoText(String msg, double fontSize){
		Platform.runLater(() -> {
			Text t = new Text(msg+"\n");
			t.setFont(new Font(fontSize));
			_infoTextFlow.getChildren().add(t);
		});
	}
	
	private void changeImageView(String path, ImageView iv){
		File file = new File(path);
		Image bg = new Image(file.toURI().toString());
		
		if(iv==null){
			System.out.println("Imageview nulla. WTF?");
		}
		
		iv.setImage(bg);
	}
	
	public void setGUI(GUI gui){
		_GUI = gui;
	}
	
	@FXML
	private void onFirstButtonClicked(){
		_GUI.showPlaceFamiliar(GraphicalUI.getInstance().placeFamiliar());
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
	
	@FXML
	private void onFifthButtonClicked(){
		_GUI.showCardsInfoDialog();
	}
	
	public void startTurn(Player me, GameBoard board){
		_buttonPane.setDisable(false);
		
		_blackDiceValue.setText(board.getDices()[0].toString());
		_whiteDiceValue.setText(board.getDices()[1].toString());
		_orangeDiceValue.setText(board.getDices()[2].toString());
		
		_goldValue.setText(me.getResource(GC.RES_COINS).toString());
		_woodValue.setText(me.getResource(GC.RES_WOOD).toString());
		_stoneValue.setText(me.getResource(GC.RES_STONES).toString());
		_servantValue.setText(me.getResource(GC.RES_SERVANTS).toString());
		
		int i = 0;
		
		for(;i<me.getLeaderCards().size();i++){
			File file = new File("src/main/resources/javafx/images/leaders/" + me.getLeaderCards().get(i).getName() + ".jpg");
			Image image = new Image(file.toURI().toString());

			_leaderCards.get(i).setImage(image);
			
		}
		
		for(;i<_leaderCards.size();i++){
			File file = new File("src/main/resources/javafx/images/leaders/leaders_b_c_00.jpg");
			Image image = new Image(file.toURI().toString());
			_leaderCards.get(i).setImage(image);
			_leaderCards.get(i).setDisable(true);
		}
		
		
		//TODO mostrare i familiari nello spazio "palazzo del consiglio"
//		List<ImageView> councilPalaceServants = new ArrayList<>(
//				Arrays.asList(_CouncilPalaceServant1,_CouncilPalaceServant2,_CouncilPalaceServant3,_CouncilPalaceServant4));
//		
//		for(ImageView iv : councilPalaceServants){
//			
//		}
		
		for(int row = 0;row<GameBoard.MAX_ROW;row++){
			for(int column = 0;column<GameBoard.MAX_COLUMN;column++){
				ImageView iv = (ImageView) getNodeFromGridPane(_towersCardsGridPane, column, row);
				DevelopmentCard card = board.getCard(GameBoard.MAX_ROW - row, column);
				changeImageView("src/main/resources/javafx/images/devel_cards/devcards_f_en_c_"+ card.getId() +".png", iv);
				
				//TODO prendere e piazzare i familiari
				Space space = board.getFromTowers(GameBoard.MAX_ROW - row, column);
				space.getFamiliars().get(space.getFamiliars().size()-1);
			}
		}
		
		List<ImageView> excCards = new ArrayList<>(
				Arrays.asList(_excCard1, _excCard2, _excCard3));
		for(i = 0;i<GC.NUMBER_OF_AGES;i++){
			changeImageView("src/main/resources/javafx/images/exc_tiles/excomm_"+board.getExCard()[0].getAge()+"_"+ board.getExCard()[0].getID() +".png", excCards.get(i));
		}
		
		//TODO mettere le info a posto
		
		appendToInfoText("It's YOUR turn now!", 24);
		appendToInfoText("What do you want to do?");
	}
	
	private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
	    for (Node node : gridPane.getChildren()) {
	        if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
	            return node;
	        }
	    }
	    return null;
	}
	
	private Logger _log = Logger.getLogger(MainViewController.class.getName());
}
