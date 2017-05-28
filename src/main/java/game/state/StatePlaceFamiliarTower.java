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
	
	private static final String ERR_NO_CARD_FOUND = "Non è presente nessuna carta";
	
	private static final List<String> SELECT_A_COLOUMN = Arrays.asList(TERRITORY, CHARACTER, BUILDING, VENTURE);
	
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
		try {
			DevelopmentCard card = board.getCard(row,coloumn);
		} catch (GameException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			canPutFamiliar(player, coloumn, row);
		} catch (GameException e) {
			sendMessage("Non puoi comprare questa carta");
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
	
	public void canPutFamiliar(Player player, int coloumn, int row) throws GameException{
		GameBoard board = theGame.getGameBoard();
		DevelopmentCard card = board.getCard(row,coloumn);
		if (card == null){
			sendMessage(ERR_NO_CARD_FOUND);
			throw new GameException();
		}
		
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