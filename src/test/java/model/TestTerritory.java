package model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import game.GC;
import game.development.AppendsToListVisitor;
import game.development.DevelopmentCard;
import game.development.DevelopmentCardManager;
import game.development.Territory;

/**
 * Test model: Territory
 * @author M
 *
 */
public class TestTerritory {
	
	@Test
    public void createTerritory() throws Exception {
		DevelopmentCard card = new Territory("Name", 1, GC.NIX, null, 3);
		assertTrue(card.getName().equals("Name"));
		assertTrue(card.getAge() == 1);
		assertTrue(card.getImmediateEffect().size() == 1);
		assertTrue(card.getPermanentEffect().size() == 1);
		assertTrue(card.getPermanentEffect().get(0) == null);
		assertTrue(card.getDice() == 3);
		assertTrue(card.getCost(0).toString().equals(""));
		assertTrue(card.getRequirement(0).toString().equals(""));
		assertTrue(card.getId() == 0);
		card.setId(4);
		assertTrue(card.getId() == 4);
		
		//visitor design pattern
		card.accept(new AppendsToListVisitor(new DevelopmentCardManager()));
    }
	
}
