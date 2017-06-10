package game.state;

import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.GameException;
import game.Game;
import game.LeaderCard;

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
	protected State processAction(String whichLeaderCard) throws GameException {
		for(LeaderCard lc : _player.getLeaderCards()){
			if(lc.getName()==whichLeaderCard){
				_player.getDynamicBar().discardLeaderCard(lc);
				return new StateStartingTurn(_theGame);
			}
		}
		throw new GameException(getClass()+"ERROR: Wrong command "+ whichLeaderCard +" in StateDropLeaderCard.processAction(String action)");
	}

	private Logger _log = Logger.getLogger(StateDropLeaderCard.class.getName());
}
