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
		_startingGames = new ArrayList<>();
		
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
					_log.log(Level.SEVERE, e.getMessage(), e);
					try {
						reader.close();
					} catch (IOException e1) {
						_log.log(Level.SEVERE, e1.getMessage(), e1);
					}
				}
			}
		});
		tr.start();
		
		do {
			if(!tr.isAlive()){
				_isRunning = false;
			}
			
			for(Room r : _startingGames){
				if(r.isTimeoutOver()){//se è scaduto il timeout
					if(r.isReady()){
						if(r.isRunning()){
							if(r.isOver()){
								r.shutdown();
								_startingGames.remove(r);
								_log.info("Room removed 'cause the game was over");
							}
						} else {
							
							_gameExecutor.execute(r);
						}
					} else {
						r.shutdown();
						_startingGames.remove(r);
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
		
		for(Room r : _startingGames){
			_startingGames.remove(r);
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
	
	public synchronized boolean addMeToGame(ConnectionHandler handler, String name){//TODO controllarlo
		System.out.println("Client asked to join a game");
		String id = UUID.randomUUID().toString();
		Client client = new Client(handler, id, name);
		handler.setClient(client);
		try{
			if(!_startingGames.isEmpty()){
				System.out.println("Ci sono gia' dei game");
				for(Room r : _startingGames){
					if(!r.isFull()){
						for(Client c : r.getPlayers()){
							if(c.getName().equalsIgnoreCase(name)){
								System.out.println("Nome uguale");
								return false;
							}
						}
						r.addPlayer(client);
						System.out.println("Aggiunto ad un game");
						return true;
					}
				}
			}
			System.out.println("Primo game creato");
			Room r = new Room(handler.getConfigFile());
			r.addPlayer(client);
			System.out.println("added to game");
			return _startingGames.add(r);
		} catch(GameException e){
			_log.log(Level.SEVERE, e.getMessage(), e);
			return false;
		}
	}
	
	public void shutdown(){
		_isRunning = false;
	}
	
	protected List<Room> getRooms(){
		return _startingGames;
	}

	private boolean _isRunning = false;
	private List<Room> _startingGames;
	//TODO spostare i game startati in un'altra lista
	private ServerRMI _serverRMI;
	private SocketServer _serverSocket;
	private static Server _instance = null;
	private Executor _gameExecutor = Executors.newCachedThreadPool();
	
	private Logger _log = Logger.getLogger(Server.class.getName());
}
