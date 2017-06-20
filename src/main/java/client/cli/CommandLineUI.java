package client.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import client.ClientText;
import client.UI;
import client.network.ConnectionServerHandler;
import client.network.ConnectionServerHandlerFactory;
import client.network.SocketConnectionServerHandler;
import game.FamilyMember;
import game.GC;
import game.GameBoard;
import game.LeaderCard;
import game.Player;
import game.Resource;
import game.Space;
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
		/*
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
		*/
		Integer [] dadi = {4, 6, 4};
		GameBoard b = new GameBoard();
		b.setDices(dadi);
		
		showGameBoard(b);
		
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
		_ioHandler.write("(^_^) Here is the game board:");
		
		_ioHandler.write("*****Tower");
		for (int column = 0; column < GameBoard.MAX_COLUMN; column++){
			_ioHandler.write("\nColumn " + column + ":");
			for (int row = GameBoard.MAX_ROW - 1; row >= 0; row--){
				_ioHandler.writeNext("Row " + row + ": ");
				DevelopmentCard card = board.getCard(row, column);
				if (card == null)
					_ioHandler.writeNext("No card here! ");
				else{
					_ioHandler.writeNext("Card name: " + card.getName() + " ");
				}
				printSpace(board.getCell(row, column));
			}
		}
		
		//council palace
		_ioHandler.write("\n*****Council Palace: ");
		printSpace(board.getCouncilPalaceSpace());
		
		//harvest
		_ioHandler.write("\n*****Harvest Space: ");
		_ioHandler.writeNext("Normal Space: ");
		printSpace(board.getWorkSpace(GC.HARVEST));
		_ioHandler.writeNext("Long Space: ");
		printSpace(board.getWorkLongSpace(GC.HARVEST));
		
		//production
		_ioHandler.write("\n*****Producion Space: ");
		_ioHandler.writeNext("Normal Space: ");
		printSpace(board.getWorkSpace(GC.PRODUCTION));
		_ioHandler.writeNext("Long Space: ");
		printSpace(board.getWorkLongSpace(GC.PRODUCTION));
		
		//market
		_ioHandler.write("\n*****Market: ");
		for (int index=0; index < GameBoard.MAX_MARKET_SPACE; index ++){
			_ioHandler.writeNext("Space " + index + ": ");
			printSpace(board.getMarketSpace(index));
		}
		
		//excommunication tiles
		_ioHandler.write("\n*****Excommunication tiles: ");
		for (int index=0; index < GameBoard.MAX_EXCOMUNNICATION_CARD; index++){
			String effectDescription = board.getExCard()[index].getEffect().toString();
			_ioHandler.write("Malus " + index + ": " + effectDescription);
		}
		
		//dices
		_ioHandler.write("\n*****Dices: ");
		List<Integer> dices = Arrays.asList(board.getDices());
		
		for (int index=0; index < GameBoard.MAX_DICES; index++)
			_ioHandler.write("Dice " + GC.FM_TYPE.get(index) + ": "+ dices.get(index));
		
	}
	
	public void showWhatIHave(Player me){
		//resources
		_ioHandler.write("\n*****Resources: ");
		GC.RES_TYPES.stream()
			.filter(type -> me.getResource(type)>0)
			.forEach(type -> _ioHandler.write(type + ": " + me.getResource(type)));
		
		//dev cards
		_ioHandler.write("\n*****Development cards: ");
		for(String type : GC.DEV_TYPES)
			if (!me.getDevelopmentCards(type).isEmpty()){
				_ioHandler.write("\n" + type);
				for(DevelopmentCard card : me.getDevelopmentCards(type)){
					_ioHandler.writeNext(card.getName() + " ");
				}
			}
		
		//leader cards
		_ioHandler.write("\n*****Leader cards: ");
		me.getLeaderCards()	
			.forEach(leader -> _ioHandler.writeNext(leader.getName() + " "));
		
		//leader cards that you can activate
		_ioHandler.write("\n*****Leader cards that you can activate: ");
		me.getActivableLeaderCards()
			.forEach(leader -> _ioHandler.writeNext(leader.getName() + " "));
		
		//free familiars
		_ioHandler.write("\n*****Familiars: ");
		me.getFreeMember()
			.forEach(fam -> _ioHandler.writeNext(fam.getColor() + " with power " + fam.getValue()));
		
	}
	
	private void printSpace(Space space){
		_ioHandler.writeNext("Dice Required: " + space.getRequiredDiceValue() + " ");
		_ioHandler.writeNext("Effect: " + space.getInstantEffect() + " ");
		printListFamiliar(space.getFamiliar());
	}
	
	private void printListFamiliar(List<FamilyMember> familiars){
		if (familiars.isEmpty())
			_ioHandler.write("No familiars here");
		else
			familiars.forEach(fam -> _ioHandler
				.write("A " + fam.getColor() + " " + fam.getOwner().getName() + "'s familiar is here "));
	}
	
	/**
	 * Let player to spend his council privilege
	 * @param resources List of options, as rewards
	 * @return Index, selection of the player
	 */
	public int spendCouncil(List<Resource> resources){
		_ioHandler.write("Select a reward");
		int index = 0;
		for(Resource reward : resources){
			_ioHandler.write(index + ") ");
			GC.RES_TYPES.stream()
				.filter(type -> reward.get(type) > 0)
				.forEach(type -> _ioHandler.writeNext(reward.get(type) + " " + type + " "));
		}
		return _ioHandler.readNumberWithinInterval(resources.size() - 1);
	}
	
	/**
	 * Let player to select a leader card
	 * @param leaders List of options
	 * @return index of list, selection
	 */
	public int dropLeaderCard(List<LeaderCard> leaders){
		_ioHandler.write("Select a Leader card");
		int index = 0;
		for(LeaderCard card : leaders){
			_ioHandler.write(index + ") ");
			_ioHandler.writeNext("Name :" + card.getName());
			_ioHandler.writeNext("Effect :" + card.getEffect().toString());
		}
		return _ioHandler.readNumberWithinInterval(leaders.size() - 1);
	}
}
