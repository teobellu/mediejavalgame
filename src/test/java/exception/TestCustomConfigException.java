package exception;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.exceptions.CustomConfigException;

/**
 * Test CustomConfigException
 *
 */
public class TestCustomConfigException {
		
		/**
		 * Create a new custom config exception and verify correct initialization
		 */
		@Test
		public void createException(){
			Exception e = new CustomConfigException();
			assertTrue(e.getMessage()==null);
		}
		
		/**
		 * Throw a new custom config exception
		 * @throws CustomConfigException
		 */
		@Test(expected = CustomConfigException.class)
		public void throwException() throws CustomConfigException{
			throw new CustomConfigException();
		}
}
