package controller;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import model.Effect;
import model.ExcommunicationTile;
import model.FamilyMember;
import model.GC;
import model.GameBoard;
import model.LeaderCard;
import model.Resource;
import model.Territory;
import model.exceptions.GameException;
import server.Room;
import server.game.DynamicAction;
import server.game.Game;
import server.game.GameInformation;
import server.game.State;

/**
 * Some tests of game (controller)
 *
 */
public class TestGame {
	
	/**
	 * Constructor
	 * @throws Exception
	 */
	@Test
	public void TestGameGenerate() throws Exception{
		Game game = new Game(new Room(null));
		
		//Gameboard
		Map<String, List<Effect>> map = new HashMap<>();
		List<Effect> effects = new ArrayList<>();
		for (int i = 0; i < 16; i++)
			effects.add(GC.NIX);
		map.put(GC.COUNCIL_PALACE, effects);
		map.put(GC.MARKET, effects);
		map.put(GC.HARVEST, effects);
		map.put(GC.PRODUCTION, effects);
		map.put(GC.TOWER, effects);
		GameBoard board = new GameBoard(map);
		
		Integer[] dices = {2, 5, 6};
		board.setDices(dices);
		
		ExcommunicationTile tile1 = new ExcommunicationTile(1, GC.NIX);
		ExcommunicationTile tile2 = new ExcommunicationTile(2, null);
		ExcommunicationTile tile3 = new ExcommunicationTile(3, GC.NIX);
		ExcommunicationTile[] tiles = {tile1, tile2, tile3};
		board.setExCard(tiles);
		
		board.getFromTowers(0, 0).setCard(new Territory(null, 3, GC.NIX, GC.NIX, 2));
		
		game.getGameInformation().setBonusFaith(Arrays.asList(1,2,4,5,6,7,8));
		
		game.setBoard(board);
		assertTrue(game.getBoard() == board);
		
		game.setTurnTimeout(1000);
		assertTrue(game.getTurnTimeout() == 1000);
		assertTrue(!game.isOver());
		
		FakePlayer p = new FakePlayer(null, GC.PLAYER_YELLOW);
		
		game.getPlayers().add(p);
		
		game.setListener(null);
		assertTrue(game.getListener() == null);
		
		
	}
	
	/**
	 * No connection handler setted
	 * @throws GameException
	 */
	@Test(expected = Exception.class)
	public void throwException() throws Exception {
		Game game = new Game(new Room(null));
		FakePlayer p = new FakePlayer(null, GC.PLAYER_YELLOW);
		game.getPlayers().add(p);
		State s = new State(game);
		
		game.setState(s);
		assertTrue(game.getState() == s);
		
		s.setupNewTurn(p);
		s.nextState();
	}
	
	
}
