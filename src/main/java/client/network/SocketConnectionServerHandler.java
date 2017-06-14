package client.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
			
			_log.info("Socket Connection is up");
			
			_isRunning = true;
			
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	@Override
	public void run() {
		
	}
	
	@Override
	public List<String> putFamiliar() throws RemoteException {
			String message = CommandStrings.PUT_FAMILIAR;
			writeObject(message);
			
			List<String> familiars = new ArrayList<String>();
			while(message!=CommandStrings.END_TRANSMISSION){
				message = (String) readObject();
				familiars.add(message);
			}
			
			synchronized (familiars) {
				
			}
			
			return familiars;
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
				message = (String) readObject();
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
	public void activateLeaderCard() throws RemoteException {
			String message = CommandStrings.ACTIVATE_LEADER_CARD;
			writeObject(message);
	}
	
	@Override
	public void activateLeaderCard(String card){
			String message = CommandStrings.ACTIVATE_WHICH_LEADER_CARD;
			writeObject(message);
			writeObject(card);
			
			//TODO se Federico da Montefeltro, chiedi quale familiare
			//TODO se Lorenzo de� Medici, chiedi quale altro leader
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
	public void onConnect() throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean addMeToGame() throws RemoteException {
		try {
			writeObject(CommandStrings.ADD_TO_GAME);
			if (readObject().equals(CommandStrings.ADD_TO_GAME)) {
				return true;
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return false;
	}
	
	private synchronized void writeObject(Object message){
		try {
			_outputStream.writeObject(message);
			_outputStream.flush();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
	}
	
	/**
	 * Read an object from the {@link ObjectInputStream}. Cannot return <code>null</code> value.
	 * @return the object read
	 */
	private synchronized Object readObject() {
		try {
			do {
				Object obj = _inputStream.readObject();
				if (obj!=null) {
					return obj;
				} else {
					Thread.sleep(500);
				}
			} while (_isRunning);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		return CommandStrings.END_TRANSMISSION;
	}
	
	@Override
	public void sendInitialInformations(String name) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doIspendMyFaithPoints(boolean doI) throws RemoteException {
		writeObject(CommandStrings.SPEND_FAITH_POINTS);
		writeObject(Boolean.toString(doI));
	}

	@Override
	public boolean endTurn() throws RemoteException {
		writeObject(CommandStrings.END_TURN);
		String response = (String) readObject();
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
			str = (String) readObject();
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

	@Override
	public void spendCouncilPrivilege(String resource) throws RemoteException {
		writeObject(CommandStrings.HANDLE_COUNCIL);
		writeObject(resource);
	}
	
	@Override
	public String readResponse() throws RemoteException {
		return (String) readObject();
	}
	
	private Socket _socket;
	private ObjectInputStream _inputStream;
	private ObjectOutputStream _outputStream;
	private final Logger _log = Logger.getLogger(SocketConnectionServerHandler.class.getName());
}
