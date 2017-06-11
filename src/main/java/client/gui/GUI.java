package client.gui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GUI extends Application{

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
			
			_rootLayout.setCenter(pane);
			MainViewController controller = loader.getController();
			controller.setGUI(this);
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
	
	public void showDropLeaderDialog(){
		try {
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
			controller.setDialogStage(dialog);
			
			dialog.showAndWait();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private Stage _primaryStage;
    private BorderPane _rootLayout;
    
    private Logger _log = Logger.getLogger(GUI.class.getName());
}
