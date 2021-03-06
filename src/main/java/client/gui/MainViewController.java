package client.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
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
import model.DevelopmentCard;
import model.FamilyMember;
import model.GC;
import model.GameBoard;
import model.LeaderCard;
import model.Player;
import model.Space;

/**
 * Controller for the main scene
 * @author Jacopo
 * @author Matteo
 */
public class MainViewController {

	/**
	 * ImageView for the map
	 */
	@FXML
	private ImageView _backgroundImage;//mappa
	
	/**
	 * Show More button
	 */
	@FXML
	private Button _downArrowButton;
	
	/**
	 * ImageView for resource pane
	 */
	@FXML
	private ImageView _goldWoodBg;
	/**
	 * ImageView for resource pane
	 */
	@FXML
	private ImageView _stoneServantBg;
	
	/**
	 * ImageView for first leader card
	 */
	@FXML
	private ImageView _leaderCard0;
	/**
	 * ImageView for second leader card
	 */
	@FXML
	private ImageView _leaderCard1;
	/**
	 * ImageView for third leader card
	 */
	@FXML
	private ImageView _leaderCard2;
	/**
	 * ImageView for fourth leader card
	 */
	@FXML
	private ImageView _leaderCard3;
	
	/**
	 * ImageView for first excommunication card
	 */
	@FXML
	private ImageView _excCard1;
	/**
	 * ImageView for second excommunication card
	 */
	@FXML
	private ImageView _excCard2;
	/**
	 * ImageView for third excommunication card
	 */
	@FXML
	private ImageView _excCard3;
	
	/**
	 * ImageView for production action little space
	 */
	@FXML
	private ImageView _productionLittleSpace;
	/**
	 * ImageView for harvest action little space
	 */
	@FXML
	private ImageView _harvestLittleSpace;
	/**
	 * ImageView for first market action space
	 */
	@FXML
	private ImageView _market0;
	/**
	 * ImageView for second market action space
	 */
	@FXML
	private ImageView _market1;
	/**
	 * ImageView for third market action space
	 */
	@FXML
	private ImageView _market2;
	/**
	 * ImageView for fourth market action space
	 */
	@FXML
	private ImageView _market3;
	
	/**
	 * GridPane to organize development cards
	 */
	@FXML
	private GridPane _towersCardsGridPane;
	/**
	 * GridPane to organize familiars on the tower
	 */
	@FXML
	private GridPane _towersFamiliarsGridPane;
	
	/**
	 * place familiar
	 */
	@FXML
	private Button _firstButton;
	/**
	 * activate leader card
	 */
	@FXML
	private Button _secondButton;
	/**
	 * drop leader card
	 */
	@FXML
	private Button _thirdButton;
	/**
	 * end turn
	 */
	@FXML
	private Button _fourthButton;
	/**
	 * show your cards
	 */
	@FXML
	private Button _fifthButton;
	/**
	 * show vatican support
	 */
	@FXML
	private Button _sixthButton;
	/**
	 * Play OPT leader cards
	 */
	@FXML
	private Button _seventhButton;

	/**
	 * Panel with the main buttons
	 */
	@FXML
	private AnchorPane _buttonPane;
	
	/**
	 * Scrollpane for the infoBox
	 */
	@FXML
	private ScrollPane _infoScrollPane;
	/**
	 * InfoBox
	 */
	@FXML
	private TextFlow _infoTextFlow;
	
	/**
	 * Value of the black dice
	 */
	@FXML
	private Text _blackDiceValue;
	/**
	 * Value of the white dice
	 */
	@FXML
	private Text _whiteDiceValue;
	/**
	 * Value of the orange dice
	 */
	@FXML
	private Text _orangeDiceValue;
	
	/**
	 * Value of the gold
	 */
	@FXML
	private Text _goldValue;
	/**
	 * Value of the wood
	 */
	@FXML
	private Text _woodValue;
	/**
	 * Value of the stone
	 */
	@FXML
	private Text _stoneValue;
	/**
	 * Value of the servant
	 */
	@FXML
	private Text _servantValue;
	
	/**
	 * Pane dynamically loaded when 
	 */
	@FXML
	private AnchorPane _frontBackMainPane;
	
	/**
	 * GridPane for council palace familiars
	 */
	@FXML
	private GridPane _councilPalaceGrid;
	/**
	 * GridPane for production space familiars
	 */
	@FXML
	private GridPane _productionSpaceGrid;
	/**
	 * GridPane for harvest space familiars
	 */
	@FXML
	private GridPane _harvestSpaceGrid;
	
