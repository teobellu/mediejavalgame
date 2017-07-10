package client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import model.DevelopmentCard;
import model.Resource;

/**
 * Controller for the choose cost dialog
 *
 */
public class ChooseCostController extends DialogAbstractController {

	@FXML
	private ChoiceBox<String> _choiches;
	
	@FXML
	private Button _okButton;
	
	/**
	 * Called on OK button clicked
	 */
	@FXML
	private void onOkClicked(){
		GraphicalUI.getInstance().addFromGUIToGraphical(_choiches.getSelectionModel().getSelectedIndex());
		GraphicalUI.getInstance().notifyCommandToGui();
		_dialog.close();
	}
	
	/**
	 * Initial setup
	 * @param card the cards
	 */
	public void setCard(DevelopmentCard card) {
		for(Resource cost : card.getCosts()){
			_choiches.getItems().add(cost.toString());
		}
		_choiches.getSelectionModel().selectFirst();
	}

}
