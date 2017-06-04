package client.cli;

import client.ClientText;
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
		_ioHandler.write(ClientText.ASK_SERVER_ADDRESS);
		String host = _ioHandler.readLine(false);
		
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
		ConnectionServerHandler connection = ConnectionServerHandlerFactory.getConnectionServerHandler(Constants.CONNECTION_TYPES[selected], host, port);
		
		return connection;
	}
	
	public String getUsername(){
		//Get username
		_ioHandler.write("Ciao. Come vorresti chiamarti?");
		String username = _ioHandler.readLine(false);
		return username;
	}
	
	@Override
	public String getStringValue(boolean isEmptyAllowed) {
		return _ioHandler.readLine(isEmptyAllowed);
	}

	@Override
	public void printString(String string) {
		_ioHandler.write(string);
	}
	
	@Override
	public String askForConfigFile() {
		_ioHandler.write(ClientText.ASK_IF_CONFIG_FILE);
		return _ioHandler.readLine(true);
	}
	
	@Override
	public void write(String str) {
		_ioHandler.write(str);
	}
	
	private final IOHandler _ioHandler;
}
