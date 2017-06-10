package game.state;

import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.GameException;
import game.Game;
import util.CommandStrings;

public class StateStartingTurn extends State{

	public StateStartingTurn(Game game) {
		super(game);
	}

	@Override
	public State doState() {
		try{
			_player.getClient().getConnectionHandler().startTurn();
			
			String action = _theGame.getNextGameAction();
			
			return processAction(action);
		}catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			return null;
		}
	}
	
	protected State processAction(String action) throws GameException {
		if(action==CommandStrings.PUT_FAMILIAR){
			if(_theGame.hasPlacedFamiliarYet()){
				//TODO digli che non può farlo
				
				
				return this;
			} else {
				return new StatePlaceFamiliar(_theGame);
			}
		} else if(action==CommandStrings.ACTIVATE_LEADER_CARD){
				if(!_player.getActivableLeaderCards().isEmpty()){
					return new StatePlayLeaderCard(_theGame);
				} else {
					//TODO avvisa il player che non può farlo
					return this;
				}
				
		} else if(action==CommandStrings.DROP_LEADER_CARD){
			if(!_player.getLeaderCards().isEmpty()){
				return new StateDropLeaderCard(_theGame);
			} else {
				//TODO avvisa il player che non può farlo
				return this;
			}
			
		} else if(action==CommandStrings.END_TURN){
			if(_theGame.hasPlacedFamiliarYet()){
				return null;
			} else {
				//TODO avvisa il player che deve piazzare un familiare
				return this;
			}
		} else {
			throw new GameException(getClass()+"ERROR: Wrong command in StateStartingTurn.processAction(String action)");
		}
	}
	
	private Logger _log = Logger.getLogger(StateStartingTurn.class.getName());
}
