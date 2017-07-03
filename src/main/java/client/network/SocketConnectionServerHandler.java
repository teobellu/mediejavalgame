package client.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	
	private void processObject(Object obj) throws Exception {
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
		else if(obj.equals(CommandStrings.INFO)){
			_ui.showInfo((String) getFromServer());
		}
		else if(obj.equals(CommandStrings.INFO_BOARD)){
			String info = (String) getFromServer();
			GameBoard board = (GameBoard) getFromServer();
			_ui.showInfoWithBoardUpdate(info, board);
		}
		else if(obj.equals(CommandStrings.ASK_BOOLEAN)){
			boolean returned = _ui.askBoolean((String) getFromServer());
			
			writeObject(CommandStrings.ASK_BOOLEAN);
			writeObject(returned);
		}
		else if(obj.equals(CommandStrings.HANDLE_COUNCIL)){
			int i = _ui.spendCouncil((List<Resource>) getFromServer());
			
			writeObject(CommandStrings.HANDLE_COUNCIL);
			writeObject(i);
		}
		else if(obj.matches(CommandStrings.GAME_BOARD+"|"+CommandStrings.PLAYER+
				"|"+CommandStrings.END_TURN+"|"+CommandStrings.DROP_LEADER_CARD+
				"|"+CommandStrings.ACTIVATE_LEADER_CARD+"|"+CommandStrings.PLACE_FAMILIAR)){
			synchronized (_returnObject) {
				_returnObject.notify();
				_returnObject = getFromServer();
				System.out.println("Wake up!");
			}
		}else{
			_log.log(Level.SEVERE, "\n###RICEVUTO COMANDO SCONOSCIUTO: "+obj+"###\n");
		}
	}
	
	@Override
	public void sendConfigFile(String file) throws RemoteException {
			writeObject(CommandStrings.ASK_FOR_CONFIG);
			writeObject(file);
	}

	@Override
	public void ping() throws RemoteException {
			//TODO
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
			_outputStream.reset();
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
					obj = _inputStream.readUnshared();
				}
				
				if (obj!=null) {
					
					if(obj instanceof Player){
						System.out.println("\n######Questo player ha "+((Player) obj).getLeaderCards().size()+" carte leader\n");
					}
					
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
	public void endTurn() throws RemoteException {
		writeObject(CommandStrings.END_TURN);
	}
	
	public GameBoard getBoard(){
		try {
			_returnObject = new Object();
			
			writeObject(CommandStrings.GAME_BOARD);
			
			synchronized (_returnObject) {
				_returnObject.wait();
			}
			
			return (GameBoard) _returnObject;
		} catch (InterruptedException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		System.out.println("ERRORE IN getBoard");
		
		return null;
	}
	
	@Override
	public Player getMe() throws RemoteException {
		try{
			_returnObject = new Object();
			
			writeObject(CommandStrings.PLAYER);
			
			synchronized (_returnObject) {
				System.out.println("\nWaiting...\n");
				_returnObject.wait();
			}
			
			Player pl = (Player) _returnObject;
			
			System.out.println("Questo player ha "+pl.getLeaderCards().size()+" carte leader");
			return (Player) _returnObject;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		System.out.println("ERRORE IN getMe");
		
		return null;
	}

	@Override
	public void dropLeaderCard(String leaderName) throws RemoteException {
		writeObject(CommandStrings.DROP_LEADER_CARD);
		writeObject(leaderName);
	}

	@Override
	public void activateLeaderCard(String leaderName) throws RemoteException {
		writeObject(CommandStrings.ACTIVATE_LEADER_CARD);
		writeObject(leaderName);
	}

	@Override
	public void placeFamiliar(String familiarName, Position position) throws RemoteException {
		writeObject(CommandStrings.PLACE_FAMILIAR);
		writeObject(familiarName);
		writeObject(position);
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
					System.out.println("Reading...");
					Object obj = getFromServer();
					if (obj != null) {
						processObject(obj);
					}
				}catch(Exception e){
					_log.log(Level.SEVERE, e.getMessage(), e);
				}
			}
			
			System.out.println("\n\n###READER SHUTTING DOWN###\n\n");
		}
	}
}
