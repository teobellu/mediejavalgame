package client.gui;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PersonalBonusController {

	private Stage _dialog;
	
	public void setDialog(Stage dialog){
		_dialog = dialog;
	}
	
	@FXML
	private Button _bonus1;
	@FXML
	private Button _bonus2;
	@FXML
	private Button _bonus3;
	@FXML
	private Button _bonus4;
	@FXML
	private Button _bonus5;
	
	@FXML
	private ImageView _im00;
	@FXML
	private ImageView _im01;
	@FXML
	private ImageView _im02;
	@FXML
	private ImageView _im03;
	@FXML
	private ImageView _im04;
	@FXML
	private ImageView _im10;
	@FXML
	private ImageView _im11;
	@FXML
	private ImageView _im12;
	@FXML
	private ImageView _im13;
	@FXML
	private ImageView _im14;
	@FXML
	private ImageView _im20;
	@FXML
	private ImageView _im21;
	@FXML
	private ImageView _im22;
	@FXML
	private ImageView _im23;
	@FXML
	private ImageView _im24;
	@FXML
	private ImageView _im30;
	@FXML
	private ImageView _im31;
	@FXML
	private ImageView _im32;
	@FXML
	private ImageView _im33;
	@FXML
	private ImageView _im34;
	@FXML
	private ImageView _im40;
	@FXML
	private ImageView _im41;
	@FXML
	private ImageView _im42;
	@FXML
	private ImageView _im43;
	@FXML
	private ImageView _im44;

	
	private ArrayList<ImageView> _bonus1ImageViews = new ArrayList<>(Arrays.asList(_im00, _im01, _im02, _im03, _im04));
	private ArrayList<ImageView> _bonus2ImageViews = new ArrayList<>(Arrays.asList(_im10, _im11, _im12, _im13, _im14));
	private ArrayList<ImageView> _bonus3ImageViews = new ArrayList<>(Arrays.asList(_im20, _im21, _im22, _im23, _im24));
	private ArrayList<ImageView> _bonus4ImageViews = new ArrayList<>(Arrays.asList(_im30, _im31, _im32, _im33, _im34));
	private ArrayList<ImageView> _bonus5ImageViews = new ArrayList<>(Arrays.asList(_im40, _im41, _im42, _im43, _im44));
	
}
