package model;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import model.developmentCards.AppendsToListVisitor;
import model.developmentCards.Character;
import model.developmentCards.DevelopmentCard;
import model.developmentCards.DevelopmentCardManager;

/**
 * Test model: Character
 * @author M
 *
 */
public class TestCharacter {
	
	@Test
    public void createCharacter() throws Exception {
		Resource cost = new Resource(GC.RES_WOOD, 2);
		List<Effect> effects = new ArrayList<>();
		effects.add(GC.NIX);
		effects.add(GC.NIX);
		DevelopmentCard card = new Character(2, "Name", cost, null, effects);
		assertTrue(card.getName().equals("Name"));
		assertTrue(card.getAge() == 2);
		assertTrue(card.getImmediateEffect().size() == 0);
		assertTrue(card.getPermanentEffect().size() == 2);
		assertTrue(card.getPermanentEffect().get(0).getWhenActivate().equals(GC.NEVER));
		assertTrue(card.getDice() == 0);
		assertTrue(card.getCost(0).toString().equals(cost.toString()));
		assertTrue(card.getRequirement(0).toString().equals(""));
		assertTrue(card.getId() == 0);
		card.setId(6);
		assertTrue(card.getId() == 6);
		
		//visitor design pattern
		card.accept(new AppendsToListVisitor(new DevelopmentCardManager()));
    }
	
}
