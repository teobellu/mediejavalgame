package game.effect.behaviors;

import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectIncreaseActionPower implements IEffectBehavior{
	
	private String action;		//parametri
	private Integer increase;
	
	private Integer currentPower;
	private Integer newPower;
	
	private Effect ref;
	
	public EffectIncreaseActionPower(String action, int increase) {
		this.action = action;
		this.increase = increase;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		if(action != ref.getToScan()) 
			return;
		establishTax();
		payTax();
	}
	
	private void initializes(Effect ref) {
		newPower = new Integer(0);
		this.ref = ref;
		currentPower = (Integer) ref.getToAnalyze();
	}
	
	private void establishTax() {
		newPower = Math.max(0, currentPower + increase);
	}
	
	private void payTax() {
		ref.setToAnalyze(newPower);
	}
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		String text = "Action " + action;
		if (increase == 0)
			return "Nothing";
		if (increase > 0)
			text += " increased by " + increase;
		else if (increase < 0)
			text += " decreased by " + -increase;
		return text;
	}

}
