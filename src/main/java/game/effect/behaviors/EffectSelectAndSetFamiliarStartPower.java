package game.effect.behaviors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import game.FamilyMember;
import game.GC;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectSelectAndSetFamiliarStartPower implements IEffectBehavior, Serializable{
	
	private String typeOfFamiliar;		//parametri
	private Integer valueToSet;
	
	private List<FamilyMember> familiars;
	private FamilyMember familiarToModify;
	
	public EffectSelectAndSetFamiliarStartPower(String typeOfFamiliar, Integer valueToSet) {
		this.typeOfFamiliar = typeOfFamiliar;
		this.valueToSet = valueToSet;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		selectFamiliarsToModify();
		setFamiliarsToModify();
	}
	
	private void initializes(Effect ref) {
		familiars = new ArrayList<>();
		familiars.addAll(ref.getPlayer().getFreeMember());
	}
	
	private void selectFamiliarsToModify() {
		List<FamilyMember> filteredFamiliars = new ArrayList<>();
		if (typeOfFamiliar == GC.FM_COLOR)
			filteredFamiliars.addAll(familiars.stream()
				.filter(fam -> fam.getColor() != GC.FM_TRANSPARENT)
				.collect(Collectors.toList()));
		if (typeOfFamiliar == GC.FM_TRANSPARENT)
			filteredFamiliars.addAll(familiars.stream()
				.filter(fam -> fam.getColor() == GC.FM_TRANSPARENT)
				.collect(Collectors.toList()));
		//TODO select familiarToModify dentro la lista filteredFamiliars
	}
	
	private void setFamiliarsToModify() {
		familiarToModify.setValue(valueToSet);
	}
}
