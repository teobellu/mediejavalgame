package client.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.ClientText;
import client.UI;
import client.network.ConnectionServerHandler;
import client.network.ConnectionServerHandlerFactory;
import model.DevelopmentCard;
import model.FamilyMember;
import model.GC;
import model.GameBoard;
import model.LeaderCard;
import model.Player;
import model.Position;
import model.Resource;
import model.exceptions.GameException;
import util.Constants;
import util.IOHandler;

/**
 * CLI main view
 *
 */
public class CommandLineUI implements UI {
	
	/**
	 * Logger
	 */
	private Logger _log = Logger.getLogger(CommandLineUI.class.getName());
	
	/**
	 * Cached gameboard
	 */
	private GameBoard _board;
	
	/**
	 * Cached player
	 */
	private Player _me;

	/**
	 * IOhandler reader + writer
	 */
	private final IOHandler _ioHandler;
	
	/**
	 * List of commands
	 */
	private List<String> commands = new ArrayList<>();
	
	/**
	 * @Singleton
	 * Instance
	 */
	private static CommandLineUI _instance = null;
	
	/**
	 * Connection handler
	 */
	private ConnectionServerHandler _connectionHandler;
	
	/**
	 * My 128 bit uuid
	 */
	private String _uuid;
	
	/**
	 * Get instance if exists
	 * @return
	 */
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
	
	/**
	 * Cli constructor
	 */
	private CommandLineUI() {
		_ioHandler = new IOHandler();
	}
	
	/**
	 * Get a connection
	 */
	public void getConnection() {
				
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
		
		_ioHandler.write("If you would like to provide a custom configuration file, write the path to it.\nOtherwise, just hit enter.");
		
		String path = _ioHandler.readLine(true);
		
		setConnection(Constants.CONNECTION_TYPES.get(selected), host, port);
		
		File file = new File(path);
		FileReader customConfig = null;
		try {
			customConfig = new FileReader(file);
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(customConfig);
			String line;
			while((line = br.readLine() ) != null) {
			    sb.append(line);
			}
			
			br.close();
			_connectionHandler.sendConfigFile(sb.toString());
		} catch (FileNotFoundException e) {
			_log.log(Level.FINE, e.getMessage(), e);
			if(!path.equals("")){
				_ioHandler.write("File not found.");
			}
			_ioHandler.write("Using default file.");
		} catch (RemoteException e) {
			_log.log(Level.FINE, e.getMessage(), e);
			_ioHandler.shutdown();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			if(customConfig!=null){
				try {
					customConfig.close();
				} catch (IOException e2) {
					_log.log(Level.SEVERE, e2.getMessage(), e2);
				}
			}
		}
		
		commands.addAll(CommandConstants.STANDARD_COMMANDS);
	}
	
	@Override
	public void startTurn(GameBoard board, Player me) {
		_ioHandler.write("\nIt's your turn! :D");
		_board = board;
		_me = me;
		
		Runnable run = () -> {
			handleTurn();
		};
		new Thread(run).start();
		
	}
	
	@Override
	public void run() {
		getConnection();
		do {
			try {
				addMeToGame(getUsername());
				break;
			} catch (GameException e) {
				_log.log(Level.FINE, e.getMessage(), e);
				_ioHandler.write("Name already taken.");
			}
		} while (true);
	}
	
	public void handleTurn(){
		int selection = 0;
		_ioHandler.write("\nWhat do you want to do?");
		_ioHandler.writeList(commands);
		selection = _ioHandler.readNumberWithinInterval(commands.size() - 1);
		try {
			switch (commands.get(selection)){
			case CommandConstants.PRINT_BOARD : printBoard();
				break;
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
			case CommandConstants.SHOW_SUPPORT : showSupport();
				break;
			case CommandConstants.PLAY_OPT_LEADERS : playOPTLeader();
				break;
			default : handleTurn();
		}
		} catch (RemoteException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			_ioHandler.write("Connection error. Would you like to attempt reconnection?");
			_ioHandler.writeList(Arrays.asList("Yes","No"));
			int yesno  = _ioHandler.readNumberWithinInterval(1);
			if(yesno == 0){
				try {
					attemptReconnection();
				} catch (RemoteException e1) {
					_log.log(Level.SEVERE, e1.getMessage(), e1);
					_ioHandler.shutdown();
				}
			} else {
				_ioHandler.shutdown();
			}
		}
	}

	/**
	 * Try to show support to vatican
	 * @throws RemoteException
	 */
	private synchronized void showSupport() throws RemoteException {
		showVaticanSupport();
		handleTurn();
	}
	
