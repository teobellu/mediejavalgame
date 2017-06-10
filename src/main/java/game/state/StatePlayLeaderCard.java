package game.state;

import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.GameException;
import game.Game;
import game.LeaderCard;
import util.CommandStrings;

public class StatePlayLeaderCard extends State {

	public StatePlayLeaderCard(Game game) {
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
		if(action==CommandStrings.ACTIVATE_WHICH_LEADER_CARD){
			String whichCard = _theGame.getNextGameAction();
			for(LeaderCard lc : _player.getActivableLeaderCards()){
				if(lc.getName()==whichCard){
					try {
						_theGame.getDynamicBar().activateLeaderCard(lc);
						return new StateStartingTurn(_theGame);
					} catch (game.GameException e) {
						_log.log(Level.SEVERE, e.getMessage(), e);
						return null;
					}
				}
			}
			throw new GameException(getClass()+"ERROR: Wrong command "+ whichCard +" in StateDropLeaderCard.processAction(String action)");
		}
		
		throw new GameException(getClass()+"ERROR: Wrong command "+ action +" in StateDropLeaderCard.processAction(String action)");
	}
	
	private Logger _log = Logger.getLogger(StatePlayLeaderCard.class.getName());

}
