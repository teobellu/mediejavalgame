package game.state;

import java.util.List;

import exceptions.GameException;
import game.FamilyMember;
import game.Game;

public class StatePlaceFamiliar extends State {

	public StatePlaceFamiliar(Game game) {
		super(game);
	}
	
	@Override
	public List<String> dropLeaderCard() throws GameException {
		throw new GameException("Called method dropLeaderCard in StatePlaceFamiliar");
	}

	@Override
	public boolean endTurn() throws GameException {
		throw new GameException("Called method endTurn in StatePlaceFamiliar");
	}

	@Override
	public List<String> activateLeaderCard() throws GameException {
		throw new GameException("Called method activateLeaderCard in StatePlaceFamiliar");
	}

	@Override
	public List<FamilyMember> placeFamiliar() throws GameException {
		throw new GameException("Called method placeFamiliar in StatePlaceFamiliar");
	}

	public List<String> placeWhichFamiliar(int whichFamiliar) throws GameException {
		/*if (whichFamiliar < 0 || whichFamiliar >= _player.getFreeMember().size())
			throw new GameException("Invalid input, you don't have this familiar!");
		FamilyMember familiar = _player.getFreeMember().get(whichFamiliar);
		_whichFamiliar = familiar;
		List<String> placesAvailable = new ArrayList<>();
		//TODO calcolare quale posti sono disponibili
		
		return placesAvailable;*/
		return null;
	}
}
