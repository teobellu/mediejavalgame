package model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import game.GC;
import game.development.Building;
import game.development.Character;
import game.development.DevelopmentCardManager;
import game.development.Territory;
import game.development.Venture;

public class TestDevelopmentCardManager {
	
	@Test
	public void createDevelopmentCardManager(){
		DevelopmentCardManager mngr = new DevelopmentCardManager();
		mngr.add(new Territory(null, 0, null, null, 0));
		mngr.add(new Building(0, null, null, null, null, 0));
		mngr.add(new Building(0, null, null, null, null, 0));
		mngr.add(new Character(0, null, null, null, null));
		mngr.add(new Character(0, null, null, null, null));
		mngr.add(new Character(0, null, null, null, null));
		mngr.add(new Venture(0, null, null, null, null, 0));
		
		assertTrue(mngr.getList(GC.DEV_TERRITORY).size() == 1);
		assertTrue(mngr.getList(GC.DEV_BUILDING).size() == 2);
		assertTrue(mngr.getList(GC.DEV_CHARACTER).size() == 3);
		assertTrue(mngr.getList(GC.DEV_VENTURE).size() == 1);
		assertTrue(mngr.getList(GC.MARKET).size() == 0);
		
		mngr.freeList(GC.DEV_TERRITORY);
		mngr.freeList(GC.DEV_BUILDING);
		mngr.freeList(GC.DEV_CHARACTER);
		mngr.freeList(GC.DEV_VENTURE);
		mngr.freeList(GC.MARKET);
		
		assertTrue(mngr.getList(GC.DEV_TERRITORY).size() == 0);
		assertTrue(mngr.getList(GC.DEV_BUILDING).size() == 0);
		assertTrue(mngr.getList(GC.DEV_CHARACTER).size() == 0);
		assertTrue(mngr.getList(GC.DEV_VENTURE).size() == 0);
		assertTrue(mngr.getList(GC.MARKET).size() == 0);
	}
}
