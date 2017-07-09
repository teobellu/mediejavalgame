package client.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.FamilyMember;
import game.GameBoard;
import game.LeaderCard;
import game.Player;
import game.Resource;
import game.development.DevelopmentCard;
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
import javafx.stage.WindowEvent;
import util.CommandStrings;

/**
 * JavaFX main class
 * @author Jacopo
 *
 */
public class GUI extends Application {

	private Stage _primaryStage;

	private BorderPane _rootLayout;

	private MainViewController _mainViewController;
	
	private transient Logger _log = Logger.getLogger(GUI.class.getName());
	
	private ExecutorService executor = Executors.newCachedThreadPool();
	
	/**
	 * Create and start a task to receive input from the net
	 */
	@SuppressWarnings("unchecked")
	public void createMainObserver(){
		Task<Void> task = createReturnObjectObserver();
		
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				String str = GraphicalUI.getInstance().getCommandToGui().poll();
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
				else if(str.matches(CommandStrings.INFO_BOARD+"|"+CommandStrings.INFO_BOARD_PLAYER+"|"+CommandStrings.INFO_PLAYER)){
					_mainViewController.appendToInfoText((String) GraphicalUI.getInstance().getFirstFromGraphicalToGUI());
					_mainViewController.updateBoardAndPlayer();
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
				else if(str.equals(CommandStrings.CHOOSE_COST)){
					showChooseCost((DevelopmentCard)GraphicalUI.getInstance().getFirstFromGraphicalToGUI());
				}
				else if(str.equals(CommandStrings.CHOOSE_FAMILIAR)){
					showChooseFamiliar((List<FamilyMember>)GraphicalUI.getInstance().getFirstFromGraphicalToGUI(),(String) GraphicalUI.getInstance().getFirstFromGraphicalToGUI());
				}
				else if(str.equals(CommandStrings.CHOOSE_CONVERT)){
					showChooseConvert((List<Resource>)GraphicalUI.getInstance().getFirstFromGraphicalToGUI(),(List<Resource>) GraphicalUI.getInstance().getFirstFromGraphicalToGUI());
				}
				else if(str.equals(CommandStrings.CONNECTION_ERROR)){
					showDisconnectedDialog();
				}
				else {
					_log.info("\nGUI has received an unknown command\n");
				}
			}

		});
		
		executor.execute(task);
		
//		Thread th = new Thread(task);
//		th.setDaemon(true);
//		th.start();
	}
	
	/**
	 * Create a task
	 * @return the task created
	 */
	private Task<Void> createReturnObjectObserver(){
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				while (GraphicalUI.getInstance().getCommandToGui().isEmpty()) {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						_log.log(Level.FINE, e.getMessage(), e);
					}
				}
				return null;
			}
		};
		return task;
	}

	/**
	 * Get the primary stage
	 * @return the primary stage
	 */
	public Stage getPrimaryStage() {
		return _primaryStage;
	}
	
	/**
	 * Set the main scene
	 */
	public void setMainScene() {
		try {
			_primaryStage.hide();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/MainView.fxml"));
			AnchorPane pane = loader.load();
			
			_primaryStage.setTitle(_primaryStage.getTitle()+" - "+GraphicalUI.getInstance().getPlayerName());
			
			Scene scene = new Scene(pane);
			_primaryStage.setScene(scene);
			
			_primaryStage.setWidth(GuiSizeConstants.ROOT_WIDTH);
			_primaryStage.setMaxWidth(GuiSizeConstants.ROOT_WIDTH);
			_primaryStage.setMinWidth(GuiSizeConstants.ROOT_WIDTH);
			
			_primaryStage.setHeight(GuiSizeConstants.ROOT_HEIGHT);
			_primaryStage.setMinHeight(GuiSizeConstants.ROOT_HEIGHT);
			_primaryStage.setMaxHeight(GuiSizeConstants.ROOT_HEIGHT);
			
			_mainViewController = loader.getController();
			_mainViewController.initialSetupController();
			_mainViewController.setGUI(this);
			
			_primaryStage.show();
			
			createMainObserver();
			
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	/**
	 * Set the starting scene
	 */
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
	
	/**
	 * Setup a standard dialog
	 * @param pane the pane to put in
	 * @param title the title
	 * @return the dialog created
	 */
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
	
	/**
	 * Show the activate leader dialog
	 * @param leaderCards the cards
	 */
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

	/**
	 * Ask a yes/no question
	 * @param message the question
	 */
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

	/**
	 * Ask an int
	 * @param message the question
	 * @param min minimum int
	 * @param max maximum int
	 */
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
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	/**
	 * Show the info cards dialog
	 * @param me the player
	 */
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

	/**
	 * Ask if you want to convert
	 * @param pay what you pay
	 * @param gain what you gain
	 */
	protected void showChooseConvert(List<Resource> pay, List<Resource> gain) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/ChooseConvert.fxml"));
			AnchorPane pane = loader.load();
			
			Stage dialog = setupDialog(pane, "Choose convert");
			
			ChooseConvertController controller = loader.getController();
			controller.setDialog(dialog);
			controller.setup(pay, gain);
			
			dialog.showAndWait();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	/**
	 * Ask how you want to pay
	 * @param card the card
	 */
	private void showChooseCost(DevelopmentCard card) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/ChooseCost.fxml"));
			AnchorPane pane = loader.load();
			
			Stage dialog = setupDialog(pane, "Choose cost");
			
			ChooseCostController controller = loader.getController();
			controller.setDialog(dialog);
			controller.setCard(card);
			
			dialog.showAndWait();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	/**
	 * Show the choose familiar dialog
	 * @param familiars the familiars
	 * @param message message to display
	 */
	protected void showChooseFamiliar(List<FamilyMember> familiars, String message) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/ChooseFamiliar.fxml"));
			AnchorPane pane = loader.load();
			
			Stage dialog = setupDialog(pane, "Choose Familiar");
			
			ChooseFamiliarController controller = loader.getController();
			controller.setDialog(dialog);
			controller.setup(familiars, message);
			
			dialog.showAndWait();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	/**
	 * Show the custom config Dialog
	 * @return
	 */
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
	
	/**
	 * Show the handle council privilege dialog
	 * @param resources
	 */
	public void showCouncilPrivilegeDialog(List<Resource> resources) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/HandleCouncilDialog.fxml"));
			AnchorPane pane = loader.load();

			Stage dialog = setupDialog(pane, "Handle Council privilege");
			
			HandleCouncilController controller = loader.getController();
			controller.setDialog(dialog);
			controller.setResources(resources);

			dialog.showAndWait();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	/**
	 * Show the disconnected dialog
	 */
	private void showDisconnectedDialog() {
		try {
			//TODO provare a chiudere eventuali dialog aperti
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/DisconnectedDialog.fxml"));
			AnchorPane pane = loader.load();
			
			Stage dialog = setupDialog(pane, "Connection lost");
			
			DisconnectedController controller = loader.getController();
			controller.setDialog(dialog);
			
			dialog.showAndWait();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	
	/**
	 * Show the drop leader dialog
	 * @param leaders the leaders
	 */
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
	
	/**
	 * Show the initial leader dialog
	 * @param leaders
	 */
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
	/**
	 * Show the initial personal bonus dialog
	 * @param hashMap
	 */
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
	
	/**
	 * Show the place familiar dialog
	 * @param board the board
	 * @param familiars the familiars
	 */
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

	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) {
		
		_primaryStage = primaryStage;
		_primaryStage.setTitle("Lorenzo il Magnifico");
		_primaryStage.setResizable(false);
		_primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				System.out.println("Bye!");
				executor.shutdownNow();
				GraphicalUI.getInstance().shutdown();
				System.exit(0);
			}
		});

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
	
	/**
	 * Setup the starting turn phase
	 */
	public void startTurn(){
		GameBoard board = (GameBoard) GraphicalUI.getInstance().getFirstFromGraphicalToGUI();
		Player me = (Player) GraphicalUI.getInstance().getFirstFromGraphicalToGUI();
		_mainViewController.startTurn(me, board);
	}
}
