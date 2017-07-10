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
import model.exceptions.GameException;
import server.Room;
import server.game.DynamicAction;
import server.game.Game;

public class TestDynamicAction {
	
	/**
	 * A controller
	 */
	private DynamicAction dynamicA;
	
	/**
	 * A player
	 */
	private FakePlayer player;
	
	/**
	 * His familiar (of player)
	 */
	private FamilyMember familiar;
	
	/**
	 * Constructor
	 * @throws Exception
	 */
	public TestDynamicAction() throws Exception{
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
		
		game.getGameInformation().setBonusFaith(Arrays.asList(1,2,4,5,6,7,8));
		
		game.setBoard(board);
		dynamicA = new DynamicAction(game);

		//create a player
		player = new FakePlayer(null, GC.PLAYER_RED);
		familiar = new FamilyMember(GC.FM_BLACK);
		familiar.setOwner(player);
		dynamicA.setPlayer(player);
		
		assertTrue(dynamicA.getDiscardedLeaderCards()
				.equals(game.getGameInformation().getDiscardedLeader()));
		
	}
	
	/**
	 * E.G. of operation
	 * @throws Exception
	 */
	@Test
	public void operationVaticanSupport() throws Exception {
		//don't  show support
		dynamicA.showVaticanSupport(1);
		
		//show support
		player.setVaticanSupport(true);
		player.gain(new Resource(GC.RES_FAITHPOINTS, 10));
		dynamicA.showVaticanSupport(3);
	}
	
	/**
	 * Activate an activable leader card
	 * @throws Exception
	 */
	@Test
	public void operationActivateLeader() throws Exception {
		dynamicA.activateLeaderCard(new LeaderCard(null, null, player -> true));
	}
	
	/**
	 * Activate a not activable leader card
	 * @throws GameException
	 */
	@Test(expected = GameException.class)
	public void operationActivateLeaderIllegal() throws GameException {
		dynamicA.activateLeaderCard(new LeaderCard(null, null, player -> false));
	}
	
	/**
	 * Illegal place market operation
	 * @throws GameException
	 */
	@Test(expected = GameException.class)
	public void illegalPlaceMarketOperation() throws GameException {
		dynamicA.placeMarket(familiar, 3);
	}
	
	/**
	 * Gain operation
	 * @throws Exception
	 */
	@Test
	public void operationGainAndPay() throws Exception {
		int x = player.getResource(GC.RES_STONES);
		dynamicA.gain(GC.NIX, new Resource(GC.RES_STONES, 2));
		assertTrue(player.getResource(GC.RES_STONES) == x + 2);
	}
}
