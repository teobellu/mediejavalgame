package model;

import static org.junit.Assert.assertTrue;


import org.junit.Test;

import server.game.effectControllers.EffectGetResource;

/**
 * Test model: Space
 * @author M
 *
 */
public class TestSpace {
	
	@Test
    public void createSpace() throws Exception {
		Space space = new Space(1, GC.NIX, true);
		assertTrue(space.getRequiredDiceValue() == 1);
		assertTrue(space.getCard() == null);
		assertTrue(space.getFamiliars().size() == 0);
		assertTrue(space.getInstantEffect() == GC.NIX);
		assertTrue(space.getInstantEffect().getSource().equals(GC.ACTION_SPACE));
		assertTrue(space.isSingleObject());
		
		Effect eff = new Effect(GC.IMMEDIATE, new EffectGetResource(null));
		Space space2 = new Space(1, eff, false);
		assertTrue(space2.getInstantEffect().getWhenActivate().equals(GC.IMMEDIATE));
		assertTrue(space2.getCard() == null);
		assertTrue(!space2.isSingleObject());
    }
	
	@Test
	 public void modifySpace() throws Exception {
		Space space = new Space(0, null, true);
		int size = space.getFamiliars().size();
		FamilyMember familiar = new FamilyMember(GC.FM_TRANSPARENT);
		space.placeFamiliar(familiar);
		assertTrue(space.getFamiliars().size() == size + 1);
		
		space.setSingleObject(false);
		assertTrue(!space.isSingleObject());
		
		space.setCard(new Territory("Name", 2, GC.NIX, GC.NIX, 3));
		assertTrue(space.getCard() == null);
    }
}
