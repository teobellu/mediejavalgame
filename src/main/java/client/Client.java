package client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.network.ConnectionServerHandler;
import client.userinterface.UI;
import client.userinterface.UIFactory;
import util.Constants;
import util.IOHandler;

public class Client extends Thread {

	public Client() {
		_ioHandler = new IOHandler();
	}
	
	@Override
	public void run(){
		
		//Get UI
		_ioHandler.write("Vuoi giocare da Command Line Interface (CLI) o da Graphical User Interface (GUI)?");
		int i = 0;
		for(String ui : Constants.USER_INTERFACE_TYPES){
			_ioHandler.write(i+") "+ui);
			i++;
		}
		i--;
		_ui = UIFactory.getUserInterface(_ioHandler.readNumberWithinInterval(i));
		
		if (_ui == null) {
			_log.log(Level.SEVERE, "Can't get a UserInterface. What's goign on?");
			this.shutdown();
		}
		
		_connectionHandler = _ui.getConnection();
		
		if(_connectionHandler == null){
			_log.log(Level.SEVERE, "Can't create ConnectionServerHandler. What's going on?");
			this.shutdown();
		}
		
		_connectionHandler.setClient(this);
		
		_connectionHandler.run();
		
		try {
			doGame();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	private void doGame() throws RemoteException {
		/*TODO se primo player, chiedi se vuole mettere suo file config
		 * se s�, cerca il file
		 * se lo trovi, manda file al server
		 * */
		
		
		if(_connectionHandler.addMeToGame()){
			do {
				String path = _ui.askForConfigFile();
				if(!path.isEmpty()){
					try {
						FileReader customConfig = new FileReader(path);
						
						StringBuilder sb = new StringBuilder();
						BufferedReader br = new BufferedReader(customConfig);
						String line;
						while((line = br.readLine() ) != null) {
			                sb.append(line);
			            }
						
						br.close();
						
						_connectionHandler.sendConfigFile(sb.toString());
					} catch (FileNotFoundException e) {
						_ui.write("File not found.");
					} catch(IOException e){
						_ui.write("Invalid file. Using default settings...");
						break;
					}
				} else{
					break;
				}
			} while (true);
			//TODO send file
		}
		
		//TODO
	}
	
	private void shutdown() {
		_ioHandler.shutdown();
		_connectionHandler.shutdown();
	}

	private final IOHandler _ioHandler;
	private ConnectionServerHandler _connectionHandler;
	private Logger _log = Logger.getLogger(Client.class.getName());
	private UI _ui;
}
