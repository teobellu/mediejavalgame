package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.FamilyMember;
import model.GameBoard;
import model.LeaderCard;
import model.Player;
import model.Position;
import model.Resource;
import model.exceptions.GameException;
import util.CommandStrings;

public class SocketConnectionHandler extends ConnectionHandler {
	
	/**
	 * Monitoring object for communication
	 */
	private Object _returnObject = new Object();
	/**
	 * The socket
	 */
	private Socket _socket;
	/**
	 * The input communication stream
	 */
	private ObjectInputStream _inputStream;
	/**
	 * The output communication stream
	 */
	private ObjectOutputStream _outputStream;
	/**
	 * The logger
	 */
	private Logger _log = Logger.getLogger(SocketConnectionHandler.class.getName());

	/**
	 * The input queue of objects
	 */
	private ConcurrentLinkedQueue<Object> _fromClientToServer = new ConcurrentLinkedQueue<>();
	/**
	 * The output queue of objects
	 */
	private ConcurrentLinkedQueue<Object> _fromServerToClient = new ConcurrentLinkedQueue<>();
	
	/**
	 * Thread fetching commands
	 */
	private Thread _reader;
	/**
	 * Thread processing commands
	 */
	private Thread _processor;
	/**
	 * Thread writing objects
	 */
	private Thread _writer;

	/**
	 * Constructor
	 * @param socket the socket
	 */
	public SocketConnectionHandler(Socket socket) {
		try {
			_socket = socket;

			_outputStream = new ObjectOutputStream(_socket.getOutputStream());
			_inputStream = new ObjectInputStream(_socket.getInputStream());

			_reader = new Thread(new MyRunnable());
			_processor = new Thread(new MyProcessor());
			_writer = new Thread(new MyWriter());
			
			_isRunning = true;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}

	}

	@Override
	public void run() {
		_reader.start();
		_processor.start();
		_writer.start();
	}

