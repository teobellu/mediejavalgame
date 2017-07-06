package game.state;

import java.util.List;

import exceptions.GameException;
import game.FamilyMember;
import game.Game;

public class VaticanState extends State {

	public VaticanState(Game game) {
		super(game);
	}

	@Override
	public List<String> dropLeaderCard() throws GameException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean endTurn() throws GameException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> activateLeaderCard() throws GameException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FamilyMember> placeFamiliar() throws GameException {
		// TODO Auto-generated method stub
		return null;
	}
}
