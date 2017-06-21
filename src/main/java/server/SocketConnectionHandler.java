package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.GameException;
import game.GameBoard;
import game.Resource;
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
					return null;
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
				return null;
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
		System.out.println("Letto: " + str);
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
		List<String> messages = new ArrayList<>();
		messages.add(message);
		sendToClient(messages);
	}
	
	public void sendBoard(GameBoard board){
		
	}
	
	@Override
	public int spendCouncil(List<Resource> councilRewards) throws RemoteException {
		int selection = 0;
		try {
			writeObject(CommandStrings.HANDLE_COUNCIL);
			writeObject(councilRewards);
			_socket.setSoTimeout(9000);
			selection = (int) _inputStream.readObject();
		} catch (SocketTimeoutException e){
			// è scaduto il tuo tempo
		} catch (IOException | ClassNotFoundException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			//return spendCouncil(councilRewards);
		}
		return selection;
	}
	
	@Override
	public void sendInitialLeaderList(List<String> leadersList) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	private Socket _socket;
	private ObjectInputStream _inputStream;
	private ObjectOutputStream _outputStream;
	private Logger _log = Logger.getLogger(SocketConnectionHandler.class.getName());
	
}
