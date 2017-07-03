package client.gui;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.FamilyMember;
import game.GC;
import game.GameBoard;
import game.LeaderCard;
import game.Player;
import game.Space;
import game.development.DevelopmentCard;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
	private ImageView _backgroundImage;//mappa
	
	@FXML
	private Button _downArrowButton;
	
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
	private AnchorPane _buttonPane;//pannello con tutti i bottoni
	
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
	
	@FXML
	private AnchorPane _frontBackMainPane;
	
	@FXML
	private GridPane _councilPalaceGrid;
	@FXML
	private GridPane _productionSpaceGrid;
	@FXML
	private GridPane _harvestSpaceGrid;
	
	private ArrayList<ImageView> _leaderCards = new ArrayList<>();
	
	private boolean _downArrowClicked = false;
	
	private GUI _GUI;
	
	public void initialSetupController(){
		_infoTextFlow.getChildren().addListener((ListChangeListener<Node>) ((change) -> {
			_infoTextFlow.layout();
			_infoScrollPane.layout();
			_infoScrollPane.setVvalue(1.0f);
        }));
		_infoScrollPane.setContent(_infoTextFlow);		
		
		changeImageView("src/main/resources/javafx/images/board_sopra.jpeg", _backgroundImage);
		
		_leaderCards.add(_leaderCard0);
		_leaderCards.add(_leaderCard1);
		_leaderCards.add(_leaderCard2);
		_leaderCards.add(_leaderCard3);
		
		for(ImageView iv : _leaderCards){
			changeImageView("src/main/resources/javafx/images/leaders/leaders_b_c_00.jpg", iv);
		}
		
		_buttonPane.setDisable(true);
		_downArrowButton.setDisable(true);
		
		appendToInfoText("\nWaiting game initial setup...", 24);

	}
	
	/*https://stackoverflow.com/q/28243156*/
	/**
	 * Append and show a text to the _buttonPane with font 18
	 * @param msg
	 */
	public void appendToInfoText(String msg){
		appendToInfoText(msg, 18);
	}
	
	/**
	 * Append and show a text to the _buttonPane with the specified font
	 * @param msg the message to display
	 * @param fontSize the font size
	 */
	public synchronized void appendToInfoText(String msg, double fontSize){
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
			System.out.println("ImageView null");
			iv = new ImageView();
		}
		
		iv.setImage(bg);
	}
	
	public void setGUI(GUI gui){
		_GUI = gui;
	}
	
	@FXML
	private void onFirstButtonClicked(){
		_GUI.showPlaceFamiliar(GraphicalUI.getInstance().getCachedBoard(), GraphicalUI.getInstance().getCachedMe().getFreeMembers());
	}
	
	@FXML
	private void onSecondButtonClicked(){
		_GUI.showActivateLeaderDialog(GraphicalUI.getInstance().getCachedMe().getLeaderCards());
	}
	
	@FXML
	private void onThirdButtonClicked(){
		List<String> leaders = new ArrayList<>();
		List<LeaderCard> lead = GraphicalUI.getInstance().getCachedMe().getLeaderCards();
		
		for(LeaderCard lc : lead){
			leaders.add(lc.getName());
		}
		
		_GUI.showDropLeaderDialog(leaders);
	}
	
	@FXML
	private void onFourthButtonClicked(){
		try {
			synchronized (this) {
				GraphicalUI.getInstance().endTurn();
				endTurn();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	@FXML
	private void onFifthButtonClicked(){
		_GUI.showCardsInfoDialog(GraphicalUI.getInstance().getCachedMe());
	}
	
	public void updateBoard(GameBoard board){
		//TODO
	}
	
	public void updatePlayer(Player me){
		
	}
	
	public void startTurn(Player me, GameBoard board){
		_buttonPane.setDisable(false);
		_downArrowButton.setDisable(false);
		
		try {
			_frontBackMainPane.getChildren().clear();
			_frontBackMainPane.getChildren().setAll((AnchorPane)FXMLLoader.load(GUI.class.getResource("/client/gui/FrontMainView.fxml")));
			_frontBackMainPane = (AnchorPane) _frontBackMainPane.getChildren().get(0);
			
			_buttonPane.getChildren().clear();
			_buttonPane.getChildren().setAll((AnchorPane)FXMLLoader.load(GUI.class.getResource("/client/gui/FrontButtonPane.fxml")));
			_buttonPane = (AnchorPane) _buttonPane.getChildren().get(0);
			
			setupFrontMainView(board, me);
		} catch (/*TODO IOException*/Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
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
		
		//TODO mettere le info a posto
		
		appendToInfoText("It's YOUR turn now!", 24);
		appendToInfoText("What do you want to do?");
	}
	
	@FXML
	private void onDownArrowPressed(){
		try {
			GameBoard board = GraphicalUI.getInstance().getCachedBoard();
			Player me = GraphicalUI.getInstance().getCachedMe();
			_frontBackMainPane.getChildren().clear();
			_buttonPane.getChildren().clear();
			if(_downArrowClicked){
				_frontBackMainPane.getChildren().setAll((AnchorPane)FXMLLoader.load(GUI.class.getResource("/client/gui/FrontMainView.fxml")));
				_frontBackMainPane = (AnchorPane) _frontBackMainPane.getChildren().get(0);
				
				_buttonPane.getChildren().setAll((AnchorPane)FXMLLoader.load(GUI.class.getResource("/client/gui/FrontButtonPane.fxml")));
				_buttonPane = (AnchorPane) _buttonPane.getChildren().get(0);
				
				setupFrontMainView(board, me);
				_downArrowClicked = false;
			} else {
				_frontBackMainPane.getChildren().setAll((AnchorPane)FXMLLoader.load(GUI.class.getResource("/client/gui/BackMainView.fxml")));
				_frontBackMainPane = (AnchorPane) _frontBackMainPane.getChildren().get(0);
				
				_buttonPane.getChildren().setAll((AnchorPane)FXMLLoader.load(GUI.class.getResource("/client/gui/BackButtonPane.fxml")));
				_buttonPane = (AnchorPane) _buttonPane.getChildren().get(0);
				
				setupBackMainView(board, me);
				_downArrowClicked = true;
			}
		} catch (RemoteException e) {
			// TODO: handle exception
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setupFrontMainView(GameBoard board, Player me){
		_backgroundImage = (ImageView) _frontBackMainPane.getChildren().get(0);
		_towersCardsGridPane = (GridPane) _frontBackMainPane.getChildren().get(1);
		_towersFamiliarsGridPane = (GridPane) _frontBackMainPane.getChildren().get(2);
		
		changeImageView("src/main/resources/javafx/images/board_sopra.jpeg", _backgroundImage);
		
		_firstButton = (Button) _buttonPane.getChildren().get(0);
		_secondButton = (Button) _buttonPane.getChildren().get(1);
		_thirdButton = (Button) _buttonPane.getChildren().get(2);
		_fourthButton = (Button) _buttonPane.getChildren().get(3);
		_fifthButton = (Button) _buttonPane.getChildren().get(4);
		
		_firstButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				onFirstButtonClicked();
			}
		});
		_secondButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				onSecondButtonClicked();
			}
		});
		_thirdButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				onThirdButtonClicked();
			}
		});
		_fourthButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				onFourthButtonClicked();
			}
		});
		_fifthButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				onFifthButtonClicked();
			}
		});
		
		for(int row = 0;row<GameBoard.MAX_ROW;row++){
			for(int column = 0;column<GameBoard.MAX_COLUMN;column++){
				ImageView iv = (ImageView) GuiUtil.getNodeFromGridPane(_towersCardsGridPane, column, row);
				
				if(iv==null){
					iv = new ImageView();
					_towersCardsGridPane.add(iv, column, row);
				}
				
				DevelopmentCard card = board.getCard((GameBoard.MAX_ROW -1 - row), column);
				changeImageView("src/main/resources/javafx/images/devel_cards/devcards_f_en_c_"+ card.getId() +".png", iv);
				
				Space space = board.getFromTowers((GameBoard.MAX_ROW - 1 - row), column);
				List<FamilyMember> fams = space.getFamiliars();
				if(!fams.isEmpty()){
					iv = (ImageView) GuiUtil.getNodeFromGridPane(_towersFamiliarsGridPane, column, row);
					
					if(iv==null){
						iv = new ImageView();
						_towersFamiliarsGridPane.add(iv, column, row);
					}
					
					String path = "src/main/resources/javafx/images/familiars/fam_"+fams.get(0).getOwner().getColour()+"_"+fams.get(0).getColor()+".png";
					changeImageView(path, iv);
				}
			}
		}
	}
	
	private void setupBackMainView(GameBoard board, Player me){
		_backgroundImage = (ImageView) _frontBackMainPane.getChildren().get(0);
		_blackDiceValue = (Text) _frontBackMainPane.getChildren().get(1);
		_whiteDiceValue = (Text) _frontBackMainPane.getChildren().get(2);
		_orangeDiceValue = (Text) _frontBackMainPane.getChildren().get(3);
		_excCard1 = (ImageView) _frontBackMainPane.getChildren().get(4);
		_excCard2 = (ImageView) _frontBackMainPane.getChildren().get(5);
		_excCard3 = (ImageView) _frontBackMainPane.getChildren().get(6);
		_councilPalaceGrid = (GridPane) _frontBackMainPane.getChildren().get(7);
		_productionSpaceGrid = (GridPane) _frontBackMainPane.getChildren().get(8);
		_harvestSpaceGrid = (GridPane) _frontBackMainPane.getChildren().get(9);
		
		changeImageView("src/main/resources/javafx/images/board_sotto.jpeg", _backgroundImage);
		
		_blackDiceValue.setText(board.getDices()[0].toString());
		_whiteDiceValue.setText(board.getDices()[1].toString());
		_orangeDiceValue.setText(board.getDices()[2].toString());
		
		List<ImageView> excCards = new ArrayList<>(
				Arrays.asList(_excCard1, _excCard2, _excCard3));
		for(int i = 0;i<GC.NUMBER_OF_AGES;i++){
			changeImageView("src/main/resources/javafx/images/exc_tiles/excomm_"+board.getExCard()[0].getAge()+"_"+ board.getExCard()[0].getID() +".png", excCards.get(i));
		}
		
		//TODO sistemare le gridpane degli spazi azione
		
		_goldWoodBg = (ImageView) _buttonPane.getChildren().get(0);
		_stoneServantBg = (ImageView) _buttonPane.getChildren().get(1);
		_goldValue = (Text) _buttonPane.getChildren().get(2);
		_woodValue = (Text) _buttonPane.getChildren().get(3);
		_stoneValue = (Text) _buttonPane.getChildren().get(4);
		_servantValue = (Text) _buttonPane.getChildren().get(5);
		
		_goldValue.setText(me.getResource(GC.RES_COINS).toString());
		_woodValue.setText(me.getResource(GC.RES_WOOD).toString());
		_stoneValue.setText(me.getResource(GC.RES_STONES).toString());
		_servantValue.setText(me.getResource(GC.RES_SERVANTS).toString());
		
		changeImageView("src/main/resources/javafx/images/risorse2.jpg", _goldWoodBg);
		changeImageView("src/main/resources/javafx/images/risorse1.jpg", _stoneServantBg);
	}
	
	private void endTurn(){
		_buttonPane.setDisable(true);
	}
	
	private Logger _log = Logger.getLogger(MainViewController.class.getName());
}
