package client.userinterface;

import java.io.IOException;

import client.network.ConnectionServerHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GraphicalUI extends Application implements UI {
	
	

	@Override
	public ConnectionServerHandler getConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStringValue(String request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		_primaryStage = primaryStage;
		_primaryStage.setTitle("Lorenzo il Mgnifico");
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GraphicalUI.class.getResource(""));
			_rootLayout = loader.load();
			
			Scene scene = new Scene(_rootLayout);
            _primaryStage.setScene(scene);
            _primaryStage.show();

		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GraphicalUI.class.getResource(""));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            _rootLayout.setCenter(personOverview);
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

    private Stage _primaryStage;
    private BorderPane _rootLayout;
}
