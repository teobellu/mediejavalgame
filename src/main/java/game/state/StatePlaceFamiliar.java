package game.state;

import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.GameException;
import game.Game;
import util.CommandStrings;

public class StatePlaceFamiliar extends State {

	public StatePlaceFamiliar(Game game) {
		super(game);
	}

	@Override
	public State doState() {
		try {
			String action = _theGame.getNextGameAction();
			
			return processAction(action);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			return null;
		}
	}

	@Override
	protected State processAction(String action) throws GameException {
		if(action==CommandStrings.PUT_WHICH_FAMILIAR){
			//TODO salva l'informazione da qualche parte
			//TODO fai i dovuti controlli
			
			return processAction(_theGame.getNextGameAction());
		} else if(action==CommandStrings.PUT_WHERE_FAMILIAR) {
			//TODO compi l'azione
			//TODO controlli, as usual
			
			return new StateStartingTurn(_theGame);
		} else {
			return null;
		}
	}
	
	private Logger _log = Logger.getLogger(StatePlaceFamiliar.class.getName());
}
