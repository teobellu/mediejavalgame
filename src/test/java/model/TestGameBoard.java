package model;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Test model: GameBoard
 *
 */
public class TestGameBoard {
	
	/**
	 * Create a gameboard
	 * @throws Exception
	 */
	@Test
    public void createGameBoard() throws Exception {
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
		board.clearPos();
		//council palace
		assertTrue(!board.getCouncilPalaceSpace().isSingleObject());
		
		//work spaces
		assertTrue(board.getWorkSpace(GC.HARVEST).isSingleObject());
		assertTrue(board.getWorkSpace(GC.PRODUCTION).isSingleObject());
		assertTrue(board.getWorkSpace(GC.MARKET) == null);
		assertTrue(!board.getWorkLongSpace(GC.HARVEST).isSingleObject());
		assertTrue(!board.getWorkLongSpace(GC.PRODUCTION).isSingleObject());
		assertTrue(board.getWorkLongSpace(GC.MARKET) == null);
		
		/**
		 * Market test
		 */
		assertTrue(board.getMarketSpace(0).isSingleObject());
		
		/**
		 * Tower test
		 */
		assertTrue(board.getFromTowers(0, 0) != null);
		
		/**
		 * Get familiars in same column test
		 */
		assertTrue(board.getFamiliarInSameColumn(2).size() == 0);
		
		/**
		 * Three Dices
		 */
		Integer[] dices = {2, 5, 6};
		board.setDices(dices);
		assertTrue(board.getDices()[2] == 6);
		
		/**
		 * Set & drop excommunication tiles
		 */
		ExcommunicationTile tile1 = new ExcommunicationTile(1, GC.NIX);
		ExcommunicationTile tile2 = new ExcommunicationTile(2, null);
		ExcommunicationTile tile3 = new ExcommunicationTile(3, GC.NIX);
		ExcommunicationTile[] tiles = {tile1, tile2, tile3};
		board.setExCard(tiles);
		assertTrue(board.getExCard()[0].getAge() == 1);
		assertTrue(board.getExCard()[1].getEffect() == null);
		assertTrue(board.getExCard()[2].getEffect().getWhenActivate().equals(GC.NEVER));

		assertTrue(board.getCard(0, 1) == null);
    }
	
	/**
	 * Set development cards on board, generally readen from xml, but we use a iterator cycle
	 * for create a simple test
	 */
	@Test
	public void setDevelopmentCardsOnBoard(){
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
		
		List<DevelopmentCard> deck = new ArrayList<>();
		for (int i = 0; i < 4; i++)
			deck.add(new Territory("Name", 2, GC.NIX, null, 3));
		
		//if no dev cards are aviable
		board.generateDevelopmentCards(deck, 1);
		assertTrue(board.getCard(0, 0) == null);
		
		//if exists dev cards aviable
		board.generateDevelopmentCards(deck, 2);
		assertTrue(board.getCard(0, 0).getName().equals("Name"));
		assertTrue(board.getCard(1, 1) == null);
	}
	
	
}
