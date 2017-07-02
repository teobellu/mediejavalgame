package client.gui;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import client.ClientText;
import game.FamilyMember;
import game.GC;
import game.GameBoard;
import game.Position;
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

public class PlaceFamiliarController extends DialogAbstractController {

	@FXML
	private ChoiceBox<String> _familiarChoice;
	@FXML
	private ChoiceBox<String> _actionSpaceChoice;
	@FXML
	private ChoiceBox<String> _positionChoice;
	
	@FXML
	private Button _okButton;
	@FXML
	private Button _cancelButton;
	
	@FXML
	private GridPane _gridPane;
	
	private GUI _GUI;
	
	private GameBoard _board;
	
	private List<FamilyMember> _familiars;
	
	private List<String> _spaceTypes;
	
	public void setBoardAndFamiliars(GameBoard board, List<FamilyMember> familiars) {
		_board = board;
		_familiars = familiars;
		
		for(FamilyMember fm : familiars){
			_familiarChoice.getItems().add(fm.getColor() + " - " +fm.getValue());
		}
		
		_familiarChoice.getSelectionModel().selectFirst();
		
		_spaceTypes = new ArrayList<>();
		
		for(String s : GC.SPACE_TYPE){
			String ss = s;
			
			ss.replace("_", " ");
			ss = capsFirst(s);
			_spaceTypes.add(s);
		}
		
		_spaceTypes.add(0, _spaceTypes.get(1));
		
		_actionSpaceChoice.getItems().addAll(_spaceTypes);
		
		_actionSpaceChoice.getSelectionModel().selectFirst();
		
		/*https://stackoverflow.com/a/14523434*/
		_actionSpaceChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(oldValue.matches(_spaceTypes.get(0)+"|"+_spaceTypes.get(4))){
					List<Node> nodes = new ArrayList<>();
					nodes.add(getNodeFromGridPane(_gridPane, 0, 2));
					nodes.add(getNodeFromGridPane(_gridPane, 0, 4));
					nodes.add(getNodeFromGridPane(_gridPane, 0, 5));
					nodes.add(getNodeFromGridPane(_gridPane, 1, 5));
					
					for(Node n : nodes){
						if(n!=null){
							_gridPane.getChildren().remove(n);
						}
					}
				}
				
				if(newValue.equals(_spaceTypes.get(0))){
					Label label = new Label("Position:");
					label.setFont(Font.font(null, FontWeight.BOLD, 14));
					_gridPane.add(label, 0, 5);
					
					_positionChoice  = new ChoiceBox<>();
					
					for(int i = 0;i<GC.NUMBER_OF_MARKET_DISTRICTS; i++){
						_positionChoice.getItems().add(i, String.valueOf(i+1));
					}
					
					_gridPane.add(_positionChoice, 1, 5);
				} else if(newValue.equals(_spaceTypes.get(4))){
					Label label = new Label("Position:");
					label.setFont(Font.font(null, FontWeight.BOLD, 14));
					
					_positionChoice = new ChoiceBox<>();
					_positionChoice.getItems().addAll(ClientText.getTowerAndPositionsList());
					
					_gridPane.add(label, 0, 5);
					_gridPane.add(_positionChoice, 1, 5);
				}
			}
		});
	}
	
	private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
	    for (Node node : gridPane.getChildren()) {
	    	
	    	if(GridPane.getColumnIndex(node)==null){
	    		System.out.println("\n###TROVATO UN NODO CON COLONNA A NULL###\n");
	    		GridPane.setColumnIndex(node, 0);
	    	}
	    	
	    	if(GridPane.getRowIndex(node)==null){
	    		System.out.println("\n###TROVATO UN NODO CON RIGA A NULL###\n");
	    		GridPane.setRowIndex(node, 0);
	    	}
	    	
	        if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
	            return node;
	        }
	    }
	    return null;
	}

	@FXML
	private void onOkPressed(){
		String s = _actionSpaceChoice.getValue();
		int i = _spaceTypes.indexOf(s);
		
		Position pos = null;
		if(_spaceTypes.get(i).equals(GC.MARKET)){
			int position = _positionChoice.getSelectionModel().getSelectedIndex();
			pos = new Position(GC.SPACE_TYPE.get(i), position);
		} else if(_spaceTypes.get(i).equals(GC.TOWER)){
			//TODO
		} else {
			pos = new Position(GC.SPACE_TYPE.get(i));
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					GraphicalUI.getInstance().placeFamiliar(_familiarChoice.getValue(), pos);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		_dialog.close();
	}
	
	@FXML
	private void onCancelPressed(){
		_dialog.close();
	}
	
	public void setGUI(GUI gui){
		_GUI = gui;
	}
	
	private String capsFirst(String str) {
	    String[] words = str.split(" ");
	    StringBuilder ret = new StringBuilder();
	    for(int i = 0; i < words.length; i++) {
	        ret.append(Character.toUpperCase(words[i].charAt(0)));
	        ret.append(words[i].substring(1));
	        if(i < words.length - 1) {
	            ret.append(' ');
	        }
	    }
	    return ret.toString();
	}
}
