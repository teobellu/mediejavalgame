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

public class InitialSelectLeaderController extends DialogAbstractController{

	@FXML
	private Button _card1;
	@FXML
	private Button _card2;
	@FXML
	private Button _card3;
	@FXML
	private Button _card4;
	
	@FXML
	private Text _text;
	
	private GUI _GUI;
	
	@FXML
	private void initialize(){
		
	}
	
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
	
	
	@FXML
	private void onFirstButtonClicked(){
		handleButton(_card1);
	}
	
	@FXML
	private void onSecondButtonClicked(){
		handleButton(_card2);
	}
	
	@FXML
	private void onThirdButtonClicked(){
		handleButton(_card3);
	}
	
	@FXML
	private void onFifthButtonClicked(){
		handleButton(_card4);
	}
	
	private void handleButton(Button button){
		List<String> str = (List<String>) GraphicalUI.getInstance().getReturnObject();
		str.remove(button.getText());
		GraphicalUI.getInstance().setReturnObject(str);
		_GUI.createInitialLeaderObserver();
		_dialog.close();
	}
	
	private void setBgImages(String leader, Button button){
		File file = new File("src/main/resources/javafx/images/leaders/" + leader + ".jpg");
		Image image = new Image(file.toURI().toString());

		BackgroundSize backSize = new BackgroundSize(GuiSizeConstants.INITIAL_LEADER_WIDTH, GuiSizeConstants.INITIAL_LEADER_HEIGHT, false, false, false, true);
		BackgroundImage backImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backSize);
		button.setBackground(new Background(backImage));
		button.setDisable(false);
		button.setText(leader);
	}

	public void setGUI(GUI gui) {
		_GUI = gui;
	}
}