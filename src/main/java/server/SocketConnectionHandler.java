package server;

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
import game.Resource;
import util.CommandStrings;

public class SocketConnectionHandler extends ConnectionHandler {
	
	private Object _returnObject;
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

			_reader = new Thread(new Runnable() {

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
			});

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
		} else {
			// TODO manderemo mai cose che non sono stringhe?
			// TODO se sì, le manderemo in entrambe le direzioni?
		}
	}

	private void processString(String str) throws IOException {
		System.out.println("Letto: " + str);
		if (str.equals(CommandStrings.ADD_TO_GAME)) {
			String name = (String) getFromClient();
			if (Server.getInstance().addMeToGame(this, name)) {
				writeObject(CommandStrings.ADD_TO_GAME);
			} else {
				writeObject(CommandStrings.ERROR);
			}
		} 
		else if (str.equals(CommandStrings.DROP_LEADER_CARD)) {
			List<String> leaders = new ArrayList<>();
			try {
				leaders = _theGame.getState().dropLeaderCard();
			} catch (GameException e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
			}
			writeObject(leaders);
		} 
		else if(str.matches(CommandStrings.INITIAL_LEADER+"|"+CommandStrings.HANDLE_COUNCIL)){
			_returnObject = getFromClient();
			_returnObject.notify();
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

	@Override
	public void onConnect() throws RemoteException {
		// TODO
	}

	private synchronized void writeObject(Object obj) throws IOException {
		_outputStream.writeObject(obj);
		_outputStream.flush();
		System.out.println("mandata risposta");
	}

	/**
	 * Send the command {@link CommandStrings}.GAME_BOARD, then the {@link GameBoard} itself
	 * @param board the gameboard
	 */
	public void sendBoard(GameBoard board) {
		try {
			writeObject(CommandStrings.GAME_BOARD);
			writeObject(board);
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	public int spendCouncil(List<Resource> councilRewards) throws RemoteException {
		int selection = 0;
		try {
			writeObject(CommandStrings.HANDLE_COUNCIL);
			writeObject(councilRewards);
			
			_returnObject.wait();
			
			return (int) _returnObject;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		return selection;
	}

	@Override
	public int sendInitialLeaderList(List<String> leadersList) throws RemoteException {
		try {
			writeObject(CommandStrings.INITIAL_LEADER);
			writeObject(leadersList);
			
			_returnObject.wait();
			
			return (int) _returnObject;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		
		return 0;
	}

	@Override
	public int chooseFamiliar(List<FamilyMember> familiars, String message) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean ask(String message) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void startTurn() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int chooseLeader(List<LeaderCard> tempList) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int chooseDashboardBonus(Map<String, List<Resource>> bonus) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void notifyTurn() throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}
