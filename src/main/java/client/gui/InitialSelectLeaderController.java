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
import javafx.stage.Stage;
import util.Constants;

public class InitialSelectLeaderController {

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
	
	private Stage _dialog;
	private GUI _GUI;
	
	@FXML
	private void initialize(){
		
	}
	
	public void setDialog(Stage stage){
		_dialog = stage;
	}
	
	public void setLeaderList(List<String> leaders){
		
		ArrayList<Button> buttons = new ArrayList<>();
		buttons.add(_card1);
		buttons.add(_card2);
		buttons.add(_card3);
		buttons.add(_card4);
		
		int i = 0;
		
		for(;i<leaders.size();i++){
			setBgImages(leaders.get(i), buttons.get(i));
		}
		
		for(;i<Constants.LEADER_CARDS_PER_PLAYER;i++){
			buttons.get(i).setDisable(true);
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
		button.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(GuiSizeConstants.INITIAL_LEADER_WIDTH, GuiSizeConstants.INITAL_LEADER_HEIGHT, false, false, false, true))));
		button.setDisable(false);
		button.setText(leader);
	}

	public void setGUI(GUI gui) {
		_GUI = gui;
	}
}