package model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import game.GC;
import game.development.AppendsToListVisitor;
import game.development.Building;
import game.development.Character;
import game.development.DevelopmentCardManager;
import game.development.Territory;
import game.development.Venture;

/**
 * Test model: AppendsToListVisitor
 * @author M
 *
 */
public class TestAppendsToListVisitor {
	
	@Test
	public void createAppendsToListVisitor(){
		DevelopmentCardManager mngr = new DevelopmentCardManager();
		AppendsToListVisitor atlv = new AppendsToListVisitor(mngr);
		
		atlv.visit(new Territory(null, 0, null, null, 0));
		atlv.visit(new Building(0, null, null, null, null, 0));
		atlv.visit(new Character(0, null, null, null, null));
		atlv.visit(new Venture(0, null, null, null, null, 0));
		atlv.visit(new Venture(0, null, null, null, null, 0));
		atlv.visit(new Venture(1, null, null, null, null, 2));
		
		assertTrue(mngr.getList(GC.DEV_VENTURE).size() == 3);
	}
}
