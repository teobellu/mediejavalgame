package client.gui;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.GC;
import game.GameBoard;
import game.LeaderCard;
import game.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MainViewController {

	private GUI _GUI;
	
	@FXML
	private ImageView _backgroundImage;
	
	@FXML
	private ImageView _leaderCard0;
	@FXML
	private ImageView _leaderCard1;
	@FXML
	private ImageView _leaderCard2;
	@FXML
	private ImageView _leaderCard3;
	
	private ArrayList<ImageView> _leaderCards = new ArrayList<>(
			Arrays.asList(_leaderCard0, _leaderCard1, _leaderCard2, _leaderCard3));
	
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
	private void initialize(){
		changeImageView("src/main/resources/javafx/images/gameboard_f_c.jpeg", _backgroundImage);
		
		for(ImageView iv : _leaderCards){
			changeImageView("src/main/resources/javafx/images/leaders_b_c_00.jpg", iv);
		}
		
		_buttonPane.setDisable(true);
		
		Text text = new Text("Waiting game initial setup...");
		
		text.setFont(new Font(24));
		
		_infoTextFlow.getChildren().add(text);
	}
	
	private void changeImageView(String path, ImageView iv){
		File file = new File(path);
		Image bg = new Image(file.toURI().toString());
		
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
		//TODO mettere le info a posto
	}
	
	private Logger _log = Logger.getLogger(MainViewController.class.getName());
}
