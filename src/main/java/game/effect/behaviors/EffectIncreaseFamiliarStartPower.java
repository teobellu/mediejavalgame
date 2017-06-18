package game.effect.behaviors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import game.FamilyMember;
import game.GC;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectIncreaseFamiliarStartPower implements IEffectBehavior{
	
	private String typeOfFamiliar;		//parametri
	private Integer increase;
	
	private List<FamilyMember> familiarsToModify;
	
	public EffectIncreaseFamiliarStartPower(String typeOfFamiliar, Integer increase) {
		this.typeOfFamiliar = typeOfFamiliar;
		this.increase = increase;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		setFamiliarsToModify();
	}
	
	private void initializes(Effect ref) {
		familiarsToModify = new ArrayList<>();
		familiarsToModify.addAll(ref.getPlayer().getFreeMember());
	}
	
	private void setFamiliarsToModify() {
		if (typeOfFamiliar == GC.FM_COLOR)
			familiarsToModify.stream()
				.filter(fam -> fam.getColor() != GC.FM_TRANSPARENT)
				.forEach(fam -> fam.setValue(Math.max(0, fam.getValue() + increase)));
		if (typeOfFamiliar == GC.FM_TRANSPARENT)
			familiarsToModify.stream()
				.filter(fam -> fam.getColor() == GC.FM_TRANSPARENT)
				.forEach(fam -> fam.setValue(Math.max(0, fam.getValue() + increase)));
	}
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		String text = "All familiars with type: " + typeOfFamiliar;
		if (increase == 0)
			return "Nothing";
		if (increase > 0)
			text += ", will have an increment of " + increase;
		else if (increase < 0)
			text += ", will have a decrement of " + -increase;
		return text;
	}
}
