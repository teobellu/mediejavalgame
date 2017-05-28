package game.effect.what;

import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectSufferPower implements IEffectBehavior{
		
	private Integer currentPower;	//power attuale
	private Integer malus;			//quanto power devo togliere
	private Integer newPower;
	private Effect ref;
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		establishTax();
		payTax();
	}
	
	public void initializes(Effect ref){
		newPower = new Integer(0);
		this.ref = ref;
		currentPower = (Integer) ref.getToAnalyze();
		malus = (Integer) ref.getParameters();
	}

	public void establishTax() {
		newPower = Math.max(0, currentPower - malus);
	}
	
	public void payTax() {
		ref.setToAnalyze(newPower);
	}
		
}
