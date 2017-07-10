package exception;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.GC;
import model.exceptions.GameException;

/**
 * GameException test
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
