package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.GameException;
import game.GameBoard;
import util.CommandStrings;

public class SocketConnectionHandler extends ConnectionHandler implements Runnable {

	public SocketConnectionHandler(Socket socket) {
		try {
			_socket = socket;

			_outputStream = new ObjectOutputStream(_socket.getOutputStream());
			_inputStream = new ObjectInputStream(_socket.getInputStream());
			
			_isRunning = true;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
	}
	
	@Override
	public void run() {
		try {
			while(_isRunning){
				Object obj = getFromClient();
				if(obj!=null){
					processObject(obj);
				}
				Thread.sleep(300);
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			shutdown();
		}
	}

	private Object getFromClient(){
		synchronized (_inputStream) {
			try {
				if(_isRunning){
					Object object = _inputStream.readObject();
					return object;
				} else {
					return null;//TODO null o stringa vuota?
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
				return null;//TODO null o stringa vuota?
			}
		}
	}
	
	private void processObject(Object obj){
		if(obj instanceof String){
			processString((String) obj);
		} else {
			//TODO manderemo mai cose che non sono stringhe?
			//TODO se sì, le manderemo in entrambe le direzioni?
		}
	}
	
	private void processString(String str){
		if(str.equals(CommandStrings.ADD_TO_GAME)){
			if(Server.getInstance().addMeToGame(this)){
				sendToClient(CommandStrings.ADD_TO_GAME);
			} else {
				//TODO in teoria posso mandare quello che voglio, ma da rivedere per sicurezza
				sendToClient(CommandStrings.END_TURN);
			}
		} else if(str.equals(CommandStrings.DROP_LEADER_CARD)){
			List<String> leaders = new ArrayList<>();
			try {
				leaders = _theGame.getState().dropLeaderCard();
			} catch (GameException e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
			}
			sendToClient(leaders);
		} else if(str.equals("send me board")){
			GameBoard gameboard = new GameBoard();
			int[] a = new int[3];
			a[0] = 1;
			a[1] = 5;
			a[2] = 4;
			System.out.println(gameboard.getdices()[0]);
			gameboard.setDices(a);
			try {
				writeObject(gameboard);
			} catch (IOException e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
	
	@Override
	public void shutdown() {
		super.shutdown();
		try {
			if(_inputStream != null){
				_inputStream.close();
			}
			
			if(_outputStream != null){
				_outputStream.close();
			}
			
			if(!_socket.isClosed()){
				_socket.close();
			}
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	@Override
	public void onConnect() throws RemoteException {
		//TODO
	}
	
	private void writeObject(Object obj) throws IOException{
		_outputStream.writeObject(obj);
		_outputStream.flush();
		System.out.println("mandata risposta");
	}
	
	public String startTurn(){
		//TODO
		return null;
	}
	
	@Override
	public void sendToClient(List<String> messages) {
		try {
			for(String s : messages){
				writeObject(s);
			}
			writeObject(CommandStrings.END_TRANSMISSION);
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	@Override
	public void sendToClient(String message) {
		try {
			writeObject(message);
			//TODO mando anche qui end transmission?
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	public void sendBoard(GameBoard board){
		
	}
	
	private Socket _socket;
	private ObjectInputStream _inputStream;
	private ObjectOutputStream _outputStream;
	private Logger _log = Logger.getLogger(SocketConnectionHandler.class.getName());
}
