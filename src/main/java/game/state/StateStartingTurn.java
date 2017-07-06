package game.state;

import java.util.ArrayList;
import java.util.List;

import exceptions.GameException;
import game.FamilyMember;
import game.Game;
import game.LeaderCard;

public class StateStartingTurn extends State{

	public StateStartingTurn(Game game) {
		super(game);
	}
	
	@Override
	public List<String> dropLeaderCard() throws GameException {
		List<String> leaders = new ArrayList<>();
		if(!_player.getLeaderCards().isEmpty()){
			for(LeaderCard lc : _player.getLeaderCards()){
				leaders.add(lc.getName());
			}
		}
		_theGame.setState(new StateDropLeaderCard(_theGame));
		
		return leaders;
	}
	
	@Override
	public boolean endTurn() throws GameException {
		if(_theGame.hasPlacedFamiliarYet()){
			//TODO tell the game the turn is over
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public List<String> activateLeaderCard() throws GameException {
		List<String> leaders = new ArrayList<>();
		if(!_player.getActivableLeaderCards().isEmpty()){
			for(LeaderCard lc : _player.getActivableLeaderCards()){
				leaders.add(lc.getName());
			}
		}
		
		_theGame.setState(new StateActivateLeaderCard(_theGame));
		
		return leaders;
	}
	
	@Override
	public List<FamilyMember> placeFamiliar() throws GameException {
		if (_player.getFreeMembers().isEmpty())
			throw new GameException("You don't have free familiars!");
		_theGame.setState(new StatePlaceFamiliar(_theGame));
		return _player.getFreeMembers();
	}
}
