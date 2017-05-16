package server;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
		
		//avvio dei server
		_serverRMI = new ServerRMI();
		_serverSocket = new SocketServer();
		
		_serverRMI.start();
		_serverSocket.start();
		
		//creazione delle room e loro gestione
		_games = new ArrayList<>();
		
		//TODO condizione di stop
		boolean condition = true;
		
		while(condition){
			for(Room r : _games){
				if(r.isReady() && !r.isRunning()){
					r.start();
				}
				
				if(r.isOver()){
					_games.remove(r);
				}
			}
		}
		
		stopServer();
	}
	
	public void stopServer(){
		for(Room r : _games){
			_games.remove(r);
		}

		if(_serverRMI.isRunning()){
			_serverRMI.stopServer();
		}
		
		if(_serverSocket.isRunning()){
			_serverSocket.stopServer();
		}
		
		_instance.interrupt();
		return;
	}
	
	public synchronized boolean onConnect(ConnectionHandler handler){
		String id = UUID.randomUUID().toString();
		Client client = new Client(handler, id);
		handler.setClient(client);
		if(_games.isEmpty()){
			Room r = new Room();
			r.addPlayer(client);
			_games.add(r);
		} else {
			for(Room r : _games){
				if(!r.isFull()){
					r.addPlayer(client);
					return true;
				}
			}
			Room r = new Room();
			r.addPlayer(client);
			_games.add(r);
		}
		
		return true;
	}
	
	private List<Room> _games;
	private ServerRMI _serverRMI;
	private SocketServer _serverSocket;
	private static Server _instance = null;
}
