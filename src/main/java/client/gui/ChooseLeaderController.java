package client.gui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class ChooseLeaderController extends DialogAbstractController{

	@FXML
	private ChoiceBox<String> _choices;
	
	@FXML
	private Button _okButton;
	
	public void setLeaderList(List<String> leaders){
		for(String s : leaders){
			_choices.getItems().add(s);
		}
		
		_choices.getSelectionModel().selectFirst();
	}
	
	@FXML
	private void onOkClicked(){
		GraphicalUI.getInstance().addFromGUIToGraphical(_choices.getSelectionModel().getSelectedIndex());
		GraphicalUI.getInstance().notifyCommandToGui();
		_dialog.close();
	}
}
