package client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class AskBooleanController extends DialogAbstractController {

	@FXML
	private TextFlow _textFlow;
	
	@FXML
	private Button _okButton;
	
	@FXML
	private RadioButton _yesRadio;
	@FXML
	private RadioButton _noRadio;
	
	private ToggleGroup _toggleGroup;
	
	public void setTextAndSetup(String question){
		Text text = new Text(question);
		text.setFont(Font.font(18));
		_textFlow.getChildren().add(text);
		
		_toggleGroup = new ToggleGroup();
		
		_yesRadio.setToggleGroup(_toggleGroup);
		_yesRadio.setSelected(true);
		
		_noRadio.setToggleGroup(_toggleGroup);
	}
	
	@FXML
	private void onOkPressed(){
		GraphicalUI.getInstance().addFromGUIToGraphical(_toggleGroup.getSelectedToggle().equals(_yesRadio));
		GraphicalUI.getInstance().notifyCommandToGui();
		_dialog.close();
	}
}
