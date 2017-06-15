package game.state;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.GameException;
import game.Game;
import util.CommandStrings;

public class StatePlaceFamiliar extends State {

	public StatePlaceFamiliar(Game game) {
		super(game);
	}

	@Override
	public State doState() {
		try {
			String action = _theGame.getNextGameAction();
			
			return processAction(action);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			return null;
		}
	}
	
	@Override
	public List<String> dropLeaderCard() throws GameException {
		throw new GameException("Called method dropLeaderCard in StatePlaceFamiliar");
	}

	@Override
	public void dropWhichLeaderCard(String leader) throws GameException {
		throw new GameException("Called method dropWhichLeaderCard in StatePlaceFamiliar");
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
	public void activateWhichLeaderCard(String leader) throws GameException {
		throw new GameException("Called method activateWhichLeaderCard in StatePlaceFamiliar");
	}
	
	@Override
	public List<String> placeFamiliar() throws GameException {
		throw new GameException("Called method placeFamiliar in StatePlaceFamiliar");
	}

	@Override
	public List<String> placeWhichFamiliar(String familiar) throws GameException {
		_whichFamiliar = familiar;
		List<String> placesAvailable = new ArrayList<>();
		//TODO calcolare quale posti sono disponibili
		
		return placesAvailable;
	}

	@Override
	public void placeWhereFamiliar(String position) throws GameException {
		// TODO Auto-generated method stub
	}
	
	private String _whichFamiliar;
	
	private Logger _log = Logger.getLogger(StatePlaceFamiliar.class.getName());
}
