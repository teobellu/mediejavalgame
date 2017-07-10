package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import util.Constants;

/**
 * Socket Server that handles the creation of new SocketconnectionHandler
 * @author Jacopo
 * @author Matteo
 */
public class SocketServer extends Thread {

	/**
	 * Constructor
	 */
	public SocketServer(){
	}
	
	@Override
	public void run(){
		ExecutorService executor = Executors.newCachedThreadPool();
		try {
			_serverSocket = new ServerSocket(Constants.DEFAULT_SOCKET_PORT);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			Server.getInstance().shutdown();
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
	
	/**
	 * Stop the server
	 */
	public void stopServer() {
		_isRunning = false;		
	}

	/**
	 * Am i running?
	 * @return yes or no
	 */
	public boolean isRunning() {
		return _isRunning;
	}

	/**
	 * Am i running?
	 */
	private boolean _isRunning = false;
	/**
	 * The ServerSocket associated
	 */
	private ServerSocket _serverSocket;
	/**
	 * the logger
	 */
	private Logger _log = Logger.getLogger(SocketServer.class.getName());
}