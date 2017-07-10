package model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import controller.FakePlayer;
import server.game.effectControllers.EffectDelayFirstAction;
import server.game.effectControllers.EffectDoNothing;

/**
 * Test model: Effect
 *
 */
public class TestEffect {
	
	@Test
	public void createEffect(){
		/**
		 * Create a new effect and verify correct initialization
		 */
		Effect eff = new Effect(GC.NEVER, new EffectDoNothing());
		eff.setBar(null);
		eff.setPlayer(new FakePlayer(null, GC.PLAYER_BLUE));
		eff.setSource(GC.ACTION_SPACE);
		eff.setToScan(GC.END_TURN);
		eff.setToAnalyze(new Resource());
		
		assertTrue(eff.getBar() == null);
		assertTrue(eff.getSource().equals(GC.ACTION_SPACE));
		assertTrue(eff.getPlayer().getColour().equals(GC.PLAYER_BLUE));
		assertTrue(eff.getToScan().equals(GC.END_TURN));
		assertTrue(eff.getToAnalyze() instanceof Resource);
		
		//no exception for toString
		eff.toString(); 
	}
	
	/**
	 * Try to use an effect to a fake player {@link FakePlayer}
	 */
	@Test
	public void useAnEffect(){
		Effect eff = new Effect(GC.NEVER, new EffectDelayFirstAction());
		FakePlayer fake = new FakePlayer(null, GC.PLAYER_RED);
		assertTrue(fake.getDelay() == GC.NORMAL);
		eff.setPlayer(fake);
		eff.activateEffect(GC.NEVER, new Resource(), GC.ACTION_SPACE);
		assertTrue(fake.getDelay() == GC.DELAY);
	}
}
