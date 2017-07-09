package model;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import model.developmentCards.AppendsToListVisitor;
import model.developmentCards.DevelopmentCard;
import model.developmentCards.DevelopmentCardManager;
import model.developmentCards.Venture;

/**
 * Test model: Venture
 * @author M
 *
 */
public class TestVenture {
	
	@Test
    public void createVenture() throws Exception {
		Resource cost = new Resource(GC.RES_STONES, 6);
		List<Resource> costs = new ArrayList<>();
		costs.add(cost);
		List<Effect> effects = new ArrayList<>();
		effects.add(GC.NIX);
		effects.add(GC.NIX);
		DevelopmentCard card = new Venture(3, "Name", costs, costs, effects, 2);
		assertTrue(card.getName().equals("Name"));
		assertTrue(card.getAge() == 3);
		assertTrue(card.getImmediateEffect().size() == 2);
		assertTrue(card.getPermanentEffect().size() == 0);
		assertTrue(card.getDice() == 0);
		assertTrue(card.getCost(0).toString().equals(cost.toString()));
		assertTrue(card.getRequirement(0).get(GC.RES_STONES) == 6);
		assertTrue(((Venture)card).getVictoryReward() == 2);
		assertTrue(card.getId() == 0);
		card.setId(6);
		assertTrue(card.getId() == 6);
		
		assertTrue(card.getRequirement().equals(card.getCosts()));
		
		//visitor design pattern
		card.accept(new AppendsToListVisitor(new DevelopmentCardManager()));
    }
	
}
