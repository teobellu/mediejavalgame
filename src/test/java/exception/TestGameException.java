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
		
	/**
	 * Create a new game exception and verify correct initialization
	 */
		@Test
		public void createException(){
			Exception e = new GameException(GC.NEVER);
			assertTrue(e.getMessage().equals(GC.NEVER));
		}
		
		/**
		 * Throw a game exception and verify message
		 * @throws GameException
		 */
		@Test(expected = GameException.class)
		public void throwException() throws GameException {
			throw new GameException("Hi!");
		}
}
