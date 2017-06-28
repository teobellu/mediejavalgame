package client.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.GameException;
import game.FamilyMember;
import game.GameBoard;
import game.LeaderCard;
import game.Player;
import game.Position;
import game.Resource;
import util.CommandStrings;
import util.Constants;

/**
 * @author Jacopo
 *
 */
public class SocketConnectionServerHandler extends ConnectionServerHandler {

	public SocketConnectionServerHandler(String host, int port) {
		super(host, port);
		
		if(port==0){
			_port=Constants.DEFAULT_SOCKET_PORT;
		}
		
		try {
			_socket = new Socket(_host, _port);
			
			_outputStream = new ObjectOutputStream(_socket.getOutputStream());
			_inputStream = new ObjectInputStream(_socket.getInputStream());
			
			_reader = new Thread(new MyRunnable());
			
			_log.info("Socket Connection is up");
			
			_isRunning = true;
			
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	@Override
	public void run() {
		_reader.start();
	}
	
	public void processObject(Object obj) throws Exception {
		if(obj instanceof String){
			processString((String) obj);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void processString(String obj) throws Exception{
		//TODO da rimuovere
		_log.info(obj);
		
		if(obj.equals(CommandStrings.START_TURN)){
			GameBoard board = (GameBoard) getFromServer();
			Player me = (Player) getFromServer();
			_ui.startTurn(board, me);
		}
		else if(obj.equals(CommandStrings.ADD_TO_GAME)){
			synchronized (_returnObject) {
				_returnObject.notify();
				_returnObject = (boolean) getFromServer();
			}
		}
		else if(obj.equals(CommandStrings.INITIAL_LEADER)){
			List<LeaderCard> tempList = ((List<LeaderCard>) getFromServer());
			
			int i = _ui.chooseLeader(CommandStrings.INITIAL_LEADER, tempList);
			writeObject(CommandStrings.INITIAL_LEADER);
			writeObject(i);
		}
		else if(obj.equals(CommandStrings.INITIAL_PERSONAL_BONUS)){
			Map<String, List<Resource>> bonus = (Map<String, List<Resource>>) getFromServer();
			
			int i = _ui.chooseDashboardBonus(bonus);
			writeObject(CommandStrings.INITIAL_PERSONAL_BONUS);
			writeObject(i);
		}
		else if(obj.equals(CommandStrings.START_TURN)){
			GameBoard board = (GameBoard) getFromServer();
			Player me = (Player) getFromServer();
			_ui.startTurn(board, me);
		}
		else if(obj.equals(CommandStrings.CHOOSE_CONVERT)){
			List<Resource> realPayOptions = (List<Resource>) getFromServer();
			List<Resource> realGainOptions = (List<Resource>) getFromServer();
			
			int i = _ui.chooseConvert(realPayOptions, realGainOptions);
			writeObject(CommandStrings.CHOOSE_CONVERT);
			writeObject(i);
		}
		else if(obj.equals(CommandStrings.CHOOSE_FAMILIAR)){
			List<FamilyMember> familiars = (List<FamilyMember>) getFromServer();
			String message = (String) getFromServer();
			
			int i = _ui.chooseFamiliar(familiars, message);
			writeObject(CommandStrings.CHOOSE_FAMILIAR);
			writeObject(i);
		}
		else if(obj.equals(CommandStrings.ASK_INT)){
			String message = (String) getFromServer();
			int min = (int) getFromServer();
			int max = (int) getFromServer();
			
			int i = _ui.askInt(message, min, max);
			
			writeObject(CommandStrings.ASK_INT);
			writeObject(i);
		}
	}
	
	@Override
	public List<FamilyMember> putFamiliar() throws RemoteException {
			String message = CommandStrings.PUT_FAMILIAR;
			writeObject(message);
			
			List<String> familiars = new ArrayList<String>();
			while(message!=CommandStrings.END_TRANSMISSION){
				message = (String) getFromServer();
				familiars.add(message);
			}
			
			synchronized (familiars) {
				
			}
			return null;//TODO ovviamente non ritorno null
			//return familiars;
			//TODO quale familiare
			//TODO dove piazzarlo
			//TODO aumentare il suo valore? Se s�, di quanto?
	}
	
	@Override
	public List<String> putFamiliarWhich(String familiar) throws RemoteException {
			String message = CommandStrings.PUT_WHICH_FAMILIAR;
			writeObject(message);
			writeObject(familiar);
			List<String> positions = new ArrayList<>();
			while(message!=CommandStrings.END_TRANSMISSION){
				message = (String) getFromServer();
				positions.add(message);
			}
			
			return positions;
	}

	@Override
	public void putFamiliarWhere(String position) throws RemoteException {
			writeObject(CommandStrings.PUT_WHERE_FAMILIAR);
			writeObject(position);
	}
	
	@Override
	public void sendConfigFile(String file) throws RemoteException {
			writeObject(CommandStrings.ASK_FOR_CONFIG);
			writeObject(file);
	}

	@Override
	public List<String> activateLeaderCard() throws RemoteException {
			String message = CommandStrings.ACTIVATE_LEADER_CARD;
			writeObject(message);
			
			return null;//TODO
	}
	
	@Override
	public void activateLeaderCard(String card){
			writeObject(CommandStrings.ACTIVATE_WHICH_LEADER_CARD);
			writeObject(card);
	}

	@Override
	public void ping() throws RemoteException {
			writeObject(CommandStrings.PING);
	}
	
	@Override
	public void shutdown() {
		super.shutdown();
		
		try {
			if(_inputStream != null){
				_inputStream.close();
			}
			
			if(_outputStream != null){
				_outputStream.close();
			}
			
			if(!_socket.isClosed()){
				_socket.close();
			}
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	@Override
	public boolean addMeToGame(String name) throws RemoteException {
		try {
			_returnObject = new Object();
			writeObject(CommandStrings.ADD_TO_GAME);
			writeObject(name);
			
			synchronized (_returnObject) {
				_returnObject.wait();
			}
			
			return (boolean) _returnObject;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return false;
	}
	
	private synchronized void writeObject(Object message){
		try {
			_outputStream.writeUnshared(message);
			_outputStream.flush();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
	}
	
	/**
	 * Read an object from the {@link ObjectInputStream}. Cannot return <code>null</code> value.
	 * @return the object read
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private Object getFromServer() {
		try {
			do {
				Object obj = null;
				synchronized (_inputStream) {
					obj = _inputStream.readObject();
				}
				
				if (obj!=null) {
					return obj;
				} else {
					Thread.sleep(500);
				}
			} while (_isRunning);
		} catch (InterruptedException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		} catch(ClassNotFoundException cnf){
			_log.log(Level.SEVERE, cnf.getMessage(), cnf);
		} catch (IOException ioe) {
			_log.log(Level.SEVERE, ioe.getMessage(), ioe);
			shutdown();
		}
		System.out.println("Errore");
		return CommandStrings.END_TURN;
	}

	@Override
	public void doIspendMyFaithPoints(boolean doI) throws RemoteException {
		writeObject(CommandStrings.SPEND_FAITH_POINTS);
		writeObject(Boolean.toString(doI));
	}

	@Override
	public boolean endTurn() throws RemoteException {
		writeObject(CommandStrings.END_TURN);
		String response = (String) getFromServer();
		if (response==CommandStrings.END_TURN) {
			return true;
		} else return false;
	}

	@Override
	public List<String> dropLeaderCard() throws RemoteException {
		writeObject(CommandStrings.DROP_LEADER_CARD);
		String str = null;
		List<String> leaders = new ArrayList<>();
		do {
			str = (String) getFromServer();
			leaders.add(str);
		} while (str!=CommandStrings.END_TRANSMISSION);

		//rimuovo il comando di fine trasmissione
		leaders.remove(leaders.size()-1);
		
		return leaders;
	}

	@Override
	public void dropWhichLeaderCard(String leaderCard) throws RemoteException {
		writeObject(CommandStrings.DROP_WHICH_LEADER_CARD);
		writeObject(leaderCard);
	}
	
	public GameBoard getBoard(){
		writeObject("send me board");
		return (GameBoard) getFromServer();
	}
	
	@Override
	public Player getMe() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dropLeaderCard(LeaderCard card) throws GameException, RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activateLeaderCard(LeaderCard card) throws GameException, RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void placeFamiliar(FamilyMember familiar, Position position) throws GameException, RemoteException {
		// TODO Auto-generated method stub
		
	}

	private Object _returnObject = new Object();
	private Thread _reader;
	private Socket _socket;
	private ObjectInputStream _inputStream;
	private ObjectOutputStream _outputStream;
	private final Logger _log = Logger.getLogger(SocketConnectionServerHandler.class.getName());
	
	private class MyRunnable implements Runnable{
		@Override
		public void run() {
			while (_isRunning) {
				try{
					Object obj = getFromServer();
					if (obj != null) {
						processObject(obj);
					}
				}catch(Exception e){
					_log.log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
	}
}
