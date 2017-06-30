package client.gui;

import java.util.List;

import game.FamilyMember;
import game.GC;
import game.GameBoard;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PlaceFamiliarController extends DialogAbstractController {

	@FXML
	private ChoiceBox<String> _familiarChoice;
	@FXML
	private ChoiceBox<String> _actionSpaceChoice;
	
	@FXML
	private Button _okButton;
	@FXML
	private Button _cancelButton;
	
	@FXML
	private GridPane _gridPane;
	
	private GUI _GUI;
	
	private GameBoard _board;
	
	private List<FamilyMember> _familiars; 
	
	public void setBoardAndFamiliars(GameBoard board, List<FamilyMember> familiars) {
		_board = board;
		_familiars = familiars;
		
		for(FamilyMember fm : familiars){
			_familiarChoice.getItems().add(fm.getColor() + " - " +fm.getValue());
		}
		
		_actionSpaceChoice.getItems().addAll(GC.SPACE_TYPE);
		
		/*https://stackoverflow.com/a/14523434*/
		_actionSpaceChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(oldValue.matches(GC.MARKET+"|"+GC.TOWER)){
					//TODO rimuovere scritte aggiuntive
				}
				
				if(newValue.equals(GC.MARKET)){
					Label label = new Label("District:");
					label.setFont(Font.font(null, FontWeight.BOLD, 14));
					_gridPane.add(label, 0, 5);
					
					
				} else if(newValue.equals(GC.TOWER)){
					
				}
			}
		});
	}
	
//	private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
//	    for (Node node : gridPane.getChildren()) {
//	        if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
//	            return node;
//	        }
//	    }
//	    return null;
//	}

	@FXML
	private void onOkPressed(){
		//TODO
	}
	
	@FXML
	private void onCancelPressed(){
		_dialog.close();
	}
	
	public void setGUI(GUI gui){
		_GUI = gui;
	}
}