	/**
	 * ImageView for faith points
	 */
	@FXML
	private ImageView _faithPointsIcon;
	/**
	 * ImageView for military points
	 */
	@FXML
	private ImageView _militaryPointsIcon;
	/**
	 * ImageView for victory points
	 */
	@FXML
	private ImageView _victoryPointsIcon;
	
	/**
	 * Faith points value
	 */
	@FXML
	private Text _faithValue;
	/**
	 * Military points value
	 */
	@FXML
	private Text _militaryValue;
	/**
	 * Victory points value
	 */
	@FXML
	private Text _victoryValue;
	
	/**
	 * Textflow displayng the personal bonus
	 */
	@FXML
	private TextFlow _personalBonusTextFlow;
	
	/**
	 * List of leader cards
	 */
	private ArrayList<ImageView> _leaderCards = new ArrayList<>();
	
	/**
	 * Show more clicked?
	 */
	private boolean _downArrowClicked = false;
	
	/**
	 * The GUI
	 */
	private GUI _GUI;
	
	/**
	 * The logger
	 */
	private transient Logger _log = Logger.getLogger(MainViewController.class.getName());
	
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
	
	/**
	 * Set the image for an imageview, and returns it
	 * @param path path to the image
	 * @param iv the imageview
	 * @return the imageview
	 */
	private ImageView changeImageView(String path, ImageView iv){
		File file = new File(path);
		Image bg = new Image(file.toURI().toString());
		
		if(iv == _productionLittleSpace){
		}
		
		if(iv==null){
			iv = new ImageView();
		}
		
		iv.setImage(bg);
		
		return iv;
	}
	
	/**
	 * Disable Button Panel
	 */
	private void endTurn(){
		_buttonPane.setDisable(true);
	}
	
	/**
	 * Initial setup
	 */
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
		
		changeImageView("src/main/resources/javafx/images/faith_point_icon.png", _faithPointsIcon);
		changeImageView("src/main/resources/javafx/images/mil_point_icon.png", _militaryPointsIcon);
		changeImageView("src/main/resources/javafx/images/vic_point_icon.png", _victoryPointsIcon);
		
		_buttonPane.setDisable(true);
		_downArrowButton.setDisable(true);
		
