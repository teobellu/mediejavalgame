package client;

import java.util.logging.Level;
import java.util.logging.Logger;

import util.Constants;
import util.IOHandler;

public class Client extends Thread {

	public Client() {
		_ioHandler = new IOHandler();
	}
	
	@Override
	public void run(){
		//Get UI
		_ioHandler.write("Select your User Interface");
		
		_ioHandler.writeList(Constants.UI_TYPES);
	
		int selection = _ioHandler.readNumberWithinInterval(Constants.UI_TYPES.size() - 1);
		_ui = UIFactory.getUserInterface(selection);
		
		if (_ui == null) {
			_log.log(Level.SEVERE, "Can't get a UserInterface. What's goign on?");
			this.shutdown();
		}
		
		new Thread(_ui).start();
	}
	
	private void shutdown() {
		_ioHandler.shutdown();
	}
	
	private final IOHandler _ioHandler;
	private Logger _log = Logger.getLogger(Client.class.getName());
	private UI _ui;
}
