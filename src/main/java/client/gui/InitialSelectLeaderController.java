package client.gui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

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
	private Button _card5;
	
	@FXML
	private Text _text;
	
	@FXML
	private void initialize(){
		
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
		
		if(leaders.get(3)!=null){
			setBgImages(leaders.get(3), _card4);
		} else {
			_card4.setDisable(true);
		}
		
		if(leaders.get(4)!=null){
			setBgImages(leaders.get(4), _card5);
		} else {
			_card5.setDisable(true);
		}
	}
	
	private void setBgImages(String leader, Button button){
		StringBuilder sb = new StringBuilder("src/main/resources/javafx/images/leaders/");
		sb.append(leader);
		sb.append(".jpg");
		StringBuilder sb1 = new StringBuilder("-fx-background-image: url('" + sb.toString() + "')");
		button.setStyle(sb1.toString());
	}
}
