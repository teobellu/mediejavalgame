package client.cli;

import java.util.List;

import client.ClientText;
import client.UI;
import client.network.ConnectionServerHandler;
import client.network.ConnectionServerHandlerFactory;
import client.network.SocketConnectionServerHandler;
import game.FamilyMember;
import game.GameBoard;
import game.Space;
//import game.Space;
import game.development.DevelopmentCard;
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
		
		_ioHandler.write(b.getDices()[2] + " -> devo stamparare 4");
		
		
		int[] h = new int[3];
		h[0] = 6;
		h[1] = 6;
		h[2] = 6;
		b.setDices(h);
		
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			e.getCause();
		}
		
		b = c.getBoard();
		_ioHandler.write(b.getDices()[2] + " -> devo stamparare 4");
		
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			e.getCause();
		}
		c.shutdown();
		
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
	
	public void showGameBoard(GameBoard board){
		_ioHandler.write("Here is the game board:");
		_ioHandler.write("Tower");
		for (int row=0; row<board.MAX_ROW; row++)
			for (int column=0; column< board.MAX_COLUMN; column++){
				//DevelopmentCard card = board.getCard(row, column);
			}
		
		_ioHandler.write("Market: ");
		for (int index=0; index < board.MAX_MARKET_SPACE; index ++){
			//get info
			Space market = board.getMarketSpace(index);
			int dice = market.getRequiredDiceValue();
			List<FamilyMember> familiars = market.getFamiliar();
			String effect = market.getInstantEffect().getIEffectBehavior().toString();
			//show info
			_ioHandler.write("Space " + index + ":");
			_ioHandler.write("Dice Required = " + dice);
			_ioHandler.write("Effect = " + effect);
			if (familiars.isEmpty())
				_ioHandler.write("No familiars here");
			else
				familiars.forEach(fam -> _ioHandler
					.write("A " + fam.getColor() + " " + fam.getOwner().getName() + "'s familiar is here"));
		}
	}
}
