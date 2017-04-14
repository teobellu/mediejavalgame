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
		
		//Get username
		_ioHandler.write("Ciao. Come vorresti chiamarti?");
		String username = _ioHandler.readLine();
		
		//Get UI
		_ioHandler.write("Vuoi giocare da Command Line Interface (CLI) o da Graphical User Interface (GUI)?");
		//TODO creazione GUI
				
		//Get server address
		_ioHandler.write("Inserisci l'indirizzo del server a cui vorresti collegarti.");
		String host = _ioHandler.readLine();
		
		//Get server's port
		_ioHandler.write("Su quale porta vorresti collegarti?");
		int port = _ioHandler.readNumber();
		
		//Choose connection type
		_ioHandler.write("Come vorresti connetterti?");
		int i = 0;
		for(String ct : Constants.CONNECTION_TYPES){
			_ioHandler.write(i+") "+ct);
			i++;
		}
		i--;
		int selected = _ioHandler.readNumberWithinInterval(i);
		_connectionServerHandler = ConnectionServerHandlerFactory.getConnectionServerHandler(selected);
		if(_connectionServerHandler == null){
			_log.log(Level.SEVERE, "Can't create ConnectionServerHandler. What's going on?");
			this.shutdown();
		}
		
		_connectionServerHandler.start();
		while(!_connectionServerHandler.isRunning()){
			if(!_connectionServerHandler.isRunning())
				_ioHandler.write(Boolean.FALSE.toString());
		}
		_ioHandler.write("Passato while di attesa");
		_ioHandler.write(_connectionServerHandler.prova());
	}
	
	private void shutdown() {
		_ioHandler.shutdown();
		_connectionServerHandler.shutdown();
	}

	private final IOHandler _ioHandler;
	private ConnectionServerHandler _connectionServerHandler;
	private Logger _log = Logger.getLogger(Client.class.getName());
}
