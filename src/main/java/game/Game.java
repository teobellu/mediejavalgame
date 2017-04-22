package game;

import server.Room;

public class Game {

	public Game(Room room) {
		_room = room;
	}
	
	private final Room _room;
	private BoardGame _boardGame;
}
