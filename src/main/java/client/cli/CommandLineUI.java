package client.cli;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import client.ClientText;
import client.UI;
import client.network.ConnectionServerHandler;
import client.network.ConnectionServerHandlerFactory;
import game.FamilyMember;
import game.GC;
import game.GameBoard;
import game.LeaderCard;
import game.Player;
import game.Resource;
import game.development.DevelopmentCard;
import game.development.Territory;
import util.Constants;
import util.IOHandler;

public class CommandLineUI implements UI {
	
	private Thread t = new MyThread();
	
	class MyThread extends Thread {
	 
	    @Override
	    public void run() {
	    	while(true){
				String omega = _ioHandler.readLine(false);
				_ioHandler.write(omega.toUpperCase());
			}
	    }
	}

	private final IOHandler _ioHandler;
	
	private Object locker = null;
	
	private List<String> commands = new ArrayList<>();
	
	private static CommandLineUI _instance = null;
	
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
	
		connection.setClient(getInstance());
		
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
		
		
		
		t.start();
		
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
		t.interrupt();
		_ioHandler.write("Select a Leader card");
		int index = 0;
		for(LeaderCard card : leaders){
			_ioHandler.writeNext(index + ") ");
			_ioHandler.writeNext("Name: " + card.getName() + " ");
			_ioHandler.write("Effect: " + card.getEffect().toString());
			index++;
		}
		t.start();
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
