package game.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import exceptions.CommandException;
import game.*;
import server.Client;
import util.GamePacket;
import util.Packet;

public class StatePlaceFamiliarTower extends StatePlaceFamiliar {
	
	private static final String FIRST_FLOOR = "FIRST";
	private static final String SECOND_FLOOR = "SECOND";
	private static final String THIRD_FLOOR = "THIRD";
	private static final String FOURTH_FLOOR = "FOURTH";
	
	private static final String SMS_SELECT_COLOUMN = "Select the type of card: (1) Territory - (2) Character - (3) Building - (4) Venture";
	private static final String SMS_SELECT_ROW = "Select floor: (1) Lower floor - (2) Second floor - (3) Third floor - (4) Top floor";
	
	private static final List<String> SELECT_A_COLOUMN = Collections.unmodifiableList(
			Arrays.asList(TERRITORY, CHARACTER, BUILDING, VENTURE));
	
	private static final List<String> SELECT_A_ROW = Collections.unmodifiableList(
			Arrays.asList(FIRST_FLOOR, SECOND_FLOOR, THIRD_FLOOR, FOURTH_FLOOR));
	
	private static final List<String> COMMAND = Collections.unmodifiableList(
			Arrays.asList(FIRST_FLOOR, SECOND_FLOOR, THIRD_FLOOR, FOURTH_FLOOR));
	
	public StatePlaceFamiliarTower(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	
	// problema: entro in questo stato, ma non posso comprare nessuna carta
	@Override
	public State doState() {
		GameBoard board = theGame.getGameBoard();
		int coloumn = getCorrectResponse(SMS_SELECT_COLOUMN, 1, board.MAX_COLOUMN) - 1;
		int row = getCorrectResponse(SMS_SELECT_ROW, 1, board.MAX_ROW) - 1;
		DevelopmentCard card = board.getCard(row,coloumn);
		try {
			player.addDevelopmentCard(card);
		} catch (GameException e) {
			// TODO il player non può comprare la carta
			e.printStackTrace();
		}
		String answer = getResponse("PROSSIMA-MOSSA-Cosa vuoi fare?");
		try {
			return factoryMethod(answer);
		} catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/*
	private int returnColoumn(String coloumn) {
		switch(coloumn){
			case TERRITORY : return 0;
			case CHARACTER : return 1;
			case BUILDING : return 2;
			default : return 3;
		}
	}*/
	
	@Override
	public State factoryMethod(String string) throws CommandException{
		switch(string){
			case SELL_LEADER_CARD : return new StateSellLeaderCard(theGame);
			case PLAY_LEADER_CARD : return new StatePlayLeaderCard(theGame);
			case EXIT : return null;
			default : throw new CommandException();
		}	
	}
}