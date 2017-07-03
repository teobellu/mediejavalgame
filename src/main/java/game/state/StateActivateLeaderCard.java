package game.state;

import java.util.List;

import exceptions.GameException;
import game.FamilyMember;
import game.Game;
import game.LeaderCard;

public class StateActivateLeaderCard extends State {

	public StateActivateLeaderCard(Game game) {
		super(game);
	}

	@Override
	public List<String> dropLeaderCard() throws GameException {
		throw new GameException("Called method dropLeaderCard in StateActivateLeaderCard");
	}

	@Override
	public void dropWhichLeaderCard(String leader) throws GameException {
		throw new GameException("Called method dropWhichLeaderCard in StateActivateLeaderCard");
	}

	@Override
	public boolean endTurn() throws GameException {
		throw new GameException("Called method endTurn in StateActivateLeaderCard");
	}

	@Override
	public List<String> activateLeaderCard() throws GameException {
		throw new GameException("Called method activateLeaderCard in StateActivateLeaderCard");
	}

	@Override
	public void activateWhichLeaderCard(String leader) throws GameException {
		for(LeaderCard lc : _player.getActivableLeaderCards()){
			if(lc.getName().equals(leader)){
				_theGame.getDynamicBar().activateLeaderCard(lc);
				
				//TODO interazioni con l'utente?
				
				_theGame.setState(new StateStartingTurn(_theGame));
				return;
			}
		}
	}

	@Override
	public List<FamilyMember> placeFamiliar() throws GameException {
		// TODO Auto-generated method stub
		return null;
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

}