		appendToInfoText("\nWaiting game initial setup...", 24);

	}
	
	/**
	 * Called on Show More Button pressed
	 */
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
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	/**
	 * Called on Show Cards Button pressed
	 */
	@FXML
	private void onFifthButtonClicked(){
		_GUI.showCardsInfoDialog(GraphicalUI.getInstance().getCachedMe());
	}
	
	/**
	 * Called on Place Familiar Button pressed
	 */
	@FXML
	private void onFirstButtonClicked(){
		_GUI.showPlaceFamiliar(GraphicalUI.getInstance().getCachedBoard(), GraphicalUI.getInstance().getCachedMe().getFreeMembers());
	}
	
	/**
	 * Called on End Turn Button pressed
	 */
	@FXML
	private void onFourthButtonClicked(){
		synchronized (this) {
			Runnable run = () -> GraphicalUI.getInstance().endTurn();
			new Thread(run).start();
			endTurn();
		}
	}
	
	/**
	 * Called on Activate Leader Button pressed
	 */
	@FXML
	private void onSecondButtonClicked(){
		_GUI.showActivateLeaderDialog(GraphicalUI.getInstance().getCachedMe().getLeaderCards());
	}
	
	/**
	 * Called on Drop Leader Button pressed
	 */
	@FXML
	private void onThirdButtonClicked(){
		List<String> leaders = new ArrayList<>();
		List<LeaderCard> lead = GraphicalUI.getInstance().getCachedMe().getLeaderCards();
		
		for(LeaderCard lc : lead){
			leaders.add(lc.getName());
		}
		
		_GUI.showDropLeaderDialog(leaders);
	}
	
	/**
	 * Called on Show Vatican Support pressed
	 */
	@FXML
	private void onSixthButtonClicked(){
		synchronized (this) {
			Runnable run = () -> GraphicalUI.getInstance().showVaticanSupport();
			new Thread(run).start();
		}
	}
	
	/**
	 * Called on Play OPT Leader pressed
	 */
	@FXML
	private void onSeventhButtonClicked(){
		synchronized (this) {
			Runnable run = () -> GraphicalUI.getInstance().activateOPTLeaders();
			new Thread(run).start();
		}
	}
	
	/**
	 * Set the gui
	 * @param gui the gui
	 */
	public void setGUI(GUI gui){
		_GUI = gui;
	}
	
	/**
	 * Handle the creation of the back panel
	 * @param board the board
	 * @param me the player
	 */
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
		
		_productionLittleSpace = (ImageView) _frontBackMainPane.getChildren().get(10);
		_harvestLittleSpace = (ImageView) _frontBackMainPane.getChildren().get(11);
		
		_market0 = (ImageView) _frontBackMainPane.getChildren().get(12);
		_market1 = (ImageView) _frontBackMainPane.getChildren().get(13);
		_market2 = (ImageView) _frontBackMainPane.getChildren().get(14);
		_market3 = (ImageView) _frontBackMainPane.getChildren().get(15);
		
		
		changeImageView("src/main/resources/javafx/images/board_sotto.jpeg", _backgroundImage);
		
		_blackDiceValue.setText(board.getDices()[0].toString());
		_whiteDiceValue.setText(board.getDices()[1].toString());
		_orangeDiceValue.setText(board.getDices()[2].toString());
		
		List<ImageView> excCards = new ArrayList<>(
				Arrays.asList(_excCard1, _excCard2, _excCard3));
		for(int i = 0;i<GC.NUMBER_OF_AGES;i++){
			changeImageView("src/main/resources/javafx/images/exc_tiles/excomm_"+board.getExCard()[i].getAge()+"_"+ board.getExCard()[i].getID() +".png", excCards.get(i));
		}
		
		int i = 0;
		List<FamilyMember> fm;
		for(int row = 0;row<2;row++){
			for(int column = row;column<2;column++){

				//add to council palace space
				fm = board.getCouncilPalaceSpace().getFamiliars();
				if(fm!=null && !fm.isEmpty()){
					try {
						_councilPalaceGrid.add(
							new Text(fm.get(i).getOwner().getName()+": "+GuiUtil.cleanUnderscoresCapsFirst(fm.get(i).getColor())+" familiar"), 
							column, row);
					} catch (IndexOutOfBoundsException e) {
						_log.log(Level.OFF, e.getMessage(), e);
						_councilPalaceGrid.add(new Text(""), column, row);
					}
				}
				
				//add to production space
				fm = board.getWorkLongSpace(GC.PRODUCTION).getFamiliars();
				if(fm!=null && !fm.isEmpty()){
					try {
						_productionSpaceGrid.add(
								new Text(fm.get(i).getOwner().getName()+": "+GuiUtil.cleanUnderscoresCapsFirst(fm.get(i).getColor())+" familiar"), 
								column, row);
					} catch (IndexOutOfBoundsException e) {
						_log.log(Level.OFF, e.getMessage(), e);
						_productionSpaceGrid.add(new Text(""), column, row);
					}
					
				}
				
				//add to harvest space
				fm = board.getWorkLongSpace(GC.HARVEST).getFamiliars();
				if(fm!=null && !fm.isEmpty()){
					try {
						_harvestSpaceGrid.add(
								new Text(fm.get(i).getOwner().getName()+": "+GuiUtil.cleanUnderscoresCapsFirst(fm.get(i).getColor())+" familiar"), 
								column, row);
					} catch (IndexOutOfBoundsException e) {
						_log.log(Level.OFF, e.getMessage(), e);
						_harvestSpaceGrid.add(new Text(""), column, row);
					}
				}
				
				i++;
			}
		}
		
		fm = board.getWorkSpace(GC.PRODUCTION).getFamiliars();
		if(fm!=null && !fm.isEmpty()){
			
			changeImageView("src/main/resources/javafx/images/familiars/fam_"+fm.get(0).getOwner().getColour()+"_"+fm.get(0).getColor()+".png", _productionLittleSpace);
		}
		
		fm = board.getWorkSpace(GC.HARVEST).getFamiliars();
		if(fm!=null && !fm.isEmpty()){
			changeImageView("src/main/resources/javafx/images/familiars/fam_"+fm.get(0).getOwner().getColour()+"_"+fm.get(0).getColor()+".png", _harvestLittleSpace);
		}
		
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
		
		List<ImageView> markets = new ArrayList<>(
				Arrays.asList(_market0, _market1, _market2, _market3));
		
		for(ImageView iv : markets){
			List<FamilyMember> fams = board.getMarketSpace(markets.indexOf(iv)).getFamiliars();
			if(!fams.isEmpty()){
				changeImageView("src/main/resources/javafx/images/familiars/fam_"+fams.get(0).getOwner().getColour()+"_"+fams.get(0).getColor()+".png", iv);
			} else {
				iv.setImage(null);
			}
		}
	}
	
	/**
	 * Handle the creation of the front panel
	 * @param board the board
	 * @param me the player
	 */
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
		_sixthButton = (Button) _buttonPane.getChildren().get(5);
		_seventhButton = (Button) _buttonPane.getChildren().get(6);
		
		_firstButton.setOnAction(event -> onFirstButtonClicked());
		_secondButton.setOnAction(event -> onSecondButtonClicked());
		_thirdButton.setOnAction(event -> onThirdButtonClicked());
		_fourthButton.setOnAction(event -> onFourthButtonClicked());
		_fifthButton.setOnAction(event -> onFifthButtonClicked());
		_sixthButton.setOnAction(event -> onSixthButtonClicked());
		_seventhButton.setOnAction(event -> onSeventhButtonClicked());
		
		for(int row = 0;row<GameBoard.MAX_ROW;row++){
			for(int column = 0;column<GameBoard.MAX_COLUMN;column++){
				ImageView iv = (ImageView) GuiUtil.getNodeFromGridPane(_towersCardsGridPane, column, row);
				
				if(iv==null){
					iv = new ImageView();
					_towersCardsGridPane.add(iv, column, row);
				}
				
				DevelopmentCard card = board.getCard((GameBoard.MAX_ROW -1 - row), column);
				
				if(card!=null){
					changeImageView("src/main/resources/javafx/images/devel_cards/devcards_f_en_c_"+ card.getId() +".png", iv);
				} else {
					iv.setImage(null);
				}
				
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
	
	/**
	 * Setup for starting turn
	 * @param me the player
	 * @param board the board
	 */
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
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		updateLeaderCards(me);
		updateSidePanelPoints(me);
		
		_personalBonusTextFlow.getChildren().clear();
		_personalBonusTextFlow.getChildren().add(new Text("Production Bonus\n"));
		_personalBonusTextFlow.getChildren().add(new Text(me.getBonus(GC.PRODUCTION).toString()+"\n"));
		_personalBonusTextFlow.getChildren().add(new Text("Harvest Bonus\n"));
		_personalBonusTextFlow.getChildren().add(new Text(me.getBonus(GC.HARVEST).toString()+"\n"));
		
		appendToInfoText("It's YOUR turn now!", 24);
		appendToInfoText("What do you want to do?");
		
		_buttonPane.setDisable(false);
		_downArrowButton.setDisable(false);
	}
	
	/**
	 * Update the board and the player parameters
	 */
	public void updateBoardAndPlayer(){
		if(_downArrowClicked){//sono nella schermata dietro
			setupBackMainView(GraphicalUI.getInstance().getCachedBoard(), GraphicalUI.getInstance().getCachedMe());
		} else {//sono nella schermata avanti
			setupFrontMainView(GraphicalUI.getInstance().getCachedBoard(), GraphicalUI.getInstance().getCachedMe());
		}
		
		updateLeaderCards(GraphicalUI.getInstance().getCachedMe());
		updateSidePanelPoints(GraphicalUI.getInstance().getCachedMe());
	}
	
	/**
	 * Update Leader Cards pane
	 * @param me
	 */
	private void updateLeaderCards(Player me){
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
	}
	
	/**
	 * Set side pane
	 * @param me the player
	 */
	private void updateSidePanelPoints(Player me){
		_faithValue.setText(me.getResource(GC.RES_FAITHPOINTS)>9 ? 
				String.valueOf(me.getResource(GC.RES_FAITHPOINTS)) : "0"+String.valueOf(me.getResource(GC.RES_FAITHPOINTS)));
		_militaryValue.setText(me.getResource(GC.RES_MILITARYPOINTS)>9 ? 
				String.valueOf(me.getResource(GC.RES_MILITARYPOINTS)) : "0"+String.valueOf(me.getResource(GC.RES_MILITARYPOINTS)));
		_victoryValue.setText(me.getResource(GC.RES_VICTORYPOINTS)>9 ? 
				String.valueOf(me.getResource(GC.RES_VICTORYPOINTS)) : "0"+String.valueOf(me.getResource(GC.RES_VICTORYPOINTS)));
	}
}
