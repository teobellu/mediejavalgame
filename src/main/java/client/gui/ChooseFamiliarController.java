package client.gui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import model.FamilyMember;

/**
 * Controller for the choose familiar dialog
 * @author Jacopo
 *
 */
public class ChooseFamiliarController extends DialogAbstractController{

	@FXML
	private Text _message;
	
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
