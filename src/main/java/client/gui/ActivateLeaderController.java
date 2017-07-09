package client.gui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import model.LeaderCard;

/**
 * Controller for the activate leader dialog
 * @author Jacopo
 *
 */
public class ActivateLeaderController extends DialogAbstractController {

	@FXML
	private Button _okButton;
	@FXML
	private Button _cancelButton;
	
	@FXML
	private ChoiceBox<String> _choices;
	
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
		Runnable run = () -> GraphicalUI.getInstance().activateLeaderCard(_choices.getValue());
		new Thread(run).start();
		_dialog.close();
	}
	
	/**
	 * Initial setup
	 * @param leaderCards the leaders
	 */
	public void setLeaders(List<LeaderCard> leaderCards) {
		for(LeaderCard lc : leaderCards){
			_choices.getItems().add(lc.getName());		
		}
		_choices.getSelectionModel().selectFirst();
	}

}
