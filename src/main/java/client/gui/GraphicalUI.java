package client.gui;

import java.util.logging.Logger;

import client.Client;
import client.cli.UI;
import client.network.ConnectionServerHandler;
import client.network.ConnectionServerHandlerFactory;
import javafx.application.Application;

public class GraphicalUI implements UI {
	
	private GraphicalUI() {
	}
	
	public static GraphicalUI getInstance(){
		if(_instance==null){
			synchronized (GraphicalUI.class) {
				if(_instance==null){
					_instance = new GraphicalUI();
				}
			}
		}
		return _instance;
	}
	
	public void start(){
		Application.launch(GUI.class, new String[0]);
	}

	@Override
	public void setConnection(String connectionType, String host, int port) {
		_connectionHandler = ConnectionServerHandlerFactory.getConnectionServerHandler(connectionType, host, port);
	}
	
	public ConnectionServerHandler getConnection(){
		return _connectionHandler;
	}
	
	@Override
	public String getStringValue(boolean isEmptyAllowed) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printString(String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String askForConfigFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(String str) {
		// TODO Auto-generated method stub
		
	}
	
	public void setName(String name){
		_name = name;
	}
	
	private static String _name;
	
	private static GraphicalUI _instance = null;
	
	private Client _client = null;
    
    private Logger _log = Logger.getLogger(GraphicalUI.class.getName());
    
    private ConnectionServerHandler _connectionHandler;
}
