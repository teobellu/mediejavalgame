package game.state;

import java.util.List;

import exceptions.GameException;
import game.FamilyMember;
import game.Game;

public class StateActivateLeaderCard extends State {

	public StateActivateLeaderCard(Game game) {
		super(game);
	}

	@Override
	public List<String> dropLeaderCard() throws GameException {
		throw new GameException("Called method dropLeaderCard in StateActivateLeaderCard");
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
	public List<FamilyMember> placeFamiliar() throws GameException {
		// TODO Auto-generated method stub
		return null;
	}
}
