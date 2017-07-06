package client.gui;

import game.Resource;
import game.development.DevelopmentCard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class ChooseCostController extends DialogAbstractController {

	@FXML
	private ChoiceBox<String> _choiches;
	
	@FXML
	private Button _okButton;
	
	public void setCard(DevelopmentCard card) {
		for(Resource cost : card.getCosts()){
			_choiches.getItems().add(cost.toString());
		}
		_choiches.getSelectionModel().selectFirst();
	}
	
	@FXML
	private void onOkClicked(){
		GraphicalUI.getInstance().addFromGUIToGraphical(_choiches.getSelectionModel().getSelectedIndex());
		GraphicalUI.getInstance().notifyCommandToGui();
		_dialog.close();
	}

}
