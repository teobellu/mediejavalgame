package model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test model: Excommunication Tile
 *
 */
public class TestExcommunicationTile {
	
	/**
	 * Create a new excom. tile and verify correct initialization
	 * @throws Exception
	 */
	@Test
    public void createExcommunicationTile() throws Exception {
		ExcommunicationTile tile = new ExcommunicationTile(1, GC.NIX);
		assertTrue(tile.getID() == 0);
		tile.setId(1);
		assertTrue(tile.getAge() == 1);
		assertTrue(tile.getEffect() == GC.NIX);
		assertTrue(tile.getEffect().getWhenActivate() == GC.NEVER);
		assertTrue(tile.getID() == 1);
    }
	
}
