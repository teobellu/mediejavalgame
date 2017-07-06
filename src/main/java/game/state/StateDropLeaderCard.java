package game.state;

import java.util.List;

import exceptions.GameException;
import game.FamilyMember;
import game.Game;

public class StateDropLeaderCard extends State {

	public StateDropLeaderCard(Game game) {
		super(game);
	}

	@Override
	public List<String> dropLeaderCard() throws GameException {
		throw new GameException("Called method dropLeaderCard in StateDropLeaderCard");
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
	public List<FamilyMember> placeFamiliar() throws GameException {
		throw new GameException("Called method placeFamiliar in StateDropLeaderCard");
	}

}
