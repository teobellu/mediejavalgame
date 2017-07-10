package client.gui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import model.GC;
import model.Resource;

/**
 * Controller for the handle council dialog
 * @author Jacopo
 *
 */
public class HandleCouncilController extends DialogAbstractController{

	/**
	 * Ok Button
	 */
	@FXML
	private Button _okButton;
	
	/**
	 * ChoiceBox containing the list of council conversion
	 */
	@FXML
	private ChoiceBox<String> _choices;
	
	/**
	 * Called on Ok Button pressed
	 */
	@FXML
	private void onOkClicked(){
		GraphicalUI.getInstance().addFromGUIToGraphical(_choices.getSelectionModel().getSelectedIndex());
		GraphicalUI.getInstance().notifyCommandToGui();
		_dialog.close();
	}

	/**
	 * Initial setup
	 * @param resources list of resources
	 */
	public void setResources(List<Resource> resources) {
		for(Resource r : resources){
			StringBuilder sb = new StringBuilder();
			
			for(String s : GC.RES_TYPES){
				int i = r.get(s);
				if(i > 0){
					if(sb.length()==0){
						sb.append("Trade as ");
					} else {
						sb.append(" and ");
					}
					sb.append(i+" "+GuiUtil.cleanUnderscoresCapsFirst(s));
				}
			}
			
			_choices.getItems().add(sb.toString());
		}
		_choices.getSelectionModel().selectFirst();
	}
}
