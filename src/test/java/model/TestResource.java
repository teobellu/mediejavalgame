package model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import exceptions.GameException;
import game.GC;
import game.Resource;

/**
 * Test model: Resource
 * @author M
 *
 */
public class TestResource {
	
	@Test
    public void createResource() throws Exception {
		Resource res1 = new Resource();
		assertTrue(res1.get(GC.RES_COINS) == 0);
		
		Resource res2 = new Resource(GC.RES_COINS, 1);
		assertTrue(res2.get(GC.RES_COINS) == 1);
		assertTrue(res2.get(GC.RES_WOOD) == 0);
		
		Resource res3 = new Resource(res2, GC.RES_WOOD, 2);
		assertTrue(res3.get(GC.RES_COINS) == 1);
		assertTrue(res3.get(GC.RES_WOOD) == 2);
		assertTrue(res3.get(GC.RES_STONES) == 0);
    }
	
	@Test
    public void addResource() throws Exception {
		Resource res1 = new Resource(GC.RES_COINS, 1);
		Resource res2 = new Resource(GC.RES_COINS, 1);
		res2.add(res1);
		
		assertTrue(res1.get(GC.RES_COINS) == 1);
		assertTrue(res2.get(GC.RES_COINS) == 2);
		
		res1.add(GC.RES_WOOD, 1);
		
		assertTrue(res1.get(GC.RES_COINS) == 1);
		assertTrue(res2.get(GC.RES_COINS) == 2);
		assertTrue(res1.get(GC.RES_WOOD) == 1);
		assertTrue(res2.get(GC.RES_WOOD) == 0);
		
		res2.add(res2);
		assertTrue(res1.get(GC.RES_COINS) == 1);
		assertTrue(res2.get(GC.RES_COINS) == 4);
		
		Resource res3 = res2;
		res2.add(null);
		assertTrue(res3.toString().equals(res2.toString()));
    }
	
	@Test
    public void subResource() throws Exception {
		Resource res1 = new Resource(GC.RES_COINS, 1);
		Resource res2 = new Resource(GC.RES_COINS, 5);
		
		res1.sub(res1);
		assertTrue(res1.get(GC.RES_COINS) == 0);
		
		res2.sub(res1);
		assertTrue(res2.get(GC.RES_COINS) == 5);
		
		res2.sub(new Resource(GC.RES_COINS, 1));
		assertTrue(res2.get(GC.RES_COINS) == 4);
    }
	
	@Test(expected = GameException.class)
	public void illegalSubException() throws GameException {
		Resource res1 = new Resource(GC.RES_COINS, 1);
		Resource res2 = new Resource(GC.RES_COINS, 5);
		res1.sub(res2);
	}
	
	@Test(expected = GameException.class)
	public void indefinedSubException() throws GameException {
		Resource res1 = new Resource(GC.RES_COINS, 1);
		res1.sub(new Resource(GC.RES_SERVANTS, 1));
	}
	
}
