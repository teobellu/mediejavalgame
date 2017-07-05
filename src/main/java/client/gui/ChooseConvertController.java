package client.gui;

import java.util.List;

import game.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class ChooseConvertController extends DialogAbstractController {

	@FXML
	private ChoiceBox<String> _choiches;
	
	@FXML
	private Button _okButton;
	
	public void setup(List<Resource> pay, List<Resource> gain) {
		for(int i = 0;i<pay.size();i++){
			_choiches.getItems().add("Convert "+pay+" in "+gain);
		}
		
		_choiches.getSelectionModel().selectFirst();
	}
	
	@FXML
	private void onOkClicked(){
		GraphicalUI.getInstance().addFromGUIToGraphical(_choiches.getSelectionModel().getSelectedIndex());
		GraphicalUI.getInstance().notifyCommandToGui();
	}

}
