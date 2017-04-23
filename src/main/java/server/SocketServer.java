package server;

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
			ServerSocket _serverSocket = new ServerSocket(2334);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		_log.log(Level.INFO, "SocketServer ready");
		
		while(_IS_RUNNING){
			try {
				Socket socket = _serverSocket.accept();
				SocketConnectionClientHandler handler = new SocketConnectionClientHandler(socket);
				
				if(Server.getInstance().onConnect(handler)){
					executor.submit(handler);
				}
			} catch (Exception e) {
				break;
			}	
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