package game.state;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Logger;

import exceptions.GameException;
import game.FamilyMember;
import game.Game;
import game.LeaderCard;

public class StateDropLeaderCard extends State {

	public StateDropLeaderCard(Game game) {
		super(game);
	}

	@Override
	public List<String> dropLeaderCard() throws GameException {
		throw new GameException("Called method dropLeaderCard in StateDropLeaderCard");
	}

	@Override
	public void dropWhichLeaderCard(String leader) throws GameException {
		for(LeaderCard lc : _player.getLeaderCards()){
			if(lc.getName().equals(leader)){
				try {
					_theGame.getDynamicBar().discardLeaderCard(lc);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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
	public List<FamilyMember> placeFamiliar() throws GameException {
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
