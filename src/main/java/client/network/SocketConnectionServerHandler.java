package client.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketConnectionServerHandler extends ConnectionServerHandler {

	public SocketConnectionServerHandler(String host, int port) {
		super(host, port);
	}
	
	@Override
	public void run() {
		try {
			_socket = new Socket(_host, _port);
			
			_objectInputStream = new ObjectInputStream(_socket.getInputStream());
			_objetcOutputStream = new ObjectOutputStream(_socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private Socket _socket;
	private ObjectInputStream _objectInputStream;
	private ObjectOutputStream _objetcOutputStream;
}
