package server;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import util.Packet;

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
			
			Packet message = null;
			
			while(_isRunning){
				message = getFromClient();
				if(message!=null){
					_client.processMessage(message);
				}
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			shutdown();
		}
	}
	
	public void sendToClient(Packet message){
		try{
			if(_isRunning){
				_outputStream.writeObject(message);
				_outputStream.flush();
			}
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	public Packet getFromClient() throws ClassNotFoundException, IOException {
		return (Packet)_inputStream.readObject();
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
	
	private Socket _socket;
	private ObjectInput _inputStream;
	private ObjectOutput _outputStream;
	private Logger _log = Logger.getLogger(SocketConnectionHandler.class.getName());
}
