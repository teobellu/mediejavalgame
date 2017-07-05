package client.gui;

import java.util.List;

import game.FamilyMember;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;

public class ChooseFamiliarController extends DialogAbstractController{

	@FXML
	private Text _message;
	
	@FXML
	private ChoiceBox<String> _choiches;
	
	@FXML
	private Button _okButton;
	
	public void setup(List<FamilyMember> familiars, String message) {
		_message.setText(message);
		
		for(FamilyMember fm : familiars){
			_choiches.getItems().add(fm.toString());
		}
		
		_choiches.getSelectionModel().selectFirst();
	}
	
	@FXML
	private void onOkClicked(){
		GraphicalUI.getInstance().addFromGUIToGraphical(_choiches.getSelectionModel().getSelectedIndex());
		GraphicalUI.getInstance().notifyCommandToGui();
	}

}
