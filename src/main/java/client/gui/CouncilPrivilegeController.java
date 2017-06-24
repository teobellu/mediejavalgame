package client.gui;

import java.util.List;

import game.GC;
import game.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class CouncilPrivilegeController extends DialogAbstractController{

	@FXML
	private Button _okButton;
	
	@FXML
	private ChoiceBox<String> _choices;
	
	public void setResources(List<Resource> resources) {
		for(Resource r : resources){
			StringBuilder sb = new StringBuilder();
			
			for(String s : GC.RES_TYPES){
				int i = r.get(s);
				if(i > 0){
					if(sb.length()==0){
						sb.append("Converti in ");
					} else {
						sb.append(" e in");
					}
					sb.append(i+" "+s);
				}
			}
			
			_choices.getItems().add(sb.toString());
		}
	}

}
