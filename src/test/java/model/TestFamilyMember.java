package model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

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
    public void identifyFamilyMember() throws Exception {
		FamilyMember familiar1 = new FamilyMember(GC.FM_ORANGE);
		FamilyMember familiar2 = new FamilyMember(GC.FM_BLACK);
		FamilyMember familiar3 = new FamilyMember(GC.FM_BLACK);
		FamilyMember familiar4 = new FamilyMember(GC.FM_BLACK);
		
		familiar1.setValue(1);
		familiar2.setValue(1);
		familiar3.setValue(5);
		familiar4.setValue(5);
		
		//simmetry
		assertTrue(familiar1.toString().equals(familiar1.toString()));
		
		
		assertTrue(!familiar1.toString().equals(familiar2.toString()));
		assertTrue(!familiar1.toString().equals(familiar3.toString()));
		assertTrue(!familiar1.toString().equals(familiar4.toString()));
		
		assertTrue(!familiar2.toString().equals(familiar3.toString()));
		assertTrue(!familiar2.toString().equals(familiar4.toString()));
		
		//different reference, equals values
		assertTrue(familiar3.toString().equals(familiar4.toString()));
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
	
	@Test
    public void nullOwnerFamilyMember() throws Exception {
		FamilyMember familiar = new FamilyMember(GC.FM_ORANGE);
		familiar.setOwner(null);
		assertTrue(familiar.getOwner() == null);
    }
	
}
