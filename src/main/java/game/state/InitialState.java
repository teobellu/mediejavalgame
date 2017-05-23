package game.state;

import exceptions.CommandException;
import game.*;
import server.Client;
import util.GamePacket;
import util.Packet;

public class InitialState extends State {
	
	public InitialState(Game game) {
		super(game);
	}

	@Override
	public State doState() {
		sendMessage("COMANDO-SCEGLI-AZIONE");
		String answer = receiveMessage();
		try {
			return factoryMethod(answer);
		} catch (CommandException e) {
			//return new InitialState(theGame);
			return this;
		}
		//return null;
	}
	
	@Override
	public State factoryMethod(String string) throws CommandException{
		switch(string){
			case PLACE_FAMILIAR_TOWER : return new StatePlaceFamiliarTower(theGame);
			case PLACE_FAMILIAR_MARKET : return new StatePlaceFamiliarMarket(theGame);
			case PLACE_FAMILIAR_HARVEST : return new StatePlaceFamiliarHarvest(theGame);
			case PLACE_FAMILIAR_PRODUCTION : return new StatePlaceFamiliarProduction(theGame);
			case PLACE_FAMILIAR_COUNCIL : return new StatePlaceFamiliarCouncil(theGame);
			case SELL_LEADER_CARD : return new StateSellLeaderCard(theGame);
			case PLAY_LEADER_CARD : return new StatePlayLeaderCard(theGame);
			case EXIT : return null;
			default : throw new CommandException();
		}
		
	}

}
