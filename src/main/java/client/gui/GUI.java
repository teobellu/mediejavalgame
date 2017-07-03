package client.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.FamilyMember;
import game.GameBoard;
import game.LeaderCard;
import game.Player;
import game.Resource;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.CommandStrings;

public class GUI extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		//TODO settare dimensioni massime per stare dentro schermo 1440 * 900
		//TODO settare dimensioni massime per stare dentro schermo 1440 * 900
		//TODO settare dimensioni massime per stare dentro schermo 1440 * 900
		//TODO settare dimensioni massime per stare dentro schermo 1440 * 900
		
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
			_primaryStage.hide();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/MainView.fxml"));
			AnchorPane pane = loader.load();
			
			_primaryStage.setTitle(_primaryStage.getTitle()+" - "+GraphicalUI.getInstance().getPlayerName());
			
			_rootLayout.setCenter(pane);

			_primaryStage.setWidth(GuiSizeConstants.ROOT_WIDTH);
			_primaryStage.setMaxWidth(GuiSizeConstants.ROOT_WIDTH);
			_primaryStage.setMinWidth(GuiSizeConstants.ROOT_WIDTH);
			
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			
			System.out.println("###"+primaryScreenBounds.getHeight());
			
			_primaryStage.setHeight(primaryScreenBounds.getHeight()/100*80);
			_primaryStage.setMaxHeight(primaryScreenBounds.getHeight()/100*80);
			_primaryStage.setMinHeight(primaryScreenBounds.getHeight()/100*80);
			
			_mainViewController = loader.getController();
			_mainViewController.initialSetupController();
			_mainViewController.setGUI(this);
			
			_primaryStage.show();
			
			createMainObserver();
			
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void createMainObserver(){
		Task<Void> task = createReturnObjectObserver();
		
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				System.out.println("Dentro handle");
				String str = GraphicalUI.getInstance().getCommandToGui().poll();
				System.out.println("\n###RICEVUTO MESSAGGIO DA GUI: "+str+"###\n"); 
				processString(str);
				createMainObserver();
			}
			
			private void processString(String str){
				if(str.equals(CommandStrings.START_TURN)){
					startTurn();
				}
				else if(str.equals(CommandStrings.INITIAL_LEADER)){
					showInitialSelectLeaderDialog((List<String>) GraphicalUI.getInstance().getFirstFromGraphicalToGUI());
				}
				else if(str.equals(CommandStrings.INITIAL_PERSONAL_BONUS)){
					showPersonalBonusDialog((HashMap<String, List<Resource>>)GraphicalUI.getInstance().getFirstFromGraphicalToGUI());
				}
				else if(str.equals(CommandStrings.INFO)){
					_mainViewController.appendToInfoText((String) GraphicalUI.getInstance().getFirstFromGraphicalToGUI());
				}
				else if(str.equals(CommandStrings.INFO_BOARD)){
					_mainViewController.appendToInfoText((String) GraphicalUI.getInstance().getFirstFromGraphicalToGUI());
					_mainViewController.updateBoard((GameBoard) GraphicalUI.getInstance().getFirstFromGraphicalToGUI());
				}
				else if(str.equals(CommandStrings.ASK_BOOLEAN)){
					showAskBooleanDialog((String) GraphicalUI.getInstance().getFirstFromGraphicalToGUI());
				}
				else if(str.equals(CommandStrings.ASK_INT)){
					showAskIntDialog((String) GraphicalUI.getInstance().getFirstFromGraphicalToGUI(), 
							(int) GraphicalUI.getInstance().getFirstFromGraphicalToGUI(),(int) GraphicalUI.getInstance().getFirstFromGraphicalToGUI());
				}
				else if(str.equals(CommandStrings.HANDLE_COUNCIL)){
					showCouncilPrivilegeDialog((List<Resource>) GraphicalUI.getInstance().getFirstFromGraphicalToGUI());
				}
				else {//TODO
					System.out.println("\n###GUI HA RICEVUTO UN COMANDO SCONOSCIUTO###\n");
				}
			}
		});
		
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}
	
	private Task<Void> createReturnObjectObserver(){
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				
				System.out.println("\n##CREATED NEW TASK IN THREAD "+Thread.currentThread().getName()+"##\n");
				
				while (GraphicalUI.getInstance().getCommandToGui().isEmpty()) {
					try {
						System.out.println("Waiting for _commandToGui object...");
						Thread.sleep(1000);
					} catch (Exception e) {
					}
				}
				return null;
			}
		};
		return task;
	}
	
	public void showCouncilPrivilegeDialog(List<Resource> resources) {
		try {
			System.out.println("Chiamato showCouncilPrivilegeDialog");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/HandleCouncilDialog.fxml"));
			AnchorPane pane = loader.load();

			Stage dialog = setupDialog(pane, "Handle Council privilege");
			
			HandleCouncilController controller = loader.getController();
			controller.setDialog(dialog);
			controller.setResources(resources);

			dialog.showAndWait();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void startTurn(){
		GameBoard board = (GameBoard) GraphicalUI.getInstance().getFirstFromGraphicalToGUI();
		Player me = (Player) GraphicalUI.getInstance().getFirstFromGraphicalToGUI();
		_mainViewController.startTurn(me, board);
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

			Stage dialog = setupDialog(pane, "Choose your personal bonus card");

			PersonalBonusController controller = loader.getController();
			controller.setDialog(dialog);
			controller.setMap(hashMap);

			dialog.showAndWait();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
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
				controller.setDialog(dialog);
				controller.setLeaderList(leaders);
				
				dialog.showAndWait();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public void showCardsInfoDialog(Player me) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/CardsInfoDialog.fxml"));
			AnchorPane pane = loader.load();

			Stage dialog = setupDialog(pane, "Cards Summary");

			CardsInfoController controller = loader.getController();
			controller.setDialog(dialog);
			controller.setPlayer(me);
			
			dialog.showAndWait();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	public void showPlaceFamiliar(GameBoard board, List<FamilyMember> familiars){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/PlaceFamiliarDialog.fxml"));
			AnchorPane pane = loader.load();
			
			Stage dialog = setupDialog(pane, "Choose Familiar");
			
			PlaceFamiliarController controller = loader.getController();
			controller.setDialog(dialog);
			controller.setBoardAndFamiliars(board, familiars);
			
			dialog.showAndWait();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	public void showActivateLeaderDialog(List<LeaderCard> leaderCards){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/ActivateLeaderDialog.fxml"));
			AnchorPane pane = loader.load();
			
			Stage dialog = setupDialog(pane, "Choose Leader");
			
			ActivateLeaderController controller = loader.getController();
			controller.setDialog(dialog);
			controller.setLeaders(leaderCards);
			
			dialog.showAndWait();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	public void showAskBooleanDialog(String message){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/AskBooleanDialog.fxml"));
			AnchorPane pane = loader.load();
			
			Stage dialog = setupDialog(pane, "Choose yes or no");
			
			AskBooleanController controller = loader.getController();
			controller.setDialog(dialog);
			controller.setTextAndSetup(message);
			
			dialog.showAndWait();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	public void showAskIntDialog(String message, int min, int max){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/AskIntDialog.fxml"));
			AnchorPane pane = loader.load();
			
			Stage dialog = setupDialog(pane, "Choose a value");
			
			AskIntController controller = loader.getController();
			controller.setDialog(dialog);
			controller.setup(message, min, max);
			
			dialog.showAndWait();
		} catch (IOException e) {
			// TODO: handle exception
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
	
	private MainViewController _mainViewController;

	private Logger _log = Logger.getLogger(GUI.class.getName());
}
