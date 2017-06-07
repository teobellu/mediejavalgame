package game.state;

import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.GameException;
import game.Game;
import util.CommandStrings;

public class StateStartingTurn extends State{

	public StateStartingTurn(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public State doState() {
		try{
			_theGame.getCurrentClient().getConnectionHandler().startTurn();
			
			String action = _theGame.getNextGameAction();
			
			return processAction(action);
		}catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			return null;
		}
	}
	
	protected State processAction(String action) throws GameException {
		if(action==CommandStrings.PUT_FAMILIAR){
			return new StatePlaceFamiliar(theGame);
		} else if(action==CommandStrings.ACTIVATE_LEADER_CARD){
			return new StatePlayLeaderCard(theGame);
		} else if(action==CommandStrings.DROP_LEADER_CARD){
			return new StateDropLeaderCard(theGame);
		} else if(action==CommandStrings.END_TURN){
			//TODO check se il player può passare il turno
		} else {
			throw new GameException(getClass()+"ERROR: Wrong command in initial state");
		}
	}
	
	private Logger _log = Logger.getLogger(StateStartingTurn.class.getName());
}
