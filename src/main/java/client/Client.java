package client;

import java.util.logging.Level;
import java.util.logging.Logger;

import client.network.ConnectionServerHandler;
import client.userinterface.UI;
import client.userinterface.UIFactory;
import util.Constants;
import util.IOHandler;
import util.packets.Packet;

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
		_connectionHandler.setClient(this);
		
		if(_connectionHandler == null){
			_log.log(Level.SEVERE, "Can't create ConnectionServerHandler. What's going on?");
			this.shutdown();
		}
		
		_connectionHandler.run();
		
		doGame();
	}
	
	public void processMessage(Packet message){
		
	}
	
	private void doGame(){
		/*TODO se primo player, chiedi se vuole mettere suo file config
		 * se sì, cerca il file
		 * se lo trovi, manda file al server
		 * */
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
