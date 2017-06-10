package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketServer extends Thread {

	public SocketServer(){
		//TODO
	}
	
	@Override
	public void run(){
		ExecutorService executor = Executors.newCachedThreadPool();
		try {
			ServerSocket _serverSocket = new ServerSocket(2334);//TODO
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		_IS_RUNNING = true;
		_log.log(Level.INFO, "SocketServer ready");
		
		while(_IS_RUNNING){
			try {
				Socket socket = _serverSocket.accept();
				SocketConnectionHandler handler = new SocketConnectionHandler(socket);
				
				executor.submit(handler);
			} catch (Exception e) {
				break;
			}
		}
		
		try {
			_serverSocket.close();
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		executor.shutdown();
	}
	
	public void stopServer() {
		_IS_RUNNING = false;		
	}

	public boolean isRunning() {
		return _IS_RUNNING;
	}

	private boolean _IS_RUNNING = false;
	private ServerSocket _serverSocket;
	private Logger _log = Logger.getLogger(SocketServer.class.getName());
}