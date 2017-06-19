package client.gui;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GUI extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		GraphicalUI.getInstance().setGUI(this);
		
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
	
	private void setStartingScene(){
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
	
	public void setMainScene(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/MainView.fxml"));
			AnchorPane pane = loader.load();
			
			double width = 1068;//TODO non hardcoded
			double height = 900;
			
			System.out.println(width + " e " + height);
			
			_rootLayout.setMinSize(width, height);
			_rootLayout.setMaxSize(width, height);
			_rootLayout.setPrefSize(width, height);
			
			_rootLayout.setCenter(pane);
			
			MainViewController controller = loader.getController();
			controller.setGUI(this);
			
		    Task<Integer> task = new Task<Integer>() {
		         @Override protected Integer call() throws Exception {
		             int iterations;
		             for (iterations = 0; iterations < 100000; iterations++) {
		                 if (isCancelled()) {
		                     break;
		                 }
		                 System.out.println("Iteration " + iterations);
		                 try {
		                     Thread.sleep(100);
		                 } catch (InterruptedException interrupted) {
		                     if (isCancelled()) {
		                         updateMessage("Cancelled");
		                         break;
		                     }
		                 }
		             }
		             return iterations;
		         }
		     };
		     
		     Thread th = new Thread(task);
		     th.setDaemon(true);
		     th.start();
		     
//			Thread th = new Thread(new Task<Void>() {
//				
//				@Override
//				protected Void call() throws Exception {
//					Platform.runLater(new Runnable() {
//						
//						@Override
//						public void run() {
//							while(GraphicalUI.getInstance().getTmpLeaderList()==null || GraphicalUI.getInstance().getTmpLeaderList().isEmpty()){
//								try {
//									System.out.println("Pause");
//									Thread.sleep(1000);
//								} catch (Exception e) {
//								}
//							}
//							showInitialSelectLeaderDialog(GraphicalUI.getInstance().getTmpLeaderList());
//						}
//					});
//					return null;
//				}
//			});
//	        th.setDaemon(true);
//	        th.start();

		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	public boolean showConfigDialog(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/ConfigDialog.fxml"));
			AnchorPane pane = loader.load();
			
			Stage dialog = new Stage();
			dialog.setTitle("Custom Config File");
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(_primaryStage);
			Scene scene = new Scene(pane);
			dialog.setScene(scene);
			
			CustomConfigController controller = loader.getController();
			controller.setDialogStage(dialog);
			
			dialog.showAndWait();
			return controller.isOkClicked();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			return false;
		}
	}
	
	public Stage getPrimaryStage(){
		return _primaryStage;
	}
	
	public void showDropLeaderDialog(List<String> leaders){
		try {
			if(leaders.isEmpty()){
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
				
				Stage dialog = new Stage();
				dialog.setTitle("Drop Leader Card");
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(_primaryStage);
				Scene scene = new Scene(pane);
				dialog.setScene(scene);
				
				DropLeaderController controller = loader.getController();
				controller.setLeaderList(leaders);
				
				dialog.showAndWait();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	public void showInitialSelectLeaderDialog(List<String> leaders){
		if(!leaders.isEmpty()){
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(GUI.class.getResource("/client/gui/InitialSelectLeaderDialog.fxml"));
				AnchorPane pane = loader.load();
				
				Stage dialog = new Stage();
				
				dialog.setTitle("Choose Leader Card");
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(_primaryStage);
				Scene scene = new Scene(pane);
				dialog.setScene(scene);
				
				InitialSelectLeaderController controller = loader.getController();
				controller.setLeaderList(leaders);
				
				dialog.showAndWait();
				
			} catch (IOException e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
			}
		} else {
			_log.log(Level.SEVERE, "List vuota in showInitialSelectLeaderDialog");
		}
	}
	
	public void showCardsInfoDialog() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GUI.class.getResource("/client/gui/CardsInfoDialog.fxml"));
			AnchorPane pane = loader.load();
			
			Stage dialog = new Stage();
			
			dialog.setTitle("Cards Summary");
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(_primaryStage);
			Scene scene = new Scene(pane);
			dialog.setScene(scene);
			
			CardsInfoController controller = loader.getController();
			controller.setDialogStage(dialog);
			
			dialog.showAndWait();
			
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private Stage _primaryStage;
    private BorderPane _rootLayout;
    
    private Logger _log = Logger.getLogger(GUI.class.getName());
}
