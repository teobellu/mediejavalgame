package server.game.effectControllers;

import java.util.ArrayList;
import java.util.List;

import model.Effect;
import model.FamilyMember;
import model.GC;

/**
 * Bonus Increase familiar (filter by type) start power
 * @author M
 *
 */
public class EffectIncreaseFamiliarStartPower implements IEffectBehavior{
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;
	
	private String typeOfFamiliar;
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
	
	/**
	 * Initializes the behavior of the effect
	 * @param ref Effect that possesses this behavior
	 */
	private void initializes(Effect ref) {
		familiarsToModify = new ArrayList<>();
		familiarsToModify.addAll(ref.getPlayer().getFreeMembers());
	}
	
	private void setFamiliarsToModify() {
		if (typeOfFamiliar.equals(GC.FM_COLOR))
			familiarsToModify.stream()
				.filter(fam -> !fam.getColor().equals(GC.FM_TRANSPARENT))
				.forEach(fam -> fam.setValue(Math.max(0, fam.getValue() + increase)));
		if (typeOfFamiliar.equals(GC.FM_TRANSPARENT))
			familiarsToModify.stream()
				.filter(fam -> fam.getColor().equals(GC.FM_TRANSPARENT))
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
