package game.state;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.GameException;
import game.Game;
import game.LeaderCard;
import util.CommandStrings;

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
	public List<String> dropLeaderCard() throws GameException {
		throw new GameException("Called method dropLeaderCard in StateDropLeaderCard");
	}

	@Override
	public void dropWhichLeaderCard(String leader) throws GameException {
		for(LeaderCard lc : _player.getLeaderCards()){
			if(lc.getName().equals(leader)){
				_theGame.getDynamicBar().discardLeaderCard(lc);
				
				//TODO
				
				_theGame.setState(new StateStartingTurn(_theGame));
			}
		}
	}
	
	@Override
	public boolean endTurn() throws GameException {
		throw new GameException("Called method endTurn in StateDropLeaderCard");
	}

	@Override
	public List<String> activateLeaderCard() throws GameException {
		throw new GameException("Called method activateLeaderCard in StateDropLeaderCard");
	}

	@Override
	public void activateWhichLeaderCard(String leader) throws GameException {
		throw new GameException("Called method activateWhichLeaderCard in StateDropLeaderCard");
	}

	@Override
	public List<String> placeFamiliar() throws GameException {
		throw new GameException("Called method placeFamiliar in StateDropLeaderCard");
	}

	@Override
	public List<String> placeWhichFamiliar(String familiar) throws GameException {
		throw new GameException("Called method placeWhichFamiliar in StateDropLeaderCard");
	}

	@Override
	public void placeWhereFamiliar(String position) throws GameException {
		throw new GameException("Called method placeWhereFamiliar in StateDropLeaderCard");
	}
	
	private Logger _log = Logger.getLogger(StateDropLeaderCard.class.getName());
}
