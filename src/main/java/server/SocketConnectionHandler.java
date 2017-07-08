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

import exceptions.GameException;
import game.FamilyMember;
import game.GameBoard;
import game.LeaderCard;
import game.Player;
import game.Position;
import game.Resource;
import util.CommandStrings;

public class SocketConnectionHandler extends ConnectionHandler {
	
	private Object _returnObject = new Object();
	private Socket _socket;
	private ObjectInputStream _inputStream;
	private ObjectOutputStream _outputStream;
	private Logger _log = Logger.getLogger(SocketConnectionHandler.class.getName());

	private ConcurrentLinkedQueue<Object> _fromClientToServer = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<Object> _fromServerToClient = new ConcurrentLinkedQueue<>();
	
	private Thread _reader;
	private Thread _processor;
	private Thread _writer;

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

	private void fetchFromClient() throws IOException {
		Object object = null;
		while(_isRunning){
			synchronized (_inputStream) {
				try {
					object = _inputStream.readObject();
				} catch (ClassNotFoundException e) {
					object=null;
				}
			}
			if(object!=null){
				System.out.println("Ricevuto "+object.toString());
				System.out.println("Tento di bloccare _fromClientToServer");
				synchronized (_fromClientToServer) {
					System.out.println("blocco _fromClientToServer");
					_fromClientToServer.add(object);
				}
				System.out.println("Lascio blocco su _fromClientToServer");
			} else {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					_log.log(Level.SEVERE, e.getMessage(), e);
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
		_log.info(str);
		//TODO togliere il log
		
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
						}
					} catch (GameException e) {
						try {
							sendInfo(e.getMessage());
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
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
						}
					} catch (GameException e) {
						try {
							sendInfo(e.getMessage());
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
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
						}
					} catch (GameException e) {
						try {
							sendInfo(e.getMessage());
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
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
				sendInfo(e.getMessage());
			}
		}
		else if(str.equals(CommandStrings.GAME_BOARD)){
			GameBoard board = _theGame.getListener().getGameBoard();
			queueToClient(CommandStrings.GAME_BOARD, board);
		}
		else if(str.equals(CommandStrings.ASK_FOR_CONFIG)){
			_configFile = (String) getFromClient();
		}
		else if(str.equals(CommandStrings.PLAYER)){
			Player player = _theGame.getListener().getMe(_client.getName());
			System.out.println("\nQuesto player ha "+player.getLeaderCards().size()+" carte leader\n");
			queueToClient(CommandStrings.PLAYER, player);
		}
		else if(str.matches(CommandStrings.INITIAL_LEADER+"|"+CommandStrings.HANDLE_COUNCIL
				+"|"+CommandStrings.INITIAL_PERSONAL_BONUS+"|"+CommandStrings.CHOOSE_CONVERT
				+"|"+CommandStrings.CHOOSE_FAMILIAR+"|"+CommandStrings.ASK_INT
				+"|"+CommandStrings.ASK_BOOLEAN)){
			synchronized (_returnObject) {
				_returnObject.notify();
				_returnObject = getFromClient();
			}
		} else {
			_log.log(Level.SEVERE, "\n###RICEVUTO COMANDO SCONOSCIUTO###\n");
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
		System.out.println("\n###Sended "+obj.toString()+"\n");
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
				_returnObject.wait();
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
				_returnObject.wait();
			}
			
			return (int) _returnObject;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		System.out.println("ERRORE");
		
		return 0;
	}

	@Override
	public boolean askBoolean(String message) throws RemoteException {
		try {
			_returnObject = new Object();
			queueToClient(CommandStrings.ASK_BOOLEAN, message);
			
			synchronized (_returnObject) {
				_returnObject.wait();
			}
			
			return (boolean) _returnObject;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		System.out.println("ERRORE");
		
		return false;
	}

	@Override
	public int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions) throws RemoteException {
		try {
			_returnObject = new Object();
			queueToClient(CommandStrings.CHOOSE_CONVERT, realPayOptions, realGainOptions);
			
			synchronized (_returnObject) {
				_returnObject.wait();
			}
			
			return (int) _returnObject;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		System.out.println("ERRORE");
		
		return 0;
	}
	
	@Override
	public int askInt(String message, int min, int max) throws RemoteException {
		try {
			_returnObject = new Object();
			queueToClient(CommandStrings.ASK_INT, message, min, max);
			
			synchronized (_returnObject) {
				_returnObject.wait();
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
				_returnObject.wait();
			}
			
			return (int) _returnObject;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		System.out.println("ERROR: LEADER NOT FOUND");
		return 0;
	}

	@Override
	public int chooseDashboardBonus(Map<String, List<Resource>> bonus) throws RemoteException {
		try {
			queueToClient(CommandStrings.INITIAL_PERSONAL_BONUS, bonus);
			
			synchronized (_returnObject) {
				_returnObject.wait();
			}
			
			return (int) _returnObject;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		System.out.println("ERROR: PERSONAL BONUS NOT FOUND");
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
				System.out.println("getFromClient ritorna "+obj.toString());
				return obj;
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					_log.log(Level.SEVERE, e.getMessage(), e);
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
			System.out.println("Aggiunto in uscita "+obj.toString());
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
				System.out.println("\n\n###READER SHUTTING DOWN###\n\n");
			}
		}
		
	}
	
	private class MyProcessor implements Runnable{

		@Override
		public void run() {
			while(_isRunning){
				try {
					processObject(getFromClient());
				} catch (RemoteException e) {
					// TODO gestire disconnessione
					_log.log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
		
	}
	
	private class MyWriter implements Runnable{

		@Override
		public void run() {
			while(_isRunning){
				synchronized (_fromServerToClient) {
					if(!_fromServerToClient.isEmpty()){
						System.out.println("Exit queue not empty! ");
						try {
							System.out.println("Trying to send... ");
							writeObject(_fromServerToClient.poll());
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			}
		}
		
	}
}
