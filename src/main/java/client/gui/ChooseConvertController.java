package client.gui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import model.Resource;

/**
 * Controller for the choose convert dialog
 * @author Jacopo
 *
 */
public class ChooseConvertController extends DialogAbstractController {

	/**
	 * ChoiceBox containing the possible conversions
	 */
	@FXML
	private ChoiceBox<String> _choiches;
	
	/**
	 * Ok Button
	 */
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
	 * @param pay what you'll pay
	 * @param gain what you'll gain
	 */
	public void setup(List<Resource> pay, List<Resource> gain) {
		for(int i = 0;i<pay.size();i++){
			_choiches.getItems().add("Convert "+pay.get(i)+" in "+gain.get(i));
		}
		_choiches.getSelectionModel().selectFirst();
	}

}
