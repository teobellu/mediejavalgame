package game.effect.what;

import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectReducePower implements IEffectBehavior{
	
	private String action;		//parametri
	private Integer malus;
	
	private Integer currentPower;
	private Integer newPower;
	
	private Effect ref;
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		if(action != ref.getToScan()){
			ref.setToAnalyze(currentPower);
			return;
		}
		establishTax();
		payTax();
	}
	
	public void initializes(Effect ref) {
		newPower = new Integer(0);
		this.ref = ref;
		currentPower = (Integer) ref.getToAnalyze();
		action = (String) ref.getParameters(1);
		malus = (Integer) ref.getParameters();
	}
	
	public void establishTax() {
		newPower = Math.max(0, currentPower - malus);
	}
	
	public void payTax() {
		ref.setToAnalyze(newPower);
	}

}
