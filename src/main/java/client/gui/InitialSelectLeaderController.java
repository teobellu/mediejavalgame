package client.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.text.Text;

/**
 * Controller for the initial select leader dialog
 * @author Jacopo
 *
 */
public class InitialSelectLeaderController extends DialogAbstractController{

	/**
	 * First card
	 */
	@FXML
	private Button _card1;
	/**
	 * Second card
	 */
	@FXML
	private Button _card2;
	/**
	 * Third card
	 */
	@FXML
	private Button _card3;
	/**
	 * Fourth card
	 */
	@FXML
	private Button _card4;
	
	/**
	 * Message text
	 */
	@FXML
	private Text _text;
	
	/**
	 * Called on Select Leader Button pressed
	 * @param choice the leader chosen
	 */
	private void handleButton(int choice){
		GraphicalUI.getInstance().addFromGUIToGraphical(choice);
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
	 * Called on fourth Button pressed
	 */
	@FXML
	private void onFifthButtonClicked(){
		handleButton(3);
	}
	
	/**
	 * Called on first Button pressed
	 */
	@FXML
	private void onFirstButtonClicked(){
		handleButton(0);
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
	 * Set bg of buttons based on leader
	 * @param leader the leader
	 * @param button the button
	 */
	private void setBgImages(String leader, Button button){
		File file = new File("src/main/resources/javafx/images/leaders/" + leader + ".jpg");
		Image image = new Image(file.toURI().toString());

		BackgroundSize backSize = new BackgroundSize(GuiSizeConstants.INITIAL_LEADER_WIDTH, GuiSizeConstants.INITIAL_LEADER_HEIGHT, false, false, false, true);
		BackgroundImage backImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backSize);
		button.setBackground(new Background(backImage));
		button.setDisable(false);
		button.setText(leader);
	}
	
	/**
	 * Initial setup
	 * @param leaders list of leaders
	 */
	public void setLeaderList(List<String> leaders){
		
		List<Button> buttons = new ArrayList<>();
		buttons.add(_card1);
		buttons.add(_card2);
		buttons.add(_card3);
		buttons.add(_card4);
		
		int index = 0;
		for (; index < leaders.size(); index++){
			setBgImages(leaders.get(index), buttons.get(index));
		}
		for(; index < buttons.size();index++){
			buttons.get(index).setDisable(true);
		}
	}
}