package client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Controller for the ask boolean dialog
 *
 */
public class AskBooleanController extends DialogAbstractController {

	/**
	 * The textflow displaying the question
	 */
	@FXML
	private TextFlow _textFlow;
	
	/**
	 * Ok Button
	 */
	@FXML
	private Button _okButton;
	
	/**
	 * Answer yes
	 */
	@FXML
	private RadioButton _yesRadio;
	/**
	 * Answer no
	 */
	@FXML
	private RadioButton _noRadio;
	
	/**
	 * ToggleGroup containing the buttons
	 */
	private ToggleGroup _toggleGroup;
	
	/**
	 * Called on OK button clicked
	 */
	@FXML
	private void onOkPressed(){
		GraphicalUI.getInstance().addFromGUIToGraphical(_toggleGroup.getSelectedToggle().equals(_yesRadio));
		GraphicalUI.getInstance().notifyCommandToGui();
		_dialog.close();
	}
	
	/**
	 * Initial setup
	 * @param question the question asked
	 */
	public void setTextAndSetup(String question){
		Text text = new Text(question);
		text.setFont(Font.font(18));
		_textFlow.getChildren().add(text);
		
		_toggleGroup = new ToggleGroup();
		
		_yesRadio.setToggleGroup(_toggleGroup);
		_yesRadio.setSelected(true);
		
		_noRadio.setToggleGroup(_toggleGroup);
	}
}
