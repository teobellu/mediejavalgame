package client.gui;

import java.io.File;
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
	
	@FXML
	private void initialize(){
		
	}
	
	public void setDialog(Stage stage){
		_dialog = stage;
	}
	
	public void setLeaderList(List<String> leaders){
		
		
		
		if(leaders.get(0)!=null){
			setBgImages(leaders.get(0), _card1);
		} else {
			_card1.setDisable(true);
		}
		
		if(leaders.get(1)!=null){
			setBgImages(leaders.get(1), _card2);
		} else {
			_card2.setDisable(true);
		}
		
		if(leaders.get(2)!=null){
			setBgImages(leaders.get(2), _card3);
		} else {
			_card3.setDisable(true);
		}
		
		if(leaders.size() == 3)//TODO, idea, array di bottoni e non _card1, _card2...
		{
			_card4.setDisable(true);
			return;
		}
		
		if(leaders.get(3)!=null){
			setBgImages(leaders.get(3), _card4);
		} else {
			_card4.setDisable(true);
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
		_dialog.close();
	}
	
	private void setBgImages(String leader, Button button){
		File file = new File("src/main/resources/javafx/images/leaders/" + leader + ".jpg");
		Image image = new Image(file.toURI().toString());
		button.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(GuiSizeConstants.INITIAL_LEADER_WIDTH, GuiSizeConstants.INITAL_LEADER_HEIGHT, false, false, false, true))));
		//button.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(arg0, arg1, arg2, arg3, arg4, arg5))));
		button.setDisable(false);
		button.setText(leader);
	}
}