package client.network;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.constants.CommandKeys;
import util.GamePacket;
import util.packets.NamePacket;
import util.packets.Packet;
import util.packets.PingPacket;

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
			
			Packet message = null;
			
			while(_isRunning){
				message = readFromServer();
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
	
	public synchronized void sendToServer(Packet packet) {
		try {
			_outputStream.writeObject(packet);
			_outputStream.flush();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	public synchronized Packet readFromServer() throws ClassNotFoundException, IOException {
		return (Packet) _inputStream.readObject();
	}
	
	@Override
	public void sendName(String name) throws RemoteException {
		Packet message = new NamePacket(CommandKeys.ASK_NAME, name);
		sendToServer(message);
	}

	@Override
	public void putFamiliar() throws RemoteException {
		// TODO Auto-generated method stub
		sendToServer(message);
	}

	@Override
	public void sendConfigFile() throws RemoteException {
		// TODO Auto-generated method stub
		sendToServer(message);
	}

	@Override
	public void activateLeaderCard() throws RemoteException {
		// TODO Auto-generated method stub
		sendToServer(message);
	}

	@Override
	public void ping() throws RemoteException {
		Packet message = new PingPacket(CommandKeys.PING);
		sendToServer(message);
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
	private final Logger _log = Logger.getLogger(SocketConnectionServerHandler.class.getName());
}
