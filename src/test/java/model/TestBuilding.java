package model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test model: Building
 *
 */
public class TestBuilding {
	
	@Test
    public void createBuilding() throws Exception {
		Resource cost = new Resource(GC.RES_SERVANTS, 5);
		DevelopmentCard card = new Building(1, "Name", cost, null, GC.NIX, 3);
		assertTrue(card.getName().equals("Name"));
		assertTrue(card.getAge() == 1);
		assertTrue(card.getImmediateEffect().size() == 1);
		assertTrue(card.getPermanentEffect().size() == 1);
		assertTrue(card.getPermanentEffect().get(0).getWhenActivate().equals(GC.NEVER));
		assertTrue(card.getDice() == 3);
		assertTrue(card.getCost(0).toString().equals(cost.toString()));
		assertTrue(card.getRequirement(0).toString().equals(""));
		assertTrue(card.getId() == 0);
		card.setId(4);
		assertTrue(card.getId() == 4);
		
		card.setDice(2);
		assertTrue(card.getDice() == 2);
		
		//visitor design pattern
		card.accept(new AppendsToListVisitor(new DevelopmentCardManager()));
    }
	
}
