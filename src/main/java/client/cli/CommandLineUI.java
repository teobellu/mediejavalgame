package client.cli;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.ClientText;
import client.UI;
import client.network.ConnectionServerHandler;
import client.network.ConnectionServerHandlerFactory;
import exceptions.GameException;
import game.FamilyMember;
import game.GC;
import game.GameBoard;
import game.LeaderCard;
import game.Player;
import game.Position;
import game.Resource;
import game.development.DevelopmentCard;
import util.Constants;
import util.IOHandler;

public class CommandLineUI implements UI {
	
	private Logger _log = Logger.getLogger(CommandLineUI.class.getName());
	
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
		if ("0".equals(host))
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
		
		return connection;
	}
	
	@Override
	public void startTurn(GameBoard board, Player me) {
		_ioHandler.write("\nIt's your turn! :D");
		_board = board;
		_me = me;
		ModelPrinter.printBoard(_board);
		ModelPrinter.printMyLoot(_me);
		
		try {
			handleTurn();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			getConnection().addMeToGame(getUsername());
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
	}
	
	public void handleTurn() throws RemoteException{
		int selection = 0;
		_ioHandler.write("\nWhat do you want to do?");
		_ioHandler.writeList(commands);
		selection = _ioHandler.readNumberWithinInterval(commands.size() - 1);
		switch (commands.get(selection)){
			case CommandConstants.PLACE_FAMILIAR : placeFamiliar();
				break;
			case CommandConstants.ACTIVATE_LEADER : activateLeader();
				break;
			case CommandConstants.DROP_LEADER : dropLeader();
				break;
			case CommandConstants.END_TURN : endTurn();
				break;
			case CommandConstants.SHOW_MY_CARDS : showMyCards();
				break;
			default : handleTurn();
		}
	}

	private void placeFamiliar() throws RemoteException{
		int selection = 0;
		// quale familiare
		List<FamilyMember> myFreeFamiliars = _me.getFreeMembers();
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
			
		try {
			placeFamiliar(selectedFamiliar.getColor(), position);
			_board = _connectionHandler.getBoard();
			_me = _connectionHandler.getMe();
			ModelPrinter.printBoard(_board);
			ModelPrinter.printMyLoot(_me);
		} catch (GameException e) {
			e.printStackTrace();
		}
		handleTurn();
		//fine, esco da questo metodo
	}
	
	private void activateLeader() throws RemoteException{
		int selection = 0;
		List<LeaderCard> myLeaders;
		//seleziona carta
		_me = _connectionHandler.getMe();
		myLeaders = _me.getLeaderCards();
		if (myLeaders.isEmpty()){
			_ioHandler.write("You don't have leader cards!");
			handleTurn();
		}
		_ioHandler.write("Select card to activate");
		ModelPrinter.printLeaderCards(myLeaders);
		selection = _ioHandler.readNumberWithinInterval(myLeaders.size() - 1);
		try {
			activateLeaderCard(myLeaders.get(selection).getName());
		} catch (GameException e) {
			e.printStackTrace();
		}
		_me = _connectionHandler.getMe();
		handleTurn();
	}
	
	private void dropLeader() throws RemoteException{
		int selection = 0;
		List<LeaderCard> myLeaders;
		//seleziona carta
		_me = _connectionHandler.getMe();
		myLeaders = _me.getLeaderCards();
		if (myLeaders.isEmpty()){
			_ioHandler.write("You don't have leader cards!");
			handleTurn();
		}
		_ioHandler.write("Select card to drop");
		ModelPrinter.printLeaderCards(myLeaders);
		selection = _ioHandler.readNumberWithinInterval(myLeaders.size() - 1);
		try {
			dropLeaderCard(myLeaders.get(selection).getName());
		} catch (GameException e) {
			e.printStackTrace();
		}
		_me = _connectionHandler.getMe();
		handleTurn();
	}
	
	@Override
	public void endTurn() throws RemoteException{
		_connectionHandler.endTurn();
	}
	
	private void showMyCards() throws RemoteException{
		System.out.println("COSA CI FACCIO QUI DENTRO?????");
		_me = _connectionHandler.getMe();
		ModelPrinter.printMyLoot(_me);
		handleTurn();
	}
	
	public String getUsername(){
		//Get username
		_ioHandler.write("Welcome! Select your nickname");
		String username = _ioHandler.readLine(false);
		return username;
	}
	
	//TODO ERA OVERRIDE E DAVa conflitto (1/7/2017)
	public String askForConfigFile() {
		_ioHandler.write(ClientText.ASK_IF_CONFIG_FILE);
		return _ioHandler.readLine(true);
	}

	@Override
	public void setConnection(String connectionType, String host, int port) {
		// TODO Auto-generated method stub	
	}

	
	
	@Override
	public int spendCouncil(List<Resource> resources){
		_ioHandler.write("Select a reward");
		int index = 0;
		for(Resource reward : resources){
			_ioHandler.writeNext(index + ") ");
			ModelPrinter.printResource(reward);
			_ioHandler.write("");
			index++;
		}
		return _ioHandler.readNumberWithinInterval(resources.size() - 1);
	}
	
	
	//public int chooseLeader(List<LeaderCard> tempList);
	/**
	 * Let player to select a leader card
	 * @param context Context, explain why the player has to choose
	 * @param leaders List of options
	 * @return index of list, selection
	 */
	public int chooseLeader(String context, List<LeaderCard> leaders){
		_ioHandler.writeNext(context);
		_ioHandler.write(" : Select a Leader card");
		ModelPrinter.printLeaderCards(leaders);
		return _ioHandler.readNumberWithinInterval(leaders.size() - 1);
	}
	
	@Override
	public boolean askBoolean(String message){
		_ioHandler.write("Attention! Reply to this message: ");
		_ioHandler.write(message);
		_ioHandler.write("0) ok");
		_ioHandler.write("1) no");
		int answer = _ioHandler.readNumberWithinInterval(1);
		return answer == 0;
	}

	@Override
	public int chooseFamiliar(List<FamilyMember> familiars, String message) {
		_ioHandler.write("Attention! Reply to this message: ");
		_ioHandler.writeNext(message);
		ModelPrinter.printListFamiliar(familiars);
		return _ioHandler.readNumberWithinInterval(familiars.size() - 1);
	}
	
	@Override
	public int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions){
		_ioHandler.write("Choose the resource to convert");
		for (int i = 0; i < realPayOptions.size(); i++){
			_ioHandler.writeNext(i + ") ");
			_ioHandler.writeNext("Pay: ");
			ModelPrinter.printResource(realPayOptions.get(i));
			_ioHandler.writeNext("Gain: ");
			ModelPrinter.printResource(realGainOptions.get(i));
		}
		return _ioHandler.readNumberWithinInterval(realPayOptions.size() - 1);
	}

	@Override
	public int chooseCardCost(DevelopmentCard card) {
		_ioHandler.write("This card has different costs, choose one");
		ModelPrinter.printCard(card);
		return _ioHandler.readNumberWithinInterval(card.getCosts().size() - 1);
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
	public int askInt(String message, int min, int max) {
		_ioHandler.write("Answer to this!");
		_ioHandler.write(message + ". Answer from " + min + " to " + max);
		return _ioHandler.readNumberWithinInterval(min, max);
	}

	@Override
	public void addMeToGame(String username) throws GameException {
		try {
			getConnection().addMeToGame(username);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void dropLeaderCard(String leaderName) throws RemoteException, GameException {
		_connectionHandler.dropLeaderCard(leaderName);
	}

	@Override
	public void activateLeaderCard(String leaderName) throws RemoteException, GameException {
		_connectionHandler.activateLeaderCard(leaderName);
	}


	@Override
	public void placeFamiliar(String familiarColour, Position position) throws RemoteException, GameException {
		_connectionHandler.placeFamiliar(familiarColour, position);
	}
	
	@Override
	public void showInfo(String info) {
		_ioHandler.write("[INFO] : " + info);
	}

	@Override
	public void showInfo(String infoMessage, GameBoard board) {
		_board = board;
		ModelPrinter.printBoard(_board);
		showInfo(infoMessage);
	}

	@Override
	public void showInfo(String message, Player me) {
		_me = me;
		ModelPrinter.printMyLoot(_me);
		showInfo(message);
	}

	@Override
	public void showInfo(String message, GameBoard board, Player me) {
		_board = board;
		_me = me;
		ModelPrinter.printBoard(_board);
		ModelPrinter.printMyLoot(_me);
		showInfo(message);
	}

	@Override
	public void setUUID(String uuid) {
		// TODO Auto-generated method stub
		
	}
	
}
