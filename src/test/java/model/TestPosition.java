package model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import game.GC;
import game.Position;

/**
 * Test model: Position
 * @author M
 *
 */
public class TestPosition {
	
	@Test
    public void createPosition() throws Exception {
		Position p1 = new Position(GC.HARVEST);
		Position p2 = new Position(GC.MARKET, 1);
		Position p3 = new Position(GC.TOWER, 2, 3);
		
		assertTrue(p1.getRow() == 0);
		assertTrue(p1.getColumn() == 0);
		assertTrue(p1.getWhere().equals(GC.HARVEST));
		
		assertTrue(p2.getRow() == 1);
		assertTrue(p2.getColumn() == 0);
		assertTrue(p2.getWhere().equals(GC.MARKET));
		
		assertTrue(p3.getRow() == 2);
		assertTrue(p3.getColumn() == 3);
		assertTrue(p3.getWhere().equals(GC.TOWER));
    }
	
	@Test
    public void toStringNoException() throws Exception {
		Position p1 = new Position(GC.PRODUCTION);
		Position p2 = new Position(GC.MARKET, 3);
		Position p3 = new Position(GC.TOWER, 3, 1);
		
		//Verify the are no exception:
		p1.toString();
		p2.toString();
		p3.toString();
	}
	
}
