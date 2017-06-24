package client.gui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class PlaceWhichFamiliarController extends DialogAbstractController {

	@FXML
	private ChoiceBox<String> _choices;
	
	@FXML
	private Button _okButton;
	
	public void setPositions(List<String> positions) {
		// TODO Auto-generated method stub
		
	}

}
