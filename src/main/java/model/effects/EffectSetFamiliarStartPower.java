package model.effects;

import java.util.ArrayList;
import java.util.List;

import model.Effect;
import model.FamilyMember;
import model.GC;
import model.IEffectBehavior;

/**
 * This effect behavior allows you to set a start power to a set of familiars
 * 
 * @author M
 *
 */
public class EffectSetFamiliarStartPower implements IEffectBehavior{
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Color of the familiar
	 */
	private String typeOfFamiliar;
	
	/**
	 * Value of the familiar to set
	 */
	private Integer valueToSet;
	
	/**
	 * Set of family members to modify
	 */
	private List<FamilyMember> familiarsToModify;
	
	/**
	 * Base constructor of the set familiar start power effect behavior
	 * @param typeOfFamiliar Color of the familiar
	 * @param valueToSet Value of the familiar to set
	 */
	public EffectSetFamiliarStartPower(String typeOfFamiliar, Integer valueToSet) {
		this.typeOfFamiliar = typeOfFamiliar;
		this.valueToSet = valueToSet;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		setFamiliarsToModify();
	}
	
	/**
	 * Initializes the behavior of the effect
	 * @param ref Effect that possesses this behavior
	 */
	private void initializes(Effect ref) {
		familiarsToModify = new ArrayList<>();
		familiarsToModify.addAll(ref.getPlayer().getFreeMembers());
	}
	
	/**
	 * This method modifies the family members in the list familiarsToModify
	 */
	private void setFamiliarsToModify() {
		if (typeOfFamiliar == GC.FM_COLOR)
			familiarsToModify.stream()
				.filter(fam -> !fam.getColor().equals(GC.FM_TRANSPARENT))
				.forEach(fam -> fam.setValue(Math.max(0, valueToSet)));
		if (typeOfFamiliar == GC.FM_TRANSPARENT)
			familiarsToModify.stream()
				.filter(fam -> fam.getColor().equals(GC.FM_TRANSPARENT))
				.forEach(fam -> fam.setValue(Math.max(0, valueToSet)));
	}
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		String text = "Set familiar start power with type: " + typeOfFamiliar;
		text += ", to power " + valueToSet;
		return text;
	}
}
