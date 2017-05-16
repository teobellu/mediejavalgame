package game.state;

import game.Game;
import server.Client;

public abstract class State {
	public State(Game game){
		_theGame = game;
	}
	
	public abstract State doState(Client player);
	
	protected final Game _theGame;
}
