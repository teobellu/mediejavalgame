package client;

import java.util.logging.Level;
import java.util.logging.Logger;

import util.Constants;
import util.IOHandler;

/**
 * Starting class for the client
 * @author Jacopo
 * @author Matteo
 *
 */
public class Client extends Thread {
	
	private final IOHandler ioHandler;
	private Logger log = Logger.getLogger(Client.class.getName());
	private UI ui;

	public Client() {
		ioHandler = new IOHandler();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run(){
		printLogo();
		
		//Get UI
		ioHandler.write("Select your User Interface");
		
		ioHandler.writeList(Constants.UI_TYPES);
	
		int selection = ioHandler.readNumberWithinInterval(Constants.UI_TYPES.size() - 1);
		ui = UIFactory.getUserInterface(selection);
		
		if (ui == null) {
			log.log(Level.SEVERE, "Can't get a UserInterface. What's goign on?");
			this.shutdown();
		}
		
		new Thread(ui).start();
	}
	
	/**
	 * Shutdown the client
	 */
	private void shutdown() {
		ioHandler.shutdown();
	}
	
	/**
	 * Prints the best awesome logo ever!
	 */
	private void printLogo(){
		ioHandler.write(" _    _____ ____  ____  ___   _  _____  _____	    _  _    ");
		ioHandler.write("| |  |  _  |    ||  __||   \\ | ||__   ||  _  |	   | || |   ");
		ioHandler.write("| |  | | | | [] ||  _] |    \\| |  /  / | | | |	   | || |   ");
		ioHandler.write("| |__| |_| |    || |__ | |\\    | /  /_ | |_| |	   | || |__ ");
		ioHandler.write("|____|_____|_|\\_\\|____||_| \\___||_____||_____|	   |_||____|");
		ioHandler.write(" __    __  _____  _____  ___   _  _  _____  _  _____  _____");
		ioHandler.write("|  \\  /  ||  _  ||  ___||   \\ | || ||  ___|| ||  ___||  _  |");
		ioHandler.write("|   \\/   || |_| || | __ |    \\| || ||  __| | || |    | | | |");
		ioHandler.write("| | \\/ | ||  _  || |_| || |\\    || || |	   | || |___ | |_| |");
		ioHandler.write("|_|    |_||_| |_||_____||_| \\___||_||_|    |_||_____||_____|\n");
	}
	
	
}
