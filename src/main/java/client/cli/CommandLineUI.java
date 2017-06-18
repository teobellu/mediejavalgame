package client.cli;

import java.util.List;

import client.ClientText;
import client.UI;
import client.network.ConnectionServerHandler;
import client.network.ConnectionServerHandlerFactory;
import client.network.SocketConnectionServerHandler;
import game.GameBoard;
import util.Constants;
import util.IOHandler;

public class CommandLineUI implements UI {

	private final IOHandler _ioHandler;
	
	public CommandLineUI() {
		_ioHandler = new IOHandler();
	}
	
	public ConnectionServerHandler getConnection() {
		
				
		//Get server address
		_ioHandler.write(ClientText.ASK_SERVER_ADDRESS);
		String host = _ioHandler.readLine(false);
		if (host == "0")
			host = "localhost";
		
		//Get server's port
		_ioHandler.write("Select port");
		int port = _ioHandler.readNumber();
		
		//Choose connection type
		_ioHandler.write("Select connection");
		_ioHandler.writeList(Constants.CONNECTION_TYPES);
		int selected = _ioHandler.readNumberWithinInterval(Constants.CONNECTION_TYPES.size() - 1);
		ConnectionServerHandler connection = ConnectionServerHandlerFactory.getConnectionServerHandler(Constants.CONNECTION_TYPES.get(selected), host, port);
		
		SocketConnectionServerHandler c = (SocketConnectionServerHandler) connection;
		
		GameBoard b = c.getBoard();
		
		_ioHandler.write(b.getExCard()[2].getAge() + " -> devo stamparare 3");
		
		return connection;
	}
	
	public String getUsername(){
		//Get username
		_ioHandler.write("Welcome! Select your nickname");
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

	@Override
	public void start() {
		_ioHandler.write("You selected CLI");
	}

	@Override
	public void setConnection(String connectionType, String host, int port) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void run() {
		getConnection();
		
	}

	@Override
	public List<String> dropLeaderCard() {
		// TODO Auto-generated method stub
		return null;
	}
}
