package client.userinterface;

import client.network.ConnectionServerHandler;
import client.network.ConnectionServerHandlerFactory;
import util.Constants;
import util.IOHandler;

public class CommandLineUI implements UI {

	public CommandLineUI() {
		_ioHandler = new IOHandler();
	}
	
	@Override
	public ConnectionServerHandler getConnection() {
		
				
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
		ConnectionServerHandler connection = ConnectionServerHandlerFactory.getConnectionServerHandler(selected, host, port);
		
		return connection;
	}
	
	public String getUsername(){
		//Get username
		_ioHandler.write("Ciao. Come vorresti chiamarti?");
		String username = _ioHandler.readLine();
		return username;
	}
	
	

	private final IOHandler _ioHandler;



	@Override
	public String getStringValue(String request) {
		// TODO Auto-generated method stub
		return null;
	}
}
