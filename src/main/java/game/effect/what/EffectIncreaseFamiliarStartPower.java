package game.effect.what;

import java.util.ArrayList;
import java.util.List;

import game.FamilyMember;
import game.GameConstants;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectIncreaseFamiliarStartPower implements IEffectBehavior{
	
	private String typeOfFamiliar;		//parametri
	private Integer bonus;
	
	private Integer currentPower;
	private Integer newPower;
	
	private List<FamilyMember> familiarsToModify;
	private Effect ref;
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		setFamiliarsToModify();
	}
	
	public void initializes(Effect ref) {
		newPower = new Integer(0);
		familiarsToModify = new ArrayList<>();
		this.ref = ref;
		currentPower = (Integer) ref.getToAnalyze();
		typeOfFamiliar = (String) ref.getParameters(1);
		bonus = (Integer) ref.getParameters();
		familiarsToModify.addAll(ref.getPlayer().getFreeMember());
	}
	
	public void setFamiliarsToModify() {
		if (typeOfFamiliar == GameConstants.FM_COLOR)
			familiarsToModify.stream()
				.filter(fam -> fam.getColor() != GameConstants.FM_TRANSPARENT)
				.forEach(fam -> fam.setValue(Math.max(0, fam.getValue() + bonus)));
		if (typeOfFamiliar == GameConstants.FM_TRANSPARENT)
			familiarsToModify.stream()
				.filter(fam -> fam.getColor() == GameConstants.FM_TRANSPARENT)
				.forEach(fam -> fam.setValue(Math.max(0, fam.getValue() + bonus)));
	}
}
