package client.cli;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.junit.rules.Timeout;

import client.ClientText;
import client.UI;
import client.network.ConnectionServerHandler;
import client.network.ConnectionServerHandlerFactory;
import game.FamilyMember;
import game.GC;
import game.GameBoard;
import game.LeaderCard;
import game.Player;
import game.Position;
import game.Resource;
import game.development.DevelopmentCard;
import game.development.Territory;
import util.Constants;
import util.IOHandler;

public class CommandLineUI implements UI {
	
	private GameBoard _board;
	
	private Player _me;

	private final IOHandler _ioHandler;
	
	private Object locker = null;
	
	private List<String> commands = new ArrayList<>();
	
	private static CommandLineUI _instance = null;
	
	private ConnectionServerHandler _connectionHandler;
	
	public static CommandLineUI getInstance(){
		if(_instance==null){
			synchronized (CommandLineUI.class) {
				if(_instance==null){
					_instance = new CommandLineUI();
				}
			}
		}
		return _instance;
	}
	
	private CommandLineUI() {
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
		connection.setClient(this);
		//connection.setClient(getInstance()); boh TODO
		
		commands.addAll(CommandConstants.STANDARD_COMMANDS);
		
		_connectionHandler = connection;
		
		new Thread(connection).start();
		
		
		
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
		
		ModelPrinter.printBoard(b);
		
		return connection;
	}
	
