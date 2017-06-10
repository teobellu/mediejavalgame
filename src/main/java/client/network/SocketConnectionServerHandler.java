package client.network;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import util.CommandStrings;

/**
 * @author Jacopo
 *
 */
public class SocketConnectionServerHandler extends ConnectionServerHandler {

	public SocketConnectionServerHandler(String host, int port) {
		super(host, port);
	}
	
	@Override
	public void run() {
		try {
			
			_socket = new Socket(_host, _port);
			
			_inputStream = new ObjectInputStream(_socket.getInputStream());
			_outputStream = new ObjectOutputStream(_socket.getOutputStream());
			
			_isRunning = true;
			
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	
	@Override
	public void sendName(String name) throws RemoteException {
		try {
			String message = CommandStrings.SEND_NAME;
			writeObject(message);
			message = name;
			writeObject(message);
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
	}

	@Override
	public List<String> putFamiliar() throws RemoteException {
		try {
			String message = CommandStrings.PUT_FAMILIAR;
			writeObject(message);
			
			List<String> familiars = new ArrayList<String>();
			while(message!=CommandStrings.END_TRANSMISSION){
				message = (String) readObject();
				if(message!=null){
					familiars.add(message);
				}
			}
			
			synchronized (familiars) {
				
			}
			
			return familiars;
			//TODO quale familiare
			//TODO dove piazzarlo
			//TODO aumentare il suo valore? Se s�, di quanto?
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			return null;
		}
		
	}
	
	@Override
	public List<String> putFamiliarWhich(String familiar) throws RemoteException {
		try{
			String message = CommandStrings.PUT_WHICH_FAMILIAR;
			writeObject(message);
			writeObject(familiar);
			List<String> positions = new ArrayList<>();
			while(message!=CommandStrings.END_TRANSMISSION){
				message = (String) readObject();
				if(message!=null){
					positions.add(message);
				}
			}
			
			return positions;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			return null;
		}
	}

	@Override
	public void putFamiliarWhere(String position) throws RemoteException {
		try {
			writeObject(CommandStrings.PUT_WHERE_FAMILIAR);
			writeObject(position);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public void sendConfigFile(String file) throws RemoteException {
		try{	
			String message = CommandStrings.ASK_FOR_CONFIG;
			writeObject(message);
			writeObject(file);
		} catch(IOException e){
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	public synchronized void activateLeaderCard() throws RemoteException {
		try{
			String message = CommandStrings.ACTIVATE_LEADER_CARD;
			writeObject(message);
		} catch (Exception e){
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	@Override
	public synchronized void activateLeaderCard(String card){
		try{
			String message = CommandStrings.ACTIVATE_WHICH_LEADER_CARD;
			writeObject(message);
			writeObject(card);
			
			//TODO se Federico da Montefeltro, chiedi quale familiare
			//TODO se Lorenzo de� Medici, chiedi quale altro leader
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

	@Override
	public void ping() throws RemoteException {
		try {
			writeObject(CommandStrings.PING);
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
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
			return true;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return false;
	}
	
	private synchronized void writeObject(Object message) throws IOException{
		_outputStream.writeObject(message);
		_outputStream.flush();
	}
	
	private synchronized Object readObject() throws ClassNotFoundException, IOException{
		return _inputStream.readObject();
	}
	
	private Socket _socket;
	private ObjectInput _inputStream;
	private ObjectOutput _outputStream;
	private final Logger _log = Logger.getLogger(SocketConnectionServerHandler.class.getName());
}
