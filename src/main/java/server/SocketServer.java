package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import util.Constants;

public class SocketServer extends Thread {

	public SocketServer(){
	}
	
	@Override
	public void run(){
		ExecutorService executor = Executors.newCachedThreadPool();
		try {
			_serverSocket = new ServerSocket(Constants.DEFAULT_SOCKET_PORT);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		_isRunning = true;
		
		_log.log(Level.INFO, "SocketServer ready");
		
		do {
			try {
				Socket socket = _serverSocket.accept();
				SocketConnectionHandler handler = new SocketConnectionHandler(socket);
				
				executor.submit(handler);
				
				_log.info("New SocketConnectionHandler created");
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
				_isRunning = false;
			}
		} while(_isRunning);
		
		_log.info("Shutting down socket server...");
		
		executor.shutdown();
		try {
			_serverSocket.close();
		} catch (IOException io) {
			_log.log(Level.SEVERE, io.getMessage(), io);
		}
	}
	
	public void stopServer() {
		_isRunning = false;		
	}

	public boolean isRunning() {
		return _isRunning;
	}

	private boolean _isRunning = false;
	private ServerSocket _serverSocket;
	private transient Logger _log = Logger.getLogger(SocketServer.class.getName());
}