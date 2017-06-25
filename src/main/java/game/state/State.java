package game.state;

import java.util.List;

import exceptions.GameException;
import game.FamilyMember;
import game.Game;
import game.Player;
import server.Client;

public abstract class State {
	
	protected int age;
	protected int phase;
	protected final Game _theGame; //TODO
	protected final Player _player;
	protected Client _client;
	
	public State(Game game){
		_theGame = game;
		_player = _theGame.getCurrentPlayer();
	}
	
	public abstract List<String> dropLeaderCard() throws GameException;
	
	public abstract void dropWhichLeaderCard(String leader) throws GameException;
	
	public abstract boolean endTurn() throws GameException;
	
	public abstract List<String> activateLeaderCard() throws GameException;

	//TODO alcune carte richiedono interazione
	public abstract void activateWhichLeaderCard(String leader) throws GameException;
	
	public abstract List<FamilyMember> placeFamiliar() throws GameException;
	
	public abstract List<String> placeWhichFamiliar(String familiar) throws GameException;
	
	//TODO string?
	public abstract void placeWhereFamiliar(String position) throws GameException;
}