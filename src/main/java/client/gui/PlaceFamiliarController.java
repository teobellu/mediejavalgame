package client.gui;

import java.util.ArrayList;
import java.util.List;

import client.ClientText;
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
import model.FamilyMember;
import model.GC;
import model.GameBoard;
import model.Position;

/**
 * Controller for the place familiar dialog
 * @author Jacopo
 *
 */
public class PlaceFamiliarController extends DialogAbstractController {

	/**
	 * ChoiceBox containing the list of familiars
	 */
	@FXML
	private ChoiceBox<String> _familiarChoice;
	/**
	 * ChoiceBox containing the list of action spaces
	 */
	@FXML
	private ChoiceBox<String> _actionSpaceChoice;
	/**
	 * ChoiceBox containing the list of positions
	 */
	@FXML
	private ChoiceBox<String> _positionChoice;
	
	/**
	 * Ok Button
	 */
	@FXML
	private Button _okButton;
	/**
	 * Cancel Button
	 */
	@FXML
	private Button _cancelButton;
	
	/**
	 * GridPane to organize all labels and ChoiceBoxes
	 */
	@FXML
	private GridPane _gridPane;
	
	/**
	 * Possible Action spaces
	 */
	private List<String> _spaceTypes;
	
	/**
	 * Possible familiars
	 */
	private List<String> _familyColours;
	
	/**
	 * Create a position
	 * @param i user selection
	 * @return the position created
	 */
	private Position getPosition(int i){
		Position pos = null;
		
		
		if(_spaceTypes.get(i).equalsIgnoreCase(GC.MARKET)){
			int position = _positionChoice.getSelectionModel().getSelectedIndex();
			pos = new Position(GC.MARKET, position);
		} else if(_spaceTypes.get(i).equalsIgnoreCase(GC.TOWER)){
			int column = _positionChoice.getSelectionModel().getSelectedIndex()/4;
			int row = _positionChoice.getSelectionModel().getSelectedIndex()-(4*column);
			pos = new Position(GC.TOWER, row, column);
		} else {
			pos = new Position(GC.SPACE_TYPE.get(i));
		}
		
		return pos;
	}

	/**
	 * Called on Cancel Button pressed
	 */
	@FXML
	private void onCancelPressed(){
		_dialog.close();
	}
	
	/**
	 * Called on Ok Button pressed
	 */
	@FXML
	private void onOkPressed(){
		String s = _actionSpaceChoice.getValue();
		int i = _spaceTypes.indexOf(s);
		
		final Position pos = getPosition(i);
		
		Runnable run = () -> GraphicalUI.getInstance()
				.placeFamiliar(_familyColours.get(_familiarChoice.getSelectionModel().getSelectedIndex()), pos);
		new Thread(run).start();
		
		_dialog.close();
	}
	
	/**
	 * Initial setup
	 * @param board the board
	 * @param familiars the familiars
	 */
	public void setBoardAndFamiliars(GameBoard board, List<FamilyMember> familiars) {
		
		_familyColours = new ArrayList<>();
		for(FamilyMember fm : familiars){
			_familyColours.add(fm.getColor());
			_familiarChoice.getItems().add(GuiUtil.cleanUnderscoresCapsFirst(fm.getColor()) + " - " +fm.getValue());
		}
		
		_familiarChoice.getSelectionModel().selectFirst();
		
		_spaceTypes = new ArrayList<>();
		
		/*memo: space_type ha council palace, harvest, production, market, e tower*/
		for(String s : GC.SPACE_TYPE){
			String ss = GuiUtil.cleanUnderscoresCapsFirst(s);
			_spaceTypes.add(ss);
		}
		
		_actionSpaceChoice.getItems().addAll(_spaceTypes);
		
		_actionSpaceChoice.getSelectionModel().selectFirst();
		
		/*https://stackoverflow.com/a/14523434*/
		_actionSpaceChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(oldValue.matches(_spaceTypes.get(3)+"|"+_spaceTypes.get(4))){
					List<Node> nodes = new ArrayList<>();
					nodes.add(GuiUtil.getNodeFromGridPane(_gridPane, 0, 2));
					nodes.add(GuiUtil.getNodeFromGridPane(_gridPane, 0, 4));
					nodes.add(GuiUtil.getNodeFromGridPane(_gridPane, 0, 5));
					nodes.add(GuiUtil.getNodeFromGridPane(_gridPane, 1, 5));
					
					for(Node n : nodes){
						if(n!=null){
							_gridPane.getChildren().remove(n);
						}
					}
				}
				
				//if equals market
				if(newValue.equals(_spaceTypes.get(3))){
					Label label = new Label("Position:");
					label.setFont(Font.font(null, FontWeight.BOLD, 14));
					_gridPane.add(label, 0, 5);
					
					_positionChoice  = new ChoiceBox<>();
					
					for(int i = 0;i<GC.NUMBER_OF_MARKET_DISTRICTS; i++){
						_positionChoice.getItems().add(i, String.valueOf(i+1));
					}
					
					_gridPane.add(_positionChoice, 1, 5);
					_positionChoice.getSelectionModel().selectFirst();
				} //else if equals tower
				else if(newValue.equals(_spaceTypes.get(4))){
					Label label = new Label("Position:");
					label.setFont(Font.font(null, FontWeight.BOLD, 14));
					
					_positionChoice = new ChoiceBox<>();
					_positionChoice.getItems().addAll(ClientText.getTowerAndPositionsList());
					
					_gridPane.add(label, 0, 5);
					_gridPane.add(_positionChoice, 1, 5);
					_positionChoice.getSelectionModel().selectFirst();
				}
			}
		});
	}
}
