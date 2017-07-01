package model;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;

import game.ExcommunicationTile;
import game.GC;
import game.LeaderCard;
import game.Player;

/**
 * Test model: Excommunication Tile
 * @author M
 *
 */
public class TestExcommunicationTile {
	
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
