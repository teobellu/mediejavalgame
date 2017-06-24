package client.gui;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class DropLeaderController extends DialogAbstractController{

	@FXML
	private ChoiceBox<String> _choiceBox;
	
	@FXML
	private Button _okButton;
	
	private List<String> _leaderList;
	
	public void setLeaderList(List<String> leaders){
		_leaderList = leaders;
	}
	
	@FXML
	private void initialize(){
		_choiceBox.getItems().addAll(_leaderList);
	}
	
	@FXML
	private void onOkClicked(){
		try {
			GraphicalUI.getInstance().getConnection().dropWhichLeaderCard(_choiceBox.getValue());
			//TODO rimuovere la carta in maniera grafica, proprio
		} catch (RemoteException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	private Logger _log = Logger.getLogger(DropLeaderController.class.getName());
}
