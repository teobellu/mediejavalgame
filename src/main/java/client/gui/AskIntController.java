package client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class AskIntController extends DialogAbstractController {

	@FXML
	private Button _okButton;
	
	@FXML
	private ChoiceBox<String> _choices;
	
	@FXML
	private TextFlow _textFlow;
	
	public void setup(String message, int min, int max){
		Text text = new Text(message);
		text.setFont(Font.font(24));
		_textFlow.getChildren().add(text);
		
		for(int i = min;i<max;i++){
			_choices.getItems().add(String.valueOf(i));
		}
	}
	
	@FXML
	private void onOkClicked(){
		GraphicalUI.getInstance().addFromGUIToGraphical(Integer.parseInt(_choices.getValue()));
		GraphicalUI.getInstance().notifyCommandToGui();
		_dialog.close();
	}
}
