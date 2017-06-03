package game.effect.behaviors;

import java.util.ArrayList;
import java.util.List;

import game.FamilyMember;
import game.GC;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectSetFamiliarStartPower implements IEffectBehavior{
	
	private String typeOfFamiliar;		//parametri
	private Integer valueToSet;
	
	private Integer currentPower;
	private Integer newPower;
	
	private List<FamilyMember> familiarsToModify;
	private Effect ref;
	
	public EffectSetFamiliarStartPower(String typeOfFamiliar, Integer valueToSet) {
		this.typeOfFamiliar = typeOfFamiliar;
		this.valueToSet = valueToSet;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		setFamiliarsToModify();
	}
	
	private void initializes(Effect ref) {
		newPower = new Integer(0);
		familiarsToModify = new ArrayList<>();
		this.ref = ref;
		currentPower = (Integer) ref.getToAnalyze();
		familiarsToModify.addAll(ref.getPlayer().getFreeMember());
	}
	
	private void setFamiliarsToModify() {
		if (typeOfFamiliar == GC.FM_COLOR)
			familiarsToModify.stream()
				.filter(fam -> fam.getColor() != GC.FM_TRANSPARENT)
				.forEach(fam -> fam.setValue(Math.max(0, valueToSet)));
		if (typeOfFamiliar == GC.FM_TRANSPARENT)
			familiarsToModify.stream()
				.filter(fam -> fam.getColor() == GC.FM_TRANSPARENT)
				.forEach(fam -> fam.setValue(Math.max(0, valueToSet)));
	}
}
