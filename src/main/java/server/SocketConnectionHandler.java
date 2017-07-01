package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
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

public class SocketConnectionHandler extends ConnectionHandler {
	
	private Object _returnObject = new Object();
	private Socket _socket;
	private ObjectInputStream _inputStream;
	private ObjectOutputStream _outputStream;
	private Logger _log = Logger.getLogger(SocketConnectionHandler.class.getName());

	private Thread _reader;

	public SocketConnectionHandler(Socket socket) {
		try {
			_socket = socket;

			_outputStream = new ObjectOutputStream(_socket.getOutputStream());
			_inputStream = new ObjectInputStream(_socket.getInputStream());

			_reader = new Thread(new MyRunnable());
			
			_isRunning = true;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}

	}

	@Override
	public void run() {
		_reader.start();
	}

	private Object getFromClient() throws IOException {
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
				return object;
			} else {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					_log.log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
		shutdown();
		return null;
	}

	private void processObject(Object obj) throws IOException {
		if (obj instanceof String) {
			processString((String) obj);
		}
	}

	private void processString(String str) throws IOException {
		_log.info(str);
		//TODO togliere il log
		
		if (str.equals(CommandStrings.ADD_TO_GAME)) {
			String name = (String) getFromClient();
			writeObject(CommandStrings.ADD_TO_GAME);
			writeObject(Server.getInstance().addMeToGame(this, name));
		}
		else if(str.equals(CommandStrings.PLACE_FAMILIAR)){
			String familiarName = (String) getFromClient();
			Position position = (Position) getFromClient();
			try {
				synchronized (this) {
					_theGame.getListener().placeFamiliar(familiarName, position);
				}
				writeObject(CommandStrings.PLACE_FAMILIAR);
				writeObject(CommandStrings.SUCCESS);
			} catch (GameException e) {
				writeObject(CommandStrings.PLACE_FAMILIAR);
				writeObject(CommandStrings.ERROR);
			}
		}
		else if (str.equals(CommandStrings.DROP_LEADER_CARD)) {
			String leaderName = (String) getFromClient();
			try {
				synchronized (this) {
					_theGame.getListener().dropLeaderCard(leaderName);
				}
				
				writeObject(CommandStrings.DROP_LEADER_CARD);
				writeObject(CommandStrings.SUCCESS);
			} catch (GameException e) {
				writeObject(CommandStrings.DROP_LEADER_CARD);
				writeObject(CommandStrings.ERROR);
			}
		} 
		else if(str.equals(CommandStrings.ACTIVATE_LEADER_CARD)){
			String leaderName = (String) getFromClient();
			try {
				synchronized (this) {
					_theGame.getListener().activateLeaderCard(leaderName);
				}
				
				writeObject(CommandStrings.ACTIVATE_LEADER_CARD);
				writeObject(CommandStrings.SUCCESS);
			} catch (GameException e) {
				writeObject(CommandStrings.ACTIVATE_LEADER_CARD);
				writeObject(CommandStrings.ERROR);
			}
		}
		else if(str.equals(CommandStrings.PLACE_FAMILIAR)){
			//TODO
		}
		else if(str.equals(CommandStrings.END_TURN)){
			try {
				synchronized (this) {
					_theGame.getListener().endTurn();
				}
				
				writeObject(CommandStrings.END_TURN);
				writeObject(CommandStrings.SUCCESS);
			} catch (GameException e) {
				writeObject(CommandStrings.END_TURN);
				writeObject(CommandStrings.ERROR);
			}
		}
		else if(str.equals(CommandStrings.GAME_BOARD)){
			GameBoard board = _theGame.getBoard();
			writeObject(CommandStrings.GAME_BOARD);
			writeObject(board);
		}
		else if(str.equals(CommandStrings.ASK_FOR_CONFIG)){
			_configFile = (String) getFromClient();
		}
		else if(str.equals(CommandStrings.PLAYER)){
			Player player = _theGame.getCurrentPlayer();
			writeObject(CommandStrings.PLAYER);
			writeObject(player);
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

	private synchronized void writeObject(Object obj) throws IOException {
		_outputStream.writeUnshared(obj);
		_outputStream.flush();
	}

	@Override
	public int spendCouncil(List<Resource> councilRewards) throws RemoteException {
		_returnObject = new Object();
		int selection = 0;
		try {
			writeObject(CommandStrings.HANDLE_COUNCIL);
			writeObject(councilRewards);
			
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
			writeObject(CommandStrings.CHOOSE_FAMILIAR);
			writeObject(familiars);
			writeObject(message);
			
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
			writeObject(CommandStrings.ASK_BOOLEAN);
			writeObject(message);
			
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
			writeObject(CommandStrings.CHOOSE_CONVERT);
			writeObject(realPayOptions);
			writeObject(realGainOptions);
			
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
			writeObject(CommandStrings.ASK_INT);
			writeObject(message);
			writeObject(min);
			writeObject(max);
			
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
		try {
			writeObject(CommandStrings.START_TURN);
			writeObject(board);
			writeObject(currentPlayer);
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	public int chooseLeader(String context, List<LeaderCard> leadersList) throws RemoteException {
		try {
			_returnObject = new Object();
			writeObject(context);
			writeObject(leadersList);
			
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
			writeObject(CommandStrings.INITIAL_PERSONAL_BONUS);
			writeObject(bonus);
			
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
		try {
			writeObject(CommandStrings.INFO);
			writeObject(infoMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	private class MyRunnable implements Runnable{

		@Override
		public void run() {
			try {
				while (_isRunning) {
					Object obj = getFromClient();
					if (obj != null) {
						processObject(obj);
					}
				}
			} catch (IOException e) {
				shutdown();
				_log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		
	}
}
