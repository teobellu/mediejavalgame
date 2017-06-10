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
			_whichFamiliar = _theGame.getNextGameAction();
			//TODO controllare che sia effettivamente un familiare valido
			//TODO fai i dovuti controlli
			
			return processAction(_theGame.getNextGameAction());
		} else if(action==CommandStrings.PUT_WHERE_FAMILIAR) {
			//TODO compi l'azione
			//TODO controlli, as usual
			
			return new StateStartingTurn(_theGame);
		} else {
			throw new GameException(getClass()+"ERROR: Wrong command in StatePlaceFamiliar.processAction(String action)");
		}
	}
	
	private String _whichFamiliar;
	
	private Logger _log = Logger.getLogger(StatePlaceFamiliar.class.getName());
}