	@Override
	public void notifyTurn() {
		_ioHandler.write("It's your turn! :D");
		try {
			_board = _connectionHandler.getBoard();
			_me = _connectionHandler.getMe();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			handleTurn();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void handleTurn() throws RemoteException{
		int selection = 0;
		_ioHandler.write("What do you want to do?");
		_ioHandler.writeList(commands);
		selection = _ioHandler.readNumberWithinInterval(commands.size() - 1);
		if (commands.get(selection) == CommandConstants.PLACE_FAMILIAR){
			// quale familiare
			List<FamilyMember> myFreeFamiliars = _me.getFreeMember();
			ModelPrinter.printListFamiliar(myFreeFamiliars);
			selection = _ioHandler.readNumberWithinInterval(myFreeFamiliars.size() - 1);
			FamilyMember selectedFamiliar = myFreeFamiliars.get(selection);
			
			//dove vuoi piazzarlo
			_ioHandler.writeList(GC.SPACE_TYPE);
			selection = _ioHandler.readNumberWithinInterval(GC.SPACE_TYPE.size() - 1);
			String where = GC.SPACE_TYPE.get(selection);
			
			//righe e colonne
			Position position;
			switch (where){
				case GC.TOWER : {
					_ioHandler.write("Select row");
					int row = _ioHandler.readNumberWithinInterval(GameBoard.MAX_ROW - 1);
					_ioHandler.write("Select column");
					int column = _ioHandler.readNumberWithinInterval(GameBoard.MAX_COLUMN - 1);
					position = new Position(where, row, column);
					break;
				}
				case GC.MARKET : {
					_ioHandler.write("Select which space");
					selection = _ioHandler.readNumberWithinInterval(GameBoard.MAX_MARKET_SPACE - 1);
					position = new Position(where, selection);
					break;
				}
				default : position = new Position(where);
			}
			//_connectionHandler.placeFamiliar(selectedFamiliar, position);
			//fine, esco da questo metodo
		}else if (commands.get(selection) == CommandConstants.PLACE_FAMILIAR){
			
		}
		
	}
	
	public String getUsername(){
		//Get username
		_ioHandler.write("Welcome! Select your nickname");
		String username = _ioHandler.readLine(false);
		return username;
	}
	
	@Override
	public String askForConfigFile() {
		_ioHandler.write(ClientText.ASK_IF_CONFIG_FILE);
		return _ioHandler.readLine(true);
	}

	@Override
	public void setConnection(String connectionType, String host, int port) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void run() {
		DevelopmentCard card = new Territory("hi", 2, GC.NIX, GC.NIX, 6);
		ModelPrinter.printCard(card);
		try {
			getConnection().addMeToGame(getUsername());
		} catch (RemoteException e) {
			
		}
		
	}

	@Override
	public List<String> dropLeaderCard() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int spendCouncil(List<Resource> resources){
		_ioHandler.write("Select a reward");
		int index = 0;
		for(Resource reward : resources){
			_ioHandler.write(index + ") ");
			ModelPrinter.printResource(reward);
			index++;
		}
		return _ioHandler.readNumberWithinInterval(resources.size() - 1);
	}
	
	
	//public int chooseLeader(List<LeaderCard> tempList);
	/**
	 * Let player to select a leader card
	 * @param leaders List of options
	 * @return index of list, selection
	 */
	public int chooseLeader(List<LeaderCard> leaders){
		_ioHandler.write("Select a Leader card");
		int index = 0;
		for(LeaderCard card : leaders){
			_ioHandler.writeNext(index + ") ");
			_ioHandler.writeNext("Name: " + card.getName() + " ");
			_ioHandler.write("Effect: " + card.getEffect().toString());
			index++;
		}
		return _ioHandler.readNumberWithinInterval(leaders.size() - 1);
	}
	
	@Override
	public boolean answerToAQuestion(String message){
		_ioHandler.write("Attention! Reply to this message: ");
		_ioHandler.writeNext(message);
		_ioHandler.write("0) ok, 1) no");
		int answer = _ioHandler.readNumberWithinInterval(1);
		return answer == 0;
	}

	@Override
	public int chooseFamiliar(List<FamilyMember> familiars, String message) {
		_ioHandler.write("Attention! Reply to this message: ");
		_ioHandler.writeNext(message);
		int index = 0;
		for(FamilyMember familiar : familiars){
			_ioHandler.write(index + ") ");
			_ioHandler.writeNext("Color: " + familiar.getColor());
			_ioHandler.writeNext("Value: " + familiar.getValue());
			index++;
		}
		return _ioHandler.readNumberWithinInterval(familiars.size() - 1);
	}
	
	@Override
	public int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions){
		_ioHandler.write("Choose the resource to convert");
		for (int i = 0; i < realPayOptions.size() - 1; i++){
			_ioHandler.write(i + ") ");
			_ioHandler.writeNext("Pay: ");
			ModelPrinter.printResource(realPayOptions.get(i));
			_ioHandler.writeNext("Gain: ");
			ModelPrinter.printResource(realGainOptions.get(i));
		}
		return _ioHandler.readNumberWithinInterval(realPayOptions.size() - 1);
	}
	
	

	@Override
	public void showInfo(String str) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int showInitialLeaderList(List<String> leadersList) {
		return 0;
	}

	@Override
	public void showBoard(GameBoard board) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyPutFamiliar(FamilyMember familiar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyDiscardLeaderCard(String playerName, String card) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int selectInitialLeaders(List<LeaderCard> leaders) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int chooseCardCost(DevelopmentCard card) {
		_ioHandler.write("This card has different costs, choose one");
		ModelPrinter.printCard(card);
		return _ioHandler.readNumberWithinInterval(card.getCost().size() - 1);
	}
	

	@Override
	public boolean activateLeaderCard(LeaderCard card) {
		// TODO Auto-generated method stub
		
		List<LeaderCard> cards = new ArrayList<>();
		List<String> names = new ArrayList<>();
		cards.forEach(carta -> names.add(carta.getName()));
		
		return false;
	}

	@Override
	public int chooseDashboardBonus(Map<String, List<Resource>> bonus) {
		_ioHandler.write("Choose your dashboard bonus");
		int index;
		for (index = 0; index < bonus.get(GC.HARVEST).size(); index++){
			_ioHandler.writeNext(index + ") ");
			_ioHandler.writeNext("Harvest bonus: ");
			ModelPrinter.printResource(bonus.get(GC.HARVEST).get(index));
			_ioHandler.writeNext("Production bonus: ");
			ModelPrinter.printResource(bonus.get(GC.PRODUCTION).get(index));
			_ioHandler.write("");
		}
		return _ioHandler.readNumberWithinInterval(index - 1);
	}

	@Override
	public void showWhatIHave(Player me) {
		// TODO Auto-generated method stub
		ModelPrinter.printMyLoot(me);
	}

	
}
