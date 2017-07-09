package model;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import exceptions.GameException;
import game.FamilyMember;
import game.GC;
import game.LeaderCard;
import game.Resource;
import game.development.Building;
import game.development.DevelopmentCard;
import game.development.Territory;
import game.effect.Effect;
import game.effect.behaviors.EffectDoNothing;
import util.FakePlayer;

/**
 * Test model: Player
 * @author M
 *
 */
public class TestPlayer {
	
	@Test
    public void createPlayer() throws Exception {
		FakePlayer fake = new FakePlayer(null, GC.PLAYER_YELLOW);
		
		assertTrue(fake.getClient().getName().equals("Nickname"));
		assertTrue(fake.getName().equals("Nickname"));
		assertTrue(fake.getColour().equals(GC.PLAYER_YELLOW));
    }
	
	@Test
    public void addThingsToPlayer() throws Exception {
		FakePlayer fake = new FakePlayer(null, GC.PLAYER_YELLOW);
		
		//development cards
		
		DevelopmentCard dc1 = new Territory(null, 3, null, null, 0);
		DevelopmentCard dc2 = new Building(0, null, null, null, null, 0);
		DevelopmentCard dc3 = new Building(0, null, null, null, null, 0);
		dc1.setId(1);
		dc2.setId(2);
	
		
		fake.addDevelopmentCard(dc1);
		fake.addDevelopmentCard(dc2);
		fake.addDevelopmentCard(dc3); //card with no id
		fake.addDevelopmentCard(null); //null card
		assertTrue(fake.getDevelopmentCards().size() == 2);
		assertTrue(fake.getDevelopmentCards(GC.DEV_TERRITORY).size() == 1);
		assertTrue(fake.getDevelopmentCards(GC.DEV_BUILDING).size() == 1);
		assertTrue(fake.getDevelopmentCards(GC.DEV_CHARACTER).size() == 0);
		assertTrue(fake.getDevelopmentCards(GC.DEV_VENTURE).size() == 0);
		assertTrue(fake.getDevelopmentCards(GC.MARKET).size() == 0);
		
		fake.freeDevelopmentCards(GC.DEV_TERRITORY);
		assertTrue(fake.getDevelopmentCards(GC.DEV_TERRITORY).size() == 0);
		
		//leader cards
		
		fake.addLeaderCard(new LeaderCard(null, GC.NIX, player -> true));
		fake.addLeaderCard(new LeaderCard(null, GC.NIX, player -> false));
		
		assertTrue(fake.getLeaderCards().size() == 2);
		assertTrue(fake.getActivableLeaderCards().size() == 1);
		
		fake.removeLeaderCard(fake.getLeaderCards().get(0));
		
		assertTrue(fake.getLeaderCards().size() == 1);
		assertTrue(fake.getActivableLeaderCards().size() == 0); //player -> false
		
		//dashboard bonus
		
		fake.setHarvestBonus(new Resource(GC.RES_WOOD, 3));
		fake.setProductionBonus(new Resource(GC.RES_STONES, 4));
		
		assertTrue(fake.getBonus(GC.HARVEST).get(GC.RES_WOOD) == 3);
		assertTrue(fake.getBonus(GC.HARVEST).get(GC.RES_STONES) == 0);
		assertTrue(fake.getBonus(GC.PRODUCTION).get(GC.RES_STONES) == 4);
		assertTrue(fake.getBonus(GC.MARKET).get(GC.RES_STONES) == 0);
		
		//resources
		
		fake.gain(new Resource(GC.RES_SERVANTS, 4));
		assertTrue(!fake.getResource().toString().equals(""));
		assertTrue(fake.getResource(GC.RES_SERVANTS) == 4);
		fake.pay(new Resource(GC.RES_SERVANTS, 4));
		
		//familiars 
		
		List<FamilyMember> familiars = new ArrayList<>();
		familiars.add(new FamilyMember(GC.FM_ORANGE));
		familiars.add(new FamilyMember(GC.FM_TRANSPARENT));
		
		fake.setFreeMember(familiars);
		
		assertTrue(fake.getFreeMembers().size() == 2);
		assertTrue(fake.getFreeMembers().get(0).getColor() == GC.FM_ORANGE);
		assertTrue(fake.getFreeMembers().get(0).getOwner() == fake);
		
		//effects
		
		List<Effect> effects = new ArrayList<>();
		effects.add(null);
		effects.add(GC.NIX);
		effects.add(new Effect(GC.WHEN_FIND_VALUE_ACTION, new EffectDoNothing()));
		fake.addEffect(effects);
		
		assertTrue(fake.getEffects().size() == 0);
		
		//delay malus
		
		assertTrue(fake.getDelay() == 0);
		fake.setDelay(GC.DELAY);
		assertTrue(fake.getDelay() == GC.DELAY);
		
		//OPT (once per turn leader cards)
		
		fake.setOPTActivated(true);
		assertTrue(fake.getOPTActivated());

		//afk
		
		fake.setAfk(true);
		assertTrue(fake.isAfk());
		
		//vatica
		
		fake.setVaticanSupport(true);
		assertTrue(fake.isVaticanSupporter());
		
    }
	
	@Test(expected = GameException.class)
	public void illegalPayException() throws GameException {
		FakePlayer fake = new FakePlayer(null, GC.PLAYER_GREEN);
		fake.gain(new Resource(GC.RES_COINS, 2));
		fake.pay(new Resource(GC.RES_COINS, 4));
	}
}
