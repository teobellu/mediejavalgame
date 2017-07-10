package client.gui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

/**
 * @author Jacopo
 * @author Matteo
 */
public class ChooseLeaderController extends DialogAbstractController{

	/**
	 * ChoiceBox containing the list of leaders
	 */
	@FXML
	private ChoiceBox<String> _choices;
	
	/**
	 * Ok Button
	 */
	@FXML
	private Button _okButton;
	
	/**
	 * Initial setup
	 * @param leaders leaders list
	 */
	public void setLeaderList(List<String> leaders){
		for(String s : leaders){
			_choices.getItems().add(s);
		}
		
		_choices.getSelectionModel().selectFirst();
	}
	
	/**
	 * Called on ok button clicked
	 */
	@FXML
	private void onOkClicked(){
		GraphicalUI.getInstance().addFromGUIToGraphical(_choices.getSelectionModel().getSelectedIndex());
		GraphicalUI.getInstance().notifyCommandToGui();
		_dialog.close();
	}
}
