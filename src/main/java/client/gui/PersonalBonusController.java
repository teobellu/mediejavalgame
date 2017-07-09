package client.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import game.GC;
import game.Resource;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Controller for the initial personal bonus dialog
 * @author Jacopo
 *
 */
public class PersonalBonusController extends DialogAbstractController{

	@FXML
	private Button _bonus1;
	@FXML
	private Button _bonus2;
	@FXML
	private Button _bonus3;
	@FXML
	private Button _bonus4;
	@FXML
	private Button _bonus5;
	
	private List<Button> _buttons;
	
	/**
	 * Called on Bonus Button pressed
	 * @param i the button pressed
	 */
	private void handleButton(int i){
		GraphicalUI.getInstance().addFromGUIToGraphical(i);
		GraphicalUI.getInstance().notifyCommandToGui();
		_dialog.close();
	}
	
	/**
	 * FXML method
	 */
	@FXML
	private void initialize(){
	}
	
	/**
	 * Called on fifth Button pressed
	 */
	@FXML
	private void onFifthButtonClicked(){
		handleButton(4);
	}
	
	/**
	 * Called on first Button pressed
	 */
	@FXML
	private void onFirstButtonClicked(){
		handleButton(0);
	}
	
	/**
	 * Called on fourth Button pressed
	 */
	@FXML
	private void onFourthButtonClicked(){
		handleButton(3);
	}
	
	/**
	 * Called on second Button pressed
	 */
	@FXML
	private void onSecondButtonClicked(){
		handleButton(1);
	}
	
	/**
	 * Called on third Button pressed
	 */
	@FXML
	private void onThirdButtonClicked(){
		handleButton(2);
	}
	
	/**
	 * Setup buttons text and background
	 * @param button the button
	 * @param resource production resource
	 * @param resource2 harvest resource
	 */
	private void setButton(Button button, Resource resource, Resource resource2){
		Label label = new Label("Produzione: "+resource.toString()+"\nRaccolto: "+resource2.toString());
		label.setRotate(-90);
		label.setFont(Font.font(null, FontWeight.BOLD, 18));

		button.setGraphic(new Group(label));
		
		File file = new File("src/main/resources/javafx/images/personal_bonus_standard.jpg/");
		Image image = new Image(file.toURI().toString());

		BackgroundSize backSize = new BackgroundSize(GuiSizeConstants.INITIAL_PERSONAL_BONUS_WIDTH, GuiSizeConstants.INITIAL_PERSONAL_BONUS_HEIGHT, false, false, false, true);
		BackgroundImage backImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backSize);
		button.setBackground(new Background(backImage));
		button.setDisable(false);
	}
	
	/**
	 * Initial setup
	 * @param map the bonus map
	 */
	public void setMap(HashMap<String, List<Resource>> map){
		_buttons = new ArrayList<>();
		
		_buttons.add(_bonus1);
		_buttons.add(_bonus2);
		_buttons.add(_bonus3);
		_buttons.add(_bonus4);
		_buttons.add(_bonus5);
		
		int i = 0;
		
		for(; i<map.get(GC.HARVEST).size();i++){
			setButton(_buttons.get(i), map.get(GC.HARVEST).get(i), map.get(GC.PRODUCTION).get(i));
		}
		
		for(;i<_buttons.size();i++){
			_buttons.get(i).setDisable(true);
		}
	}
	
}
