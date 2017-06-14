package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

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
		
		Thread tr = new Thread(new Runnable() {
			
			@Override
			public void run() {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				try {
					String str;
					do {
						System.out.println("Write exit to stop the server");
						str = reader.readLine();
						if(str.equals("exit")){
							return;
						}
					} while (true);
					
					
				} catch (Exception e) {
					try {
						reader.close();
					} catch (IOException e1) {
						System.out.println("Error on reader in Server.java");
					}
				}
			}
		});
		tr.start();
		
		do {
			if(!tr.isAlive()){
				_isRunning = false;
			}
			
			for(Room r : _games){
				if(r.isTimeoutOver()){//se è scaduto il timeout
					if(r.isReady()){
						if(r.isRunning()){
							if(r.isOver()){
								r.shutdown();
								_games.remove(r);
								_log.info("Room removed 'cause the game was over");
							}
						} else {
							_gameExecutor.execute(r);
						}
					} else {
						_games.remove(r);
						_log.info("Room removed 'cause timeout was over");
						
					}
				}
			}
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
				Thread.currentThread().interrupt();
			}
		} while(_isRunning);
		stopServer();
	}
	
	private void stopServer(){
		
		_log.info("Shutting down the server...");
		
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
		System.exit(0);
	}
	
	public synchronized boolean addMeToGame(ConnectionHandler handler){
		String id = UUID.randomUUID().toString();
		Client client = new Client(handler, id);
		handler.setClient(client);
		try{
			if(!_games.isEmpty()){
				for(Room r : _games){
					if(!r.isFull()){
						r.addPlayer(client);
						return false;
					}
				}
			} 
			Room r = new Room(handler.getConfigFile());
			r.addPlayer(client);
			return _games.add(r);
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
	private Executor _gameExecutor = Executors.newCachedThreadPool();
	
	private Logger _log = Logger.getLogger(Server.class.getName());
}
