package client.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
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
 * Handle connection via socket
 * @author Jacopo
 * @author Matteo
 */
public class SocketConnectionServerHandler extends ConnectionServerHandler {

	//TODO rimuovere
	private boolean primo = true;
	
	public SocketConnectionServerHandler(String host, int port) {
		super(host, port);
		
		if(port==0){
			_port=Constants.DEFAULT_SOCKET_PORT;
		}
		
		try {
			_socket = new Socket(_host, _port);
			
			_outputStream = new ObjectOutputStream(_socket.getOutputStream());
			_inputStream = new ObjectInputStream(_socket.getInputStream());
			
			_reader = new Thread(new MyReader());
			_processor = new Thread(new MyProcessor());
			_writer = new Thread(new MyWriter());
			
			_log.info("Socket Connection is up");
			
			_isRunning = true;
			
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	/* (non-Javadoc)
	 * @see client.network.ConnectionServerHandler#attemptReconnection(java.lang.String)
	 */
	@Override
	public void attemptReconnection(String uuid) throws RemoteException {
		try {
			
			_socket = new Socket(_host, _port);
			
			_outputStream = new ObjectOutputStream(_socket.getOutputStream());
			_inputStream = new ObjectInputStream(_socket.getInputStream());
			
			_reader = new Thread(new MyReader());
			_processor = new Thread(new MyProcessor());
			_writer = new Thread(new MyWriter());
			
			_reader.start();
			_processor.start();
			_writer.start();
			
			_isRunning = true;	

			
			_returnObject = new Object();
			
			//TODO potrei dover notificare tutto quello che è in attesa, non so
//			_returnObject.notifyAll();
			
			queueToServer(CommandStrings.RECONNECT, uuid);
			
			synchronized (_returnObject) {
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_returnObject.wait();
					break;
				}
			}
			
			return;
//			return (boolean) _returnObject;
		} catch (InterruptedException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			Thread.currentThread().interrupt();
//			return false;
		} catch (UnknownHostException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
//			return false;
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
//			return false;
		}
	}
	
	@Override
	public void run() {
		_reader.start();
		_processor.start();
		_writer.start();
	}
	
	/**
	 * Parse received objects
	 * @param obj
	 */
	private void processObject(Object obj) {
		if(obj instanceof String){
			processString((String) obj);
		}
	}
	
	/**
	 * Parse received strings
	 * @param obj
	 */
	@SuppressWarnings("unchecked")
	private void processString(String obj){
		//TODO da rimuovere
		_log.info("\n"+obj+"\n");
		
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
		else if(obj.equals(CommandStrings.RECONNECT)){
//			_returnObject.notify();
//			_returnObject = (boolean) getFromServer();
			_ui.reconnected();
		}
		else if(obj.equals(CommandStrings.UUID)){
			_ui.setUUID((String) getFromServer());
		}
		else if(obj.matches(CommandStrings.INITIAL_LEADER+"|"+CommandStrings.CHOOSE_LEADER)){
			List<LeaderCard> tempList = ((List<LeaderCard>) getFromServer());
			
			int i = _ui.chooseLeader(obj, tempList);
			queueToServer(obj, i);
		}
		else if(obj.equals(CommandStrings.INITIAL_PERSONAL_BONUS)){
			Map<String, List<Resource>> bonus = (Map<String, List<Resource>>) getFromServer();
			
			int i = _ui.chooseDashboardBonus(bonus);
			queueToServer(CommandStrings.INITIAL_PERSONAL_BONUS, i);
		}
		else if(obj.equals(CommandStrings.CHOOSE_CONVERT)){
			List<Resource> realPayOptions = (List<Resource>) getFromServer();
			List<Resource> realGainOptions = (List<Resource>) getFromServer();
			
			int i = _ui.chooseConvert(realPayOptions, realGainOptions);
			queueToServer(CommandStrings.CHOOSE_CONVERT, i);
		}
		else if(obj.equals(CommandStrings.CHOOSE_FAMILIAR)){
			List<FamilyMember> familiars = (List<FamilyMember>) getFromServer();
			String message = (String) getFromServer();
			
			int i = _ui.chooseFamiliar(familiars, message);
			queueToServer(CommandStrings.CHOOSE_FAMILIAR, i);
		}
		else if(obj.equals(CommandStrings.ASK_INT)){
			String message = (String) getFromServer();
			int min = (int) getFromServer();
			int max = (int) getFromServer();
			
			int i = _ui.askInt(message, min, max);
			
			queueToServer(CommandStrings.ASK_INT, i);
		}
		else if(obj.equals(CommandStrings.INFO)){
			_ui.showInfo((String) getFromServer());
		}
		else if(obj.equals(CommandStrings.INFO_BOARD)){
			String info = (String) getFromServer();
			GameBoard board = (GameBoard) getFromServer();
			_ui.showInfo(info, board);
		}
		else if(obj.equals(CommandStrings.INFO_PLAYER)){
			String info = (String) getFromServer();
			Player me = (Player) getFromServer();
			_ui.showInfo(info, me);
		}
		else if(obj.equals(CommandStrings.INFO_BOARD_PLAYER)){
			String message = (String) getFromServer();
			GameBoard board = (GameBoard) getFromServer();
			Player me = (Player) getFromServer();
			_ui.showInfo(message, board, me);
		}
		else if(obj.equals(CommandStrings.ASK_BOOLEAN)){
			boolean returned = _ui.askBoolean((String) getFromServer());
			
			queueToServer(CommandStrings.ASK_BOOLEAN, returned);
		}
		else if(obj.equals(CommandStrings.HANDLE_COUNCIL)){
			int i = _ui.spendCouncil((List<Resource>) getFromServer());
			
			queueToServer(CommandStrings.HANDLE_COUNCIL, i);
		}
		else if(obj.matches(CommandStrings.GAME_BOARD+"|"+CommandStrings.PLAYER+
				"|"+CommandStrings.END_TURN+"|"+CommandStrings.DROP_LEADER_CARD+
				"|"+CommandStrings.ACTIVATE_LEADER_CARD+"|"+CommandStrings.PLACE_FAMILIAR+
				"|"+CommandStrings.SHOW_VATICAN_SUPPORT+"|"+CommandStrings.ACTIVATE_OPT_LEADERS)){
			System.out.println("try to wake up");
			synchronized (_returnObject) {
				_returnObject.notify();
//				_returnObject = getFromServer();
				System.out.println("Wake up!");
			}
		}else{
			_log.log(Level.SEVERE, "\n###RICEVUTO COMANDO SCONOSCIUTO: "+obj+"###\n");
		}
	}
	
	/* (non-Javadoc)
	 * @see client.network.ConnectionServerHandler#sendConfigFile(java.lang.String)
	 */
	@Override
	public void sendConfigFile(String file) throws RemoteException {
			queueToServer(CommandStrings.ASK_FOR_CONFIG, file);
	}

	/* (non-Javadoc)
	 * @see client.network.ConnectionServerHandler#shutdown()
	 */
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
	
	/* (non-Javadoc)
	 * @see client.network.ConnectionServerHandler#addMeToGame(java.lang.String)
	 */
	@Override
	public boolean addMeToGame(String name) throws RemoteException {
		try {
			_returnObject = new Object();
			queueToServer(CommandStrings.ADD_TO_GAME, name);
			
			synchronized (_returnObject) {
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_returnObject.wait();
					break;
				}
			}
			
			return (boolean) _returnObject;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return false;
	}
	
	/**
	 * Write an object via the socket output stream
	 * @param message
	 * @throws RemoteException
	 */
	private void writeObject(Object message) throws RemoteException{
		try {
			System.out.println("\n###Sended "+message.toString()+"\n");
			_outputStream.writeUnshared(message);
			_outputStream.flush();
			_outputStream.reset();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			throw new RemoteException();
		}
		
	}
	
	/**
	 * Read an object from the {@link ObjectInputStream}. Cannot return <code>null</code> value.
	 * @return the object read
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private void fetchFromServer() {
		try {
			do {
				Object obj = null;
				synchronized (_inputStream) {
					obj = _inputStream.readObject();
				}
				
				if (obj!=null) {
					_log.info(obj.toString());
					synchronized (_fromServerToClient) {
						_fromServerToClient.add(obj);
					}
				} else {
					Thread.sleep(500);
				}
			} while (_isRunning);
		} catch (ClassNotFoundException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		} catch(InterruptedException e){
			Thread.currentThread().interrupt();
			_log.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException ioe) {
			_log.log(Level.SEVERE, ioe.getMessage(), ioe);
			shutdown();
		}
		
		System.out.println("Errore");
		synchronized (_fromServerToClient) {
			_fromServerToClient.add(CommandStrings.END_TURN);
		}
	}
	
	/**
	 * Pull from the socket input stream
	 * @return the object received
	 */
	private Object getFromServer(){
		do{
			Object obj = null;
			synchronized (_fromServerToClient) {
				obj = _fromServerToClient.poll();
			}
			if(obj!=null){
				return obj;
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					_log.log(Level.SEVERE, e.getMessage(), e);
					Thread.currentThread().interrupt();
				}
			}
		} while(true);
	}

