package server;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import util.CommandStrings;

public class SocketConnectionHandler extends ConnectionHandler implements Runnable {

	public SocketConnectionHandler(Socket socket) {
		_socket = socket;
		_isRunning = false;
	}
	
	@Override
	public void run() {
		try {
			_inputStream = new ObjectInputStream(_socket.getInputStream());
			_outputStream = new ObjectOutputStream(_socket.getOutputStream());
			
			_isRunning = true;
			
			String addMeToGame = null;
			do {
				addMeToGame = (String) _inputStream.readObject();
			} while (addMeToGame!=CommandStrings.ADD_TO_GAME);
			
			if(Server.getInstance().addMeToGame(this)){
				writeObject(CommandStrings.ASK_FOR_CONFIG);
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			shutdown();
		}
	}

	private Object getFromClient(){
		synchronized (_inputStream) {
			try {
				if(_isRunning){
					return _inputStream.readObject();
				} else {
					return null;//TODO null o stringa vuota?
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
				return null;//TODO null o stringa vuota?
			}
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
		//TODO
	}
	
	private void writeObject(Object obj) throws IOException{
		_outputStream.writeObject(obj);
		_outputStream.flush();
	}
	
	public String startTurn(){
		//TODO
		return null;
	}
	
	@Override
	public void sendToClient(String[] messages) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void sendToClient(String message) {
		// TODO Auto-generated method stub
		
	}
	
	private Socket _socket;
	private ObjectInput _inputStream;
	private ObjectOutput _outputStream;
	private Logger _log = Logger.getLogger(SocketConnectionHandler.class.getName());
	
}
