package exception;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import exceptions.GameException;
import game.GC;

/**
 * GameException test
 * @author M
 *
 */
public class TestGameException {
		
		@Test
		public void createException(){
			Exception e = new GameException(GC.NEVER);
			assertTrue(e.getMessage().equals(GC.NEVER));
		}
		
		@Test(expected = GameException.class)
		public void throwException() throws GameException {
			throw new GameException("Hi!");
		}
}
