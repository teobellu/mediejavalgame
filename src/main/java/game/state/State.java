package game.state;

import java.util.ArrayList;
import java.util.List;

import exceptions.CommandException;
import game.*;
import server.Client;
import util.GamePacket;
import util.Packet;

public abstract class State {
	
	public static final String COMMAND_NOT_FOUND = "Il comando scelto non esiste";
	public static final String NOT_A_NUMBER = "Devi scrivere un numero!";
	public static final String NOT_VALID_NUMBER = "Devi scrivere un numero valido!";
	
	public static final String PLAY_LEADER_CARD = "PLAY";
	public static final String SELL_LEADER_CARD = "SELL";
	
	public static final String PLACE_FAMILIAR_TOWER = "TOWER";
	public static final String PLACE_FAMILIAR_MARKET = "MARKET";
	public static final String PLACE_FAMILIAR_HARVEST = "HARVEST";
	public static final String PLACE_FAMILIAR_PRODUCTION = "PRODUCTION";
	public static final String PLACE_FAMILIAR_COUNCIL = "COUNCIL";
	
	public static final String TERRITORY = "TERRITORY";
	public static final String CHARACTER = "CHARACTER";
	public static final String BUILDING = "BUILDING";
	public static final String VENTURE = "VENTURE";
	
	public static final String EXIT = "EXIT";
	public static final String START = "START";
	
	public static final String GAIN = "GAIN";
	public static final String PAY = "PAY";
	
	
	protected final Game theGame;
	protected Player player;
	protected Client client;
	
	public State(Game game){
		theGame = game;
		try{
		player = theGame.getCurrentPlayer();
		client = theGame.getCurrentClient();
		}
		catch(NullPointerException e){}
	}
	
	public State factoryMethod(String string) throws CommandException{
		switch(string){
			case START : return this;
			case EXIT : return null;
			default : throw new CommandException();
		}
	}
	
	public abstract State doState();
	
	public String getCorrectResponse(String string, List<String> listOfCommand){
		do{
			String answer = getResponse(string);
			if (listOfCommand.contains(answer))
				return answer;
			else
				sendMessage(COMMAND_NOT_FOUND);
		}while(true);
	}
	
	public Integer getCorrectResponse(String string, Integer minimum, Integer maximum){
		do{
			try{
				String answer = getResponse(string);
				Integer chosenNumber = Integer.parseInt(answer);
				if (chosenNumber >= minimum && chosenNumber <= maximum)
					return chosenNumber;
				else
					sendMessage(NOT_VALID_NUMBER);
			}
			catch(NumberFormatException e){
				sendMessage(NOT_A_NUMBER);	
			}
		}while(true);
	}
	
	// Metodo in cui non controllo quello che ricevo, esempio scelta del nome e messaggio di chat
	public String getResponse(String string){
		sendMessage(string);
		return receiveMessage();
	}
	
	public void sendMessage(String string){
		Packet message = new GamePacket(string);
		client.getConnectionHandler().sendToClient(message);
	}
	
	public String receiveMessage(){
		Packet answer;
		try{
			answer = client.getConnectionHandler().getFromClient();
			return answer.getCommand();
		} catch (Exception e) {
			// TODO entro qui per errore connessione
			e.printStackTrace();
			return null;
		}
		
	}
}