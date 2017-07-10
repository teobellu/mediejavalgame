package client.gui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import model.FamilyMember;

/**
 * Controller for the choose familiar dialog
 *
 */
public class ChooseFamiliarController extends DialogAbstractController{

	/**
	 * The message to display
	 */
	@FXML
	private Text _message;
	
	/**
	 * ChoiceBox containing the list of familiars
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
	 * @param familiars the familiars 
	 * @param message the message asked
	 */
	public void setup(List<FamilyMember> familiars, String message) {
		_message.setText(message);
		
		for(FamilyMember fm : familiars){
			_choiches.getItems().add(fm.toString());
		}
		
		_choiches.getSelectionModel().selectFirst();
	}

}
