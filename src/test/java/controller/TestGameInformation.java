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
import model.GC;
import model.LeaderCard;
import model.Player;
import server.Room;
import server.game.Game;
import server.game.GameInformation;

/**
 * Test controller: GameInformation (Controller)
 *
 */
public class TestGameInformation {
	
	/**
	 * List of fake player to do tests
	 */
	private static final List<Player> FAKE_PLAYERS = new ArrayList<>(
			Arrays.asList(
					new FakePlayer(null, GC.PLAYER_RED), 
					new FakePlayer(null, GC.PLAYER_BLUE)));
	
	/**
	 * Create a new game information and verify correct initialization
	 */
	@Test
	public void createGameInformation(){
		Game game = new Game(new Room(null));
		GameInformation gi = new GameInformation(game);
		
		assertTrue(gi.getLeaderDeck().size() == 20);
	} 
	
	/**
	 * Test using gameboard and gameinformation, like verify modifing methods & get/set
	 */
	@Test
	public void manageGameBoard(){
		Game game = new Game(new Room(null));
		game.getPlayers().addAll(FAKE_PLAYERS);
		GameInformation gi = new GameInformation(game);
		
		Map<String, List<Effect>> map = new HashMap<>();
		List<Effect> effects = new ArrayList<>();
		for (int i = 0; i < 16; i++)
			effects.add(GC.NIX);
		map.put(GC.COUNCIL_PALACE, effects);
		map.put(GC.MARKET, effects);
		map.put(GC.HARVEST, effects);
		map.put(GC.PRODUCTION, effects);
		map.put(GC.TOWER, effects);
		gi.createBoard(map);
		
		gi.setBonusFaith(Arrays.asList(1,2,3,4,5,6,7,9,11,13,15,17,19,22,25));
		assertTrue(gi.getBonusFaith().get(3) == 4);
				
		ExcommunicationTile tile1 = new ExcommunicationTile(1, null);
		ExcommunicationTile tile2 = new ExcommunicationTile(2, GC.NIX);
		ExcommunicationTile tile3 = new ExcommunicationTile(3, null);
		List<ExcommunicationTile> tiles = Arrays.asList(tile1, tile2, tile3);
		gi.setExcommunicationDeck(tiles);
		gi.setExcommunicationTitlesOnBoard();
		
		assertTrue(gi.getExcommunicationDeck().size() == 3);
		
		gi.setDevelopmentDeck(new ArrayList<>());
		
		assertTrue(gi.getDevelopmentDeck().isEmpty());
		
		gi.newPhase(2);
	}
	
	/**
	 * Manage a list of players using the list at the top of this class
	 */
	@Test
	public void manageListsOfPlayers(){
		FakePlayer g1 = new FakePlayer(null, GC.PLAYER_RED);
		FakePlayer g2 = new FakePlayer(null, GC.PLAYER_BLUE);
		
		Game game = new Game(new Room(null));
		game.getPlayers().add(g1);
		game.getPlayers().add(g2);
		
		GameInformation gi = new GameInformation(game);
		
		List<Player> list = new ArrayList<>(Arrays.asList(g1, g2));
		
		//methods
		
		gi.endOfTheGameFindWinners();
		
		//council palace working list
		
		gi.setHeadPlayersTurn(list);
		assertTrue(gi.getHeadPlayersTurn().size() == 2);
		
		//dashboard bonus
		
		gi.setBonusPlayerDashBoard(new HashMap<>());
		assertTrue(gi.getBonusPlayerDashBoard().isEmpty());
		
		//discarded leader cards
		
		gi.addDiscardedLeader(new LeaderCard("Name", GC.NIX, player -> true), g2);
		assertTrue(gi.getDiscardedLeader().size() == 1);
		
		
	}
}
