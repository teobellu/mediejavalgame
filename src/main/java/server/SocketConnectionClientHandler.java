package server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketConnectionClientHandler implements Runnable {

	public SocketConnectionClientHandler(Socket socket) {
		_socket = socket;
	}
	
	@Override
	public void run() {
		try {
			_inputStream = new ObjectInputStream(_socket.getInputStream());
			_outputStream = new ObjectOutputStream(_socket.getOutputStream());
			
			//TODO lettura comandi
			
			_inputStream.close();
			_outputStream.close();
			_socket.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private Socket _socket;
	private InputStream _inputStream;
	private OutputStream _outputStream;
}
