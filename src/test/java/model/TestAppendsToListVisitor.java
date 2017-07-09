package model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.developmentCards.AppendsToListVisitor;
import model.developmentCards.Building;
import model.developmentCards.Character;
import model.developmentCards.DevelopmentCardManager;
import model.developmentCards.Territory;
import model.developmentCards.Venture;

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
