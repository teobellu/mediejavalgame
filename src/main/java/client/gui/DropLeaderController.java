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

	/**
	 * ChoiceBox containing the leaders
	 */
	@FXML
	private ChoiceBox<String> _choiceBox;
	
	/**
	 * Ok Button
	 */
	@FXML
	private Button _okButton;
	/**
	 * Cancel Button
	 */
	@FXML
	private Button _cancelButton;
	
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
		_choiceBox.getItems().clear();
		
		_choiceBox.getItems().addAll(leaders);
		_choiceBox.getSelectionModel().selectFirst();
	}
}
