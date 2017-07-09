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

	/**
	 * private constructor
	 */
	private Server(){}
	
	/**
	 * Get a server instance
	 * @return the server instance
	 */
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
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
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
	
	/**
	 * Stop the server
	 */
	private synchronized void stopServer(){
		
		_log.info("Shutting down the server...");
		
		_startingGames.clear();

		if(_serverRMI.isRunning()){
			_serverRMI.stopServer();
		}
		
		if(_serverSocket.isRunning()){
			_serverSocket.stopServer();
		}
		
		_instance.interrupt();
		System.exit(0);
	}
	
	/**
	 * Handle room creation
	 * @param handler new client
	 * @param name client name
	 * @return true if added, false otherwise
	 */
	public synchronized boolean addMeToGame(ConnectionHandler handler, String name){
		String id = UUID.randomUUID().toString();
		Client client = new Client(handler, id, name);
		handler.setClient(client);
		try{
			if(!_startingGames.isEmpty()){
				for(Room r : _startingGames){
					if(!r.isRunning() && !r.isFull()){
						for(Client c : r.getPlayers()){
							if(c.getName().equalsIgnoreCase(name)){
								return false;
							}
						}
						r.addPlayer(client);
						return true;
					}
				}
			}
			Room r = new Room(handler.getConfigFile());
			r.addPlayer(client);
			return _startingGames.add(r);
		} catch(GameException e){
			_log.log(Level.SEVERE, e.getMessage(), e);
			return false;
		}
	}
	
	/**
	 * Shutdown the server
	 */
	public void shutdown(){
		_isRunning = false;
	}
	
	/**
	 * Get the list of rooms
	 * @return the list
	 */
	protected List<Room> getRooms(){
		return _startingGames;
	}

	/**
	 * Am i running?
	 */
	private boolean _isRunning = false;
	/**
	 * List of rooms
	 */
	private List<Room> _startingGames;
	/**
	 * The server RMI
	 */
	private ServerRMI _serverRMI;
	/**
	 * The server socket
	 */
	private SocketServer _serverSocket;
	/**
	 * Server instance
	 */
	private static Server _instance = null;
	/**
	 * Executor pool to start new rooms
	 */
	private Executor _gameExecutor = Executors.newCachedThreadPool();
	
	/**
	 * The logger
	 */
	private Logger _log = Logger.getLogger(Server.class.getName());
}