	/* (non-Javadoc)
	 * @see client.network.ConnectionServerHandler#endTurn()
	 */
	@Override
	public void endTurn() throws RemoteException {
		queueToServer(CommandStrings.END_TURN);
	}
	
	/* (non-Javadoc)
	 * @see client.network.ConnectionServerHandler#dropLeaderCard(java.lang.String)
	 */
	@Override
	public void dropLeaderCard(String leaderName) throws RemoteException {
		
		if(primo){
			primo = false;
			throw new RemoteException();
		}
		
		try{
			_returnObject = new Object();
			
			queueToServer(CommandStrings.DROP_LEADER_CARD, leaderName);
			
			synchronized (_returnObject) {
				System.out.println("\nWaiting...\n");
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_returnObject.wait();
					break;
				}
			}
			
			if(_returnObject.equals(CommandStrings.CONNECTION_ERROR)){
				throw new RemoteException();
			}
			
			return;
		} catch (InterruptedException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			Thread.currentThread().interrupt();
		}
	}
	
	/* (non-Javadoc)
	 * @see client.network.ConnectionServerHandler#showVaticanSupport(java.lang.Integer)
	 */
	@Override
	public void showVaticanSupport() throws RemoteException {
		try{
			_returnObject = new Object();
			
			queueToServer(CommandStrings.SHOW_VATICAN_SUPPORT);
			
			synchronized (_returnObject) {
				System.out.println("\nWaiting...\n");
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_returnObject.wait();
					break;
				}
			}
			
			if(_returnObject.equals(CommandStrings.CONNECTION_ERROR)){
				throw new RemoteException();
			}
			
			return;
		} catch (InterruptedException e){
			_log.log(Level.SEVERE, e.getMessage(), e);
			Thread.currentThread().interrupt();
		}
	}
	
	/* (non-Javadoc)
	 * @see client.network.ConnectionServerHandler#activateOPTLeaders()
	 */
	@Override
	public void activateOPTLeaders() throws RemoteException {
		try{
			_returnObject = new Object();
			
			queueToServer(CommandStrings.ACTIVATE_OPT_LEADERS);
			
			synchronized (_returnObject) {
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_returnObject.wait();
					break;
				}
			}
			
			if(_returnObject.equals(CommandStrings.CONNECTION_ERROR)){
				throw new RemoteException();
			}
			
			return;
		} catch (InterruptedException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			Thread.currentThread().interrupt();
		}
	}

	/* (non-Javadoc)
	 * @see client.network.ConnectionServerHandler#activateLeaderCard(java.lang.String)
	 */
	@Override
	public void activateLeaderCard(String leaderName) throws RemoteException {
		try{
			_returnObject = new Object();
			
			queueToServer(CommandStrings.ACTIVATE_LEADER_CARD, leaderName);
			
			synchronized (_returnObject) {
				System.out.println("\nWaiting...\n");
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_returnObject.wait();
					break;
				}
			}
			
			if(_returnObject.equals(CommandStrings.CONNECTION_ERROR)){
				throw new RemoteException();
			}
			
			return;
		} catch (InterruptedException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			Thread.currentThread().interrupt();
		}
	}

	/* (non-Javadoc)
	 * @see client.network.ConnectionServerHandler#placeFamiliar(java.lang.String, game.Position)
	 */
	@Override
	public void placeFamiliar(String familiarName, Position position) throws RemoteException {
		try{
			_returnObject = new Object();
			
			queueToServer(CommandStrings.PLACE_FAMILIAR, familiarName, position);
			
			synchronized (_returnObject) {
				System.out.println("\nWaiting...\n");
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_returnObject.wait();
					break;
				}
			}
			
			if(_returnObject.equals(CommandStrings.CONNECTION_ERROR)){
				throw new RemoteException();
			}
			
			return;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	/**
	 * Put in the exit queue
	 * @param objects
	 */
	private void queueToServer(Object...objects){
		synchronized (_fromClientToServer) {
			for(Object obj : objects){
				_fromClientToServer.add(obj);
			}
		}
		
	}

	private Object _returnObject = new Object();
	
	private Thread _reader;
	private Thread _processor;
	private Thread _writer;
	
	private Socket _socket;
	private ObjectInputStream _inputStream;
	private ObjectOutputStream _outputStream;
	private final Logger _log = Logger.getLogger(SocketConnectionServerHandler.class.getName());
	
	private ConcurrentLinkedQueue<Object> _fromServerToClient = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<Object> _fromClientToServer = new ConcurrentLinkedQueue<>();
	
	/**
	 * Runnable that keeps reading from server
	 * @author Jacopo
	 *
	 */
	private class MyReader implements Runnable{
		@Override
		public void run() {
			while (_isRunning) {
				try{
					System.out.println("Reading...");
					fetchFromServer();
				}catch(Exception e){
					_log.log(Level.SEVERE, e.getMessage(), e);
				}
			}
			
			System.out.println("\n\n###READER SHUTTING DOWN###\n\n");
		}
	}
	
	/**
	 * Runnable that keeps processing messages
	 * @author Jacopo
	 *
	 */
	private class MyProcessor implements Runnable{

		@Override
		public void run() {
			while(_isRunning){
				processObject(getFromServer());
			}
		}
	}
	
	/**
	 * Runnable that keeps write to the server
	 * @author Jacopo
	 *
	 */
	private class MyWriter implements Runnable{

		@Override
		public void run() {
			while(isRunning()){
				synchronized (_fromClientToServer) {
					if(!_fromClientToServer.isEmpty()){
						try {
							writeObject(_fromClientToServer.poll());
//							_returnObject = new Object();
//							_returnObject.notify();
						} catch (RemoteException e) {
							_log.log(Level.INFO, e.getMessage(), e);
							_returnObject = CommandStrings.CONNECTION_ERROR;
							_returnObject.notify();
						}
					}
				}
				
			}
		}
		
	}
}