	/**
	 * Try to play OPT leader cards
	 * @throws RemoteException
	 */
	private synchronized void playOPTLeader() throws RemoteException {
		activateOPTLeaders();
		handleTurn();
	}

	/**
	 * Place a familiar
	 * @throws RemoteException
	 */
	private synchronized void placeFamiliar() throws RemoteException{
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
		placeFamiliar(selectedFamiliar.getColor(), position);
		handleTurn();
		//fine, esco da questo metodo
	}
	
	/**
	 * Print the cached board
	 */
	private void printBoard(){
		ModelPrinter.printBoard(_board);
		handleTurn();
	}
	
	/**
	 * Show my cards (cached player)
	 */
	private void showMyCards(){
		ModelPrinter.printMyLoot(_me);
		handleTurn();
	}

	/**
	 * Activate a leader card
	 * @throws RemoteException
	 */
	private synchronized void activateLeader() throws RemoteException{
		int selection = 0;
		List<LeaderCard> myLeaders;
		//seleziona carta
		myLeaders = _me.getLeaderCards();
		if (myLeaders.isEmpty()){
			_ioHandler.write("You don't have leader cards!");
			handleTurn();
		}
		_ioHandler.write("Select card to activate");
		ModelPrinter.printLeaderCards(myLeaders);
		selection = _ioHandler.readNumberWithinInterval(myLeaders.size() - 1);
		activateLeaderCard(myLeaders.get(selection).getName());
		handleTurn();
	} 
	
	/**
	 * Drop a leader card
	 * @throws RemoteException
	 */
	private synchronized void dropLeader() throws RemoteException{
		int selection = 0;
		List<LeaderCard> myLeaders;
		//seleziona carta
		myLeaders = _me.getLeaderCards();
		if (myLeaders.isEmpty()){
			_ioHandler.write("You don't have leader cards!");
			handleTurn();
		}
		_ioHandler.write("Select card to drop");
		ModelPrinter.printLeaderCards(myLeaders);
		selection = _ioHandler.readNumberWithinInterval(myLeaders.size() - 1);
		dropLeaderCard(myLeaders.get(selection).getName());
		handleTurn();
	}
	
	@Override
	public void dropLeaderCard(String leaderName) throws RemoteException {
		_connectionHandler.dropLeaderCard(leaderName);
	}

	@Override
	public void activateLeaderCard(String leaderName) throws RemoteException{
		_connectionHandler.activateLeaderCard(leaderName);
	}


	@Override
	public void placeFamiliar(String familiarColour, Position position) throws RemoteException {
		_connectionHandler.placeFamiliar(familiarColour, position);
	}
	
	@Override
	public void endTurn() throws RemoteException{
		_connectionHandler.endTurn();
	}
	
	@Override
	public void showVaticanSupport() throws RemoteException{
		_connectionHandler.showVaticanSupport();
	}
	
	@Override
	public void activateOPTLeaders() throws RemoteException {
		_connectionHandler.activateOPTLeaders();
	}
	
	public String getUsername(){
		//Get username
		_ioHandler.write("Welcome! Select your nickname");
		String username = _ioHandler.readLine(false);
		return username;
	}
	
	@Override
	public void setConnection(String connectionType, String host, int port) {
		_connectionHandler = ConnectionServerHandlerFactory.getConnectionServerHandler(connectionType, host, port);
		_connectionHandler.setClient(this);
		new Thread(_connectionHandler).start();
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
		_ioHandler.write("0) ok/yes");
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
	public void addMeToGame(String username) throws GameException{
		try {
			if(!_connectionHandler.addMeToGame(username)){
				throw new GameException("Name already taken!");
			}
		} catch (RemoteException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			_ioHandler.shutdown();
		}
	}

	@Override
	public void showInfo(String info) {
		_ioHandler.write("[INFO] : " + info);
	}

	@Override
	public void showInfo(String infoMessage, GameBoard board) {
		_board = board;
		showInfo(infoMessage);
	}

	@Override
	public void showInfo(String message, Player me) {
		_me = me;
		showInfo(message);
	}

	@Override
	public void showInfo(String message, GameBoard board, Player me) {
		_board = board;
		_me = me;
		showInfo(message);
	}

	@Override
	public void setUUID(String uuid) {
		_uuid = uuid;
	}

	@Override
	public synchronized void reconnected() {
		_ioHandler.write("You have been succesfully reconnected");
		handleTurn();
	}

	@Override
	public void attemptReconnection() throws RemoteException {
		_connectionHandler.attemptReconnection(_uuid);
	}
}
