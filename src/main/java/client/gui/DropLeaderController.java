package client.gui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class DropLeaderController extends DialogAbstractController{

	@FXML
	private ChoiceBox<String> _choiceBox;
	
	@FXML
	private Button _okButton;
	@FXML
	private Button _cancelButton;
	
	private List<String> _leaderList;
	
	public void setLeaderList(List<String> leaders){
		_leaderList = leaders;
		
		if(!_choiceBox.getItems().isEmpty()){
			for(String s : _choiceBox.getItems()){
				_choiceBox.getItems().remove(s);
			}
		}
		
		_choiceBox.getItems().addAll(_leaderList);
		_choiceBox.getSelectionModel().selectFirst();
	}
	
	@FXML
	private void onOkClicked(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				GraphicalUI.getInstance().dropLeaderCard(_choiceBox.getValue());
			}
		}).start();
		
		_dialog.close();
	}
	
	@FXML
	private void onCancelClicked(){
		_dialog.close();
	}
}
