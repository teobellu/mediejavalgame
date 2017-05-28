package game.state;

import game.Game;
import game.GameException;
import server.Client;
import util.GamePacket;
import util.Packet;

public class StatePlaceFamiliar extends State{

	protected boolean alreadyPlaced;
	
	public StatePlaceFamiliar(Game game) {
		super(game);
		alreadyPlaced = false;
	}

	@Override
	public State doState() {
		
		//Packet message = new GamePacket("DOVE");
		
		//theGame.getCurrentPlayer().getConnectionHandler().sendToClient(message);	
		return null;
	}

}
