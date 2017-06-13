package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketConnectionHandler extends ConnectionHandler implements Runnable {

	public SocketConnectionHandler(Socket socket) {
		try {
			_socket = socket;

			_outputStream = new ObjectOutputStream(_socket.getOutputStream());
			_inputStream = new ObjectInputStream(_socket.getInputStream());
			
			_isRunning = true;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
	}
	
	@Override
	public void run() {
		try {
			while(_isRunning){
				Object obj = getFromClient();
				if(obj!=null){
					processObject(obj);
				}
				Thread.sleep(300);
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
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
	
	private void processObject(Object obj){
		if(obj instanceof String){
			processString((String) obj);
		} else {
			//TODO manderemo mai cose che non sono stringhe?
			//TODO se sì, le manderemo in entrambe le direzioni?
		}
	}
	
	private void processString(String str){
		
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
		System.out.println("mandata risposta");
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
	private ObjectInputStream _inputStream;
	private ObjectOutputStream _outputStream;
	private Logger _log = Logger.getLogger(SocketConnectionHandler.class.getName());
	
}
