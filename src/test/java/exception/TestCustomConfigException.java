package exception;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.exceptions.CustomConfigException;

/**
 * Test CustomConfigException
 *
 */
public class TestCustomConfigException {
		
		@Test
		public void createException(){
			Exception e = new CustomConfigException();
			assertTrue(e.getMessage()==null);
		}
		
		@Test(expected = CustomConfigException.class)
		public void throwException() throws CustomConfigException{
			throw new CustomConfigException();
		}
}
