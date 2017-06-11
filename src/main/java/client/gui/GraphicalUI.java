package client.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.Client;
import client.UI;
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
	
	@Override
	public void run(){
		Application.launch(GUI.class, new String[0]);
	}

	@Override
	public void setConnection(String connectionType, String host, int port) {
		_connectionHandler = ConnectionServerHandlerFactory.getConnectionServerHandler(connectionType, host, port);
		
		new Thread(_connectionHandler).start();
		
		if(_connectionHandler.isRunning()){
			sendXML();
		}
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
	
	public void prepareXmlFile(File file){
		_xmlFile = file;
	}
	
	private void sendXML(){
		FileReader customConfig = null;
		try {
			if(_xmlFile!=null){
				customConfig = new FileReader(_xmlFile);
				
				StringBuilder sb = new StringBuilder();
				BufferedReader br = new BufferedReader(customConfig);
				String line;
				while((line = br.readLine() ) != null) {
				    sb.append(line);
				}
				
				br.close();
				_connectionHandler.sendConfigFile(sb.toString());
			} else {
				_connectionHandler.sendConfigFile("");
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		finally{
			if (customConfig != null)
				try {
					customConfig.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	private File _xmlFile;
	
	private String _name;
	
	private static GraphicalUI _instance = null;
	
	private Client _client = null;
    
    private Logger _log = Logger.getLogger(GraphicalUI.class.getName());
    
    private ConnectionServerHandler _connectionHandler = null;
}
