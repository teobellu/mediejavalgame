package client.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.GameBoard;
import game.Resource;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.CommandStrings;
import util.Constants;

public class GUI extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		_primaryStage = primaryStage;
		_primaryStage.setTitle("Lorenzo il Magnifico");
		_primaryStage.setResizable(false);

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/RootLayout.fxml"));
			_rootLayout = loader.load();

			Scene scene = new Scene(_rootLayout);

			_primaryStage.setScene(scene);
			_primaryStage.show();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}

		setStartingScene();
	}

	private void setStartingScene() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/StartingView.fxml"));
			AnchorPane pane = loader.load();

			_rootLayout.setCenter(pane);

			StartingViewController controller = loader.getController();
			controller.setGUI(this);

		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public void setMainScene() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/MainView.fxml"));
			AnchorPane pane = loader.load();
			
			_rootLayout.setMinSize(GuiSizeConstants.ROOT_WIDTH, GuiSizeConstants.ROOT_HEIGHT);
			_rootLayout.setMaxSize(GuiSizeConstants.ROOT_WIDTH, GuiSizeConstants.ROOT_HEIGHT);
			_rootLayout.setPrefSize(GuiSizeConstants.ROOT_WIDTH, GuiSizeConstants.ROOT_HEIGHT);
			
			_primaryStage.setTitle(_primaryStage.getTitle()+" - "+GraphicalUI.getInstance().getPlayerName());
			
			_rootLayout.setCenter(pane);

			_mainViewController = loader.getController();
			_mainViewController.setGUI(this);

			createSetupGameObserver();
			
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void createSetupGameObserver(){
		Task<Void> task = createReturnObjectObserver();
		
		if(_counter<Constants.LEADER_CARDS_PER_PLAYER){
			System.out.println("Creo listener per leader card");
	
			task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					showInitialSelectLeaderDialog((List<String>) GraphicalUI.getInstance().getReturnObject());
				}
			});
	
			_counter++;
		} else {
			System.out.println("Creo listener per personal bonus");
			_counter = 0;
			
			task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					showPersonalBonusDialog((HashMap<String, List<Resource>>)GraphicalUI.getInstance().getReturnObject());
				}
				
			});
		}
		
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}
	
	@SuppressWarnings("unchecked")
	public void createMainObserver(){
		Task<Void> task = createReturnObjectObserver();
		
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				processObject();
			}
			
			private void processObject(){
				Object obj = GraphicalUI.getInstance().getReturnObject();
				if(obj instanceof String){
					processString((String) obj);
				} else if(obj instanceof List<?>){
					if(((List) obj).get(0) instanceof Resource){
						showCouncilPrivilegeDialog((List<Resource>)obj);
					}
				}
			}
			
			private void processString(String str){
				if(str.equals(CommandStrings.START_TURN)){
					startTurn();
				} else if(/*TODO*/) {
					
				}
			}
		});
	}
	
	public void showCouncilPrivilegeDialog(List<Resource> resources) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("client/gui/CouncilPrivilegeDialog.fxml"));
			AnchorPane pane = loader.load();
			
			Stage dialog = setupDialog(pane, "Handle Council privilege");
			
			CouncilPrivilegeController controller = loader.getController();
			controller.setDialog(dialog);
			controller.setResources(resources);
			
			dialog.showAndWait();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void startTurn(){
		GameBoard gb = GraphicalUI.getInstance().getBoard();
		//TODO
	}
	
	public void showInitialSelectLeaderDialog(List<String> leaders) {
		if (!leaders.isEmpty()) {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(GUI.class.getResource("/client/gui/InitialSelectLeaderDialog.fxml"));
				AnchorPane pane = loader.load();

				Stage dialog = setupDialog(pane, "Choose Leader Card");
				
				InitialSelectLeaderController controller = loader.getController();
				controller.setLeaderList(leaders);
				controller.setDialog(dialog);
				controller.setGUI(this);

				dialog.showAndWait();
			} catch (IOException e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
			}
		} else {
			_log.log(Level.SEVERE, "List vuota in showInitialSelectLeaderDialog");
		}
	}
	
	public void showPersonalBonusDialog(HashMap<String, List<Resource>> hashMap) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/PersonalBonusDialog.fxml"));
			AnchorPane pane = loader.load();

			Stage dialog = new Stage();

			dialog.initStyle(StageStyle.UNDECORATED);
			dialog.setTitle("Choose your personal bonus card");
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(_primaryStage);
			Scene scene = new Scene(pane);
			dialog.setScene(scene);

			PersonalBonusController controller = loader.getController();
			controller.setDialog(dialog);
			controller.setMap(hashMap);

			dialog.showAndWait();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private Task<Void> createReturnObjectObserver(){
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				Thread.sleep(4000);
				
				while (GraphicalUI.getInstance().getReturnObject() == null) {
					try {
						System.out.println("Waiting for return object...");
						Thread.sleep(1000);
					} catch (Exception e) {
					}
				}
				return null;
			}
		};
		return task;
	}

	public boolean showConfigDialog() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/ConfigDialog.fxml"));
			AnchorPane pane = loader.load();

			Stage dialog = setupDialog(pane, "Custom Config File");

			CustomConfigController controller = loader.getController();
			controller.setDialog(dialog);

			dialog.showAndWait();
			return controller.isOkClicked();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			return false;
		}
	}

	public Stage getPrimaryStage() {
		return _primaryStage;
	}

	public void showDropLeaderDialog(List<String> leaders) {
		try {
			if (leaders.isEmpty()) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.initOwner(_primaryStage);
				alert.setTitle("No Leaders");
				alert.setHeaderText("No leader card available");
				alert.setContentText("You cannot do this. You don't have any leader card");
				alert.showAndWait();
			} else {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(GUI.class.getResource("/client/gui/DropLeaderDialog.fxml"));
				AnchorPane pane = loader.load();

				Stage dialog = setupDialog(pane, "Drop Leader Card");

				DropLeaderController controller = loader.getController();
				controller.setLeaderList(leaders);

				dialog.showAndWait();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public void showCardsInfoDialog() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/CardsInfoDialog.fxml"));
			AnchorPane pane = loader.load();

			Stage dialog = setupDialog(pane, "Cards Summary");

			CardsInfoController controller = loader.getController();
			controller.setDialog(dialog);

			dialog.showAndWait();

		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	public void showPlaceFamiliar(List<String> familyMembers){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/PlaceFamiliarDialog.fxml"));
			AnchorPane pane = loader.load();
			
			Stage dialog = setupDialog(pane, "Choose Familiar");
			
			PlaceFamiliarController controller = loader.getController();
			controller.setDialog(dialog);
			controller.setGUI(this);
			controller.setFamilyMembers(familyMembers);
			
			dialog.showAndWait();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	public void showPlaceWhichFamiliar(List<String> positions) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/PlaceWhichFamiliarDialog.fxml"));
			AnchorPane pane = loader.load();
			
			Stage dialog = setupDialog(pane, "Choose Position");
			
			PlaceWhichFamiliarController controller = loader.getController();
			controller.setDialog(dialog);
			controller.setPositions(positions);
			
			dialog.showAndWait();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	private Stage setupDialog(AnchorPane pane, String title){
		Stage dialog = new Stage();
		
		dialog.setTitle(title);
		dialog.initStyle(StageStyle.UNDECORATED);
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(_primaryStage);
		Scene scene = new Scene(pane);
		dialog.setScene(scene);
		
		return dialog;
	}

	private Stage _primaryStage;
	private BorderPane _rootLayout;
	
	private int _counter = 0;
	
	private MainViewController _mainViewController;

	private Logger _log = Logger.getLogger(GUI.class.getName());
}
