package game.state;

import exceptions.GameException;
import game.Game;
import game.Player;
import server.Client;

public abstract class State {
	
	protected final Game _theGame;
	protected final Player _player;
	protected Client _client;
	
	public State(Game game){
		_theGame = game;
		_player = _theGame.getCurrentPlayer();
	}
	
	public abstract State doState();
	
	protected abstract State processAction(String action) throws GameException;
}