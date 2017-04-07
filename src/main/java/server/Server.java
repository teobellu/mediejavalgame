package server;

public class Server extends Thread {

	private Server(){}
	
	public static Server getInstance(){
		if(_instance == null){
			synchronized (Server.class) {
				if (_instance == null){
					_instance = new Server();
				}
			}
		}
		
		return _instance;
	}
	
	@Override
	public void run(){
		_serverRMI = new ServerRMI();
		_serverSocket = new ServerSocket();
		
		_serverRMI.start();
		_serverSocket.start();
	}
	
	public void stopServer(){
		if(_serverRMI.isRunning()){
			_serverRMI.stopServer();
		}
		
		if(_serverSocket.isRunning()){
			_serverSocket.stopServer();
		}
		
		_instance.interrupt();
	}
	
	private ServerRMI _serverRMI;
	private ServerSocket _serverSocket;
	private static Server _instance = null;
}
