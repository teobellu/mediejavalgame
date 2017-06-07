package game.state;

import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.GameException;
import game.Game;

public class StateDropLeaderCard extends State {

	public StateDropLeaderCard(Game game) {
		super(game);
	}

	@Override
	public State doState() {
		try {
			String action = _theGame.getNextGameAction();
			
			return processAction(action);
		} catch (GameException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			return null;
		}
	}

	@Override
	protected State processAction(String action) throws GameException {
		// TODO Auto-generated method stub
		return null;
	}

	private Logger _log = Logger.getLogger(StateDropLeaderCard.class.getName());
}
