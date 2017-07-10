package client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Controller for the ask int dialog
 *
 */
public class AskIntController extends DialogAbstractController {

	/**
	 * Ok Button
	 */
	@FXML
	private Button _okButton;
	
	/**
	 * Choicebox displaying the possible ints
	 */
	@FXML
	private ChoiceBox<String> _choices;
	
	/**
	 * Textflow containing the question
	 */
	@FXML
	private TextFlow _textFlow;
	
	/**
	 * Called on OK button clicked
	 */
	@FXML
	private void onOkClicked(){
		GraphicalUI.getInstance().addFromGUIToGraphical(Integer.parseInt(_choices.getValue()));
		GraphicalUI.getInstance().notifyCommandToGui();
		_dialog.close();
	}
	
	/**
	 * Initial setup
	 * @param message the message
	 * @param min minimum value
	 * @param max maximum value
	 */
	public void setup(String message, int min, int max){
		Text text = new Text(message);
		text.setFont(Font.font(24));
		_textFlow.getChildren().add(text);
		
		for(int i = min;i<=max;i++){
			_choices.getItems().add(String.valueOf(i));
		}
		_choices.getSelectionModel().selectFirst();
	}
}
