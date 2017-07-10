package client.gui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

/**
 * Controller for the drop leader dialog
 *
 */
public class DropLeaderController extends DialogAbstractController{

	@FXML
	private ChoiceBox<String> _choiceBox;
	
	@FXML
	private Button _okButton;
	@FXML
	private Button _cancelButton;
	
	private List<String> _leaderList;
	
	/**
	 * Called on Cancel button clicked
	 */
	@FXML
	private void onCancelClicked(){
		_dialog.close();
	}
	
	/**
	 * Called on OK button clicked
	 */
	@FXML
	private void onOkClicked(){
		Runnable run = () -> GraphicalUI.getInstance().dropLeaderCard(_choiceBox.getValue());
		new Thread(run).start();
		_dialog.close();
	}
	
	/**
	 * Initial setup
	 * @param leaders the leaders
	 */
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
}
