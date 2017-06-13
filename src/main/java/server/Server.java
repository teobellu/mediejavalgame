package server;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import exceptions.GameException;

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
		_isRunning = true;
		
		while(_isRunning){
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
	
	private void stopServer(){
		
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
	
	public synchronized boolean addMeToGame(ConnectionHandler handler){
		String id = UUID.randomUUID().toString();
		Client client = new Client(handler, id);
		handler.setClient(client);
		boolean isFirst = false;
		try{
			if(!_games.isEmpty()){
				for(Room r : _games){
					if(!r.isFull()){
						r.addPlayer(client);
						return isFirst;
					}
				}
			} 
			Room r = new Room(handler.getConfigFile());
			r.addPlayer(client);
			isFirst = _games.add(r);
			return isFirst;
		} catch(GameException e){
			return false;
		}
	}
	
	public void shutdown(){
		_isRunning = false;
	}

	private boolean _isRunning = false;
	private List<Room> _games;
	private ServerRMI _serverRMI;
	private SocketServer _serverSocket;
	private static Server _instance = null;
}
