package game.state;

import game.Game;
import packets.GamePacket;
import packets.Packet;
import server.Client;

public class InitialState extends State {

	public InitialState(Game game) {
		super(game);
	}

	@Override
	public State doState(Client player) {
		Packet message = new GamePacket("COMANDO-SCEGLI-AZIONE");
		_theGame.getCurrentPlayer().getConnectionHandler().sendToClient(message);
		
		try {
			message = _theGame.getCurrentPlayer().getConnectionHandler().getFromClient();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/* In base a quello che ricevo, genero lo stato corrispondente
		 * O lo specifico a livello di stato, o con una StateFactory
		 * */
		return null;
	}
	


}
