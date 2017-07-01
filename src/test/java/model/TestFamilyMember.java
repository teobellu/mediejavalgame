package model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import game.FamilyMember;
import game.GC;

/**
 * Test model: Family Member
 * @author M
 *
 */
public class TestFamilyMember {
	
	@Test
    public void createFamilyMember() throws Exception {
		FamilyMember familiar = new FamilyMember(GC.FM_BLACK);
		assertTrue(familiar.getColor().equals(GC.FM_BLACK));
		assertTrue(familiar.getOwner() == null);
		assertTrue(familiar.getValue() == 0);
    }
	
	@Test
    public void valueFamilyMember() throws Exception {
		FamilyMember familiar = new FamilyMember(GC.FM_TRANSPARENT);
		assertTrue(familiar.getValue() == 0);
		familiar.setValue(1);
		assertTrue(familiar.getValue() == 1);
		familiar.setValue(2);
		assertTrue(familiar.getValue() == 2);
    }
	
}
