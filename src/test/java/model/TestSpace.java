package model;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;

import game.ExcommunicationTile;
import game.FamilyMember;
import game.GC;
import game.LeaderCard;
import game.Player;
import game.Space;
import game.development.Territory;
import game.effect.Effect;
import game.effect.behaviors.EffectGetResource;

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
		
		
		space.setFamiliar(new FamilyMember(GC.FM_TRANSPARENT));
		assertTrue(space.getFamiliars().size() == 1);
		
		space.setFamiliar(new FamilyMember(GC.FM_TRANSPARENT));
		assertTrue(space.getFamiliars().size() == 2);
		
		Effect eff = new Effect(GC.IMMEDIATE, new EffectGetResource(null));
		
    }
	
}
