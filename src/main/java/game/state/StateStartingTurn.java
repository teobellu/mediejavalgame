package game.state;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.GameException;
import game.FamilyMember;
import game.Game;
import game.LeaderCard;
import util.CommandStrings;

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
	public void dropWhichLeaderCard(String leader) throws GameException {
		throw new GameException("Called method dropWhichLeaderCard in StateStartingTurn");
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
	public void activateWhichLeaderCard(String leader) throws GameException {
		throw new GameException("Called method activateWhichLeaderCard in StateStartingTurn");
	}
	
	@Override
	public List<FamilyMember> placeFamiliar() throws GameException {
		return _player.getFreeMember();
	}

	@Override
	public List<String> placeWhichFamiliar(String familiar) throws GameException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void placeWhereFamiliar(String position) throws GameException {
		// TODO Auto-generated method stub
		
	}
	
	private Logger _log = Logger.getLogger(StateStartingTurn.class.getName());

}