	/**
	 * @throws IOException
	 */
	private void fetchFromClient() throws IOException {
		Object object = null;
		while(_isRunning){
			synchronized (_inputStream) {
				try {
					object = _inputStream.readObject();
				} catch (ClassNotFoundException e) {
					_log.log(Level.INFO, e.getMessage(), e);
					object=null;
				}
			}
			if(object!=null){
				synchronized (_fromClientToServer) {
					_fromClientToServer.add(object);
				}
			} else {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					_log.log(Level.SEVERE, e.getMessage(), e);
					Thread.currentThread().interrupt();
				}
			}
		}
		shutdown();
	}

	private void processObject(Object obj) throws RemoteException {
		if (obj instanceof String) {
			processString((String) obj);
		}
	}

	private void processString(String str) throws RemoteException {
		if (str.equals(CommandStrings.ADD_TO_GAME)) {
			String name = (String) getFromClient();
			queueToClient(CommandStrings.ADD_TO_GAME, Server.getInstance().addMeToGame(this, name));
			sendUUID(_client.getUUID());
		}
		else if (str.equals(CommandStrings.DROP_LEADER_CARD)) {
			String leaderName = (String) getFromClient();
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						synchronized (this) {
							_theGame.getListener().dropLeaderCard(_client.getName(), leaderName);
							queueToClient(CommandStrings.DROP_LEADER_CARD);
						}
					} catch (GameException e) {
						_log.log(Level.OFF, e.getMessage(), e);
						try {
							queueToClient(CommandStrings.DROP_LEADER_CARD);
							sendInfo(e.getMessage());
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							_log.log(Level.SEVERE, e1.getMessage(), e1);
						}
					}
				}
			}).start();
		} 
		else if(str.equals(CommandStrings.ACTIVATE_LEADER_CARD)){
			String leaderName = (String) getFromClient();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						synchronized (this) {
							_theGame.getListener().activateLeaderCard(_client.getName(), leaderName);
							_returnObject = new Object();
							queueToClient(CommandStrings.ACTIVATE_LEADER_CARD);
						}
					} catch (GameException e) {
						_log.log(Level.OFF, e.getMessage(), e);
						try {
							queueToClient(CommandStrings.ACTIVATE_LEADER_CARD);
							sendInfo(e.getMessage());
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							_log.log(Level.SEVERE, e1.getMessage(), e1);
						}
					}
				}
			}).start();
		}
		else if(str.equals(CommandStrings.PLACE_FAMILIAR)){
			String familiarName = (String) getFromClient();
			Position pos = (Position) getFromClient();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						synchronized (this) {
							_theGame.getListener().placeFamiliar(_client.getName(), familiarName, pos);
							_returnObject = new Object();
							queueToClient(CommandStrings.PLACE_FAMILIAR);
						}
					} catch (GameException e) {
						_log.log(Level.OFF, e.getMessage(), e);
						try {
							queueToClient(CommandStrings.PLACE_FAMILIAR);
							sendInfo(e.getMessage());
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							_log.log(Level.SEVERE, e1.getMessage(), e1);
						}
					}
				}
			}).start();
		}
		else if(str.equals(CommandStrings.END_TURN)){
			try {
				synchronized (this) {
					_theGame.getListener().endTurn(_client.getName());
				}
			} catch (GameException e) {
				_log.log(Level.OFF, e.getMessage(), e);
				sendInfo(e.getMessage());
			}
		}
		else if(str.equals(CommandStrings.SHOW_VATICAN_SUPPORT)){
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						_theGame.getListener().showVaticanSupport(_client.getName());
						queueToClient(CommandStrings.SHOW_VATICAN_SUPPORT);
					} catch (GameException e) {
						_log.log(Level.OFF, e.getMessage(), e);
						try {
							queueToClient(CommandStrings.SHOW_VATICAN_SUPPORT);
							sendInfo(e.getMessage());
						} catch (RemoteException e1) {
							_log.log(Level.SEVERE, e1.getMessage(), e1);
						}
					}
				}
			}).start();
		}
		else if(str.equals(CommandStrings.ACTIVATE_OPT_LEADERS)){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						_theGame.getListener().playOPTLeaderCards(_client.getName());
						queueToClient(CommandStrings.ACTIVATE_OPT_LEADERS);
					} catch (GameException e) {
						_log.log(Level.OFF, e.getMessage(), e);
						try {
							queueToClient(CommandStrings.ACTIVATE_OPT_LEADERS);
							sendInfo(e.getMessage());
						} catch (RemoteException e2) {
							// TODO: handle exception
							_log.log(Level.SEVERE, e2.getMessage(), e2);
						}
					}
				}
			}).start();
		}
		else if(str.equals(CommandStrings.ASK_FOR_CONFIG)){
			_configFile = (String) getFromClient();
		}
		else if(str.matches(CommandStrings.INITIAL_LEADER+"|"+CommandStrings.HANDLE_COUNCIL
				+"|"+CommandStrings.INITIAL_PERSONAL_BONUS+"|"+CommandStrings.CHOOSE_CONVERT
				+"|"+CommandStrings.CHOOSE_FAMILIAR+"|"+CommandStrings.ASK_INT
				+"|"+CommandStrings.ASK_BOOLEAN+"|"+CommandStrings.CHOOSE_LEADER)){
			synchronized (_returnObject) {
				_returnObject.notify();
				_returnObject = getFromClient();
			}
		}
		else if(str.equals(CommandStrings.RECONNECT)){
			String uuid = (String) getFromClient();
			attemptReconnection(uuid);
		} 
		else {
			_log.log(Level.SEVERE, "\n###RICEVUTO COMANDO SCONOSCIUTO###\n");
		}
	}

	private void attemptReconnection(String uuid) {
		for(Room room : Server.getInstance().getRooms()){
			for(Client client : room.getPlayers()){
				if(client.getUUID().equals(uuid)){
					client.setConnectionHandler(this);
					_client = client;
					_theGame = _client.getRoom().getGame();
					queueToClient(CommandStrings.RECONNECT, true);
				}
			}
		}
	}

	@Override
	public void shutdown() {
		super.shutdown();
		try {
			if (_inputStream != null) {
				_inputStream.close();
			}

			if (_outputStream != null) {
				_outputStream.close();
			}

			if (!_socket.isClosed()) {
				_socket.close();
			}
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void writeObject(Object obj) throws RemoteException  {
		try {
			_outputStream.writeUnshared(obj);
			_outputStream.flush();
			_outputStream.reset();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			throw new RemoteException();
		}
	}

	@Override
	public int spendCouncil(List<Resource> councilRewards) throws RemoteException {
		_returnObject = new Object();
		int selection = 0;
		try {
			queueToClient(CommandStrings.HANDLE_COUNCIL, councilRewards);
			
			synchronized (_returnObject) {
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_returnObject.wait();
					break;
				}
			}
			
			return (int) _returnObject;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		return selection;
	}

	@Override
	public int chooseFamiliar(List<FamilyMember> familiars, String message) throws RemoteException {
		try {
			_returnObject = new Object();
			queueToClient(CommandStrings.CHOOSE_FAMILIAR, familiars, message);
			
			synchronized (_returnObject) {
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_returnObject.wait();
					break;
				}
			}
			
			return (int) _returnObject;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		
		return 0;
	}

	@Override
	public boolean askBoolean(String message) throws RemoteException {
		try {
			_returnObject = new Object();
			queueToClient(CommandStrings.ASK_BOOLEAN, message);
			
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

	@Override
	public int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions) throws RemoteException {
		try {
			_returnObject = new Object();
			queueToClient(CommandStrings.CHOOSE_CONVERT, realPayOptions, realGainOptions);
			
			synchronized (_returnObject) {
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_returnObject.wait();
					break;
				}
			}
			
			return (int) _returnObject;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		
		return 0;
	}
	
	@Override
	public int askInt(String message, int min, int max) throws RemoteException {
		try {
			_returnObject = new Object();
			queueToClient(CommandStrings.ASK_INT, message, min, max);
			
			synchronized (_returnObject) {
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_returnObject.wait();
					break;
				}
			}
			
			return (int) _returnObject;
			
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		return 0;
	}

	@Override
	public void startTurn(GameBoard board, Player currentPlayer) throws RemoteException {
		queueToClient(CommandStrings.START_TURN, board, currentPlayer);
	}

	@Override
	public int chooseLeader(String context, List<LeaderCard> leadersList) throws RemoteException {
		try {
			_returnObject = new Object();
			queueToClient(context, leadersList);
			
			synchronized (_returnObject) {
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_returnObject.wait();
					break;
				}
			}
			
			return (int) _returnObject;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return 0;
	}

	@Override
	public int chooseDashboardBonus(Map<String, List<Resource>> bonus) throws RemoteException {
		try {
			queueToClient(CommandStrings.INITIAL_PERSONAL_BONUS, bonus);
			
			synchronized (_returnObject) {
				while(Thread.currentThread().getState()!=Thread.State.WAITING){
					_returnObject.wait();
					break;
				}
			}
			
			return (int) _returnObject;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return 0;
	}
	
	@Override
	public void sendInfo(String infoMessage) throws RemoteException {
		queueToClient(CommandStrings.INFO, infoMessage);
	}
	
	@Override
	public void sendInfo(String infoMessage, GameBoard board) throws RemoteException {
		queueToClient(CommandStrings.INFO_BOARD, infoMessage, board);
	}
	
	@Override
	public void sendInfo(String message, Player me) throws RemoteException {
		queueToClient(CommandStrings.INFO_PLAYER, message, me);
	}

	@Override
	public void sendInfo(String message, GameBoard board, Player me) throws RemoteException {
		queueToClient(CommandStrings.INFO_BOARD_PLAYER, message, board, me);
	}
	
	private Object getFromClient(){
		do{
			Object obj = null;
			obj = _fromClientToServer.poll();
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
	
	@Override
	public void sendUUID(String uuid) throws RemoteException {
		queueToClient(CommandStrings.UUID, uuid);
	}
	
	private void queueToClient(Object...objects){
		for(Object obj : objects){
			_fromServerToClient.add(obj);
		}
	}
	
	private class MyRunnable implements Runnable{

		@Override
		public void run() {
			try {
				while (_isRunning) {
					fetchFromClient();
				}
			} catch (IOException e) {
				shutdown();
				_log.log(Level.SEVERE, e.getMessage(), e);
			} finally {
			}
		}
		
	}
	
	private class MyProcessor implements Runnable{

		@Override
		public void run() {
			while(_isRunning){
//				try {
					Object obj = getFromClient();
//					new Thread(new Runnable() {
//						
//						@Override
//						public void run() {
							try {
								processObject(obj);
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								_log.log(Level.SEVERE, e.getMessage(), e);
							}
//						}
//					}).start();
////					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
			}
		}
		
	}
	
	private class MyWriter implements Runnable{

		@Override
		public void run() {
			while(_isRunning){
				if(!_fromServerToClient.isEmpty()){
					try {
						writeObject(_fromServerToClient.poll());
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						_log.log(Level.SEVERE, e.getMessage(), e);
					}
				} else {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						_log.log(Level.SEVERE, e.getMessage(), e);
						Thread.currentThread().interrupt();
					}
				}
			}
		}
		
	}
}
