package client.gui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class PlaceFamiliarController extends DialogAbstractController {

	@FXML
	private ChoiceBox<String> _choices;
	
	@FXML
	private Button _okButton;
	
	private GUI _GUI;
	
	public void setFamilyMembers(List<String> familyMembers) {
		_choices.getItems().addAll(familyMembers);
	}

	@FXML
	private void onOkPressed(){
		_GUI.showPlaceWhichFamiliar(GraphicalUI.getInstance().placeWhichFamiliar(_choices.getValue()));
	}
	
	public void setGUI(GUI gui){
		_GUI = gui;
	}
}
