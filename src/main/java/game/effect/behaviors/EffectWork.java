package game.effect.behaviors;

import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectWork implements IEffectBehavior{

	private Effect effect;		
	private String action;
	private Integer value;

	public EffectWork(String action, Integer value) {
		this.action = action;
		this.value = value;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		performWork();
	}

	private void initializes(Effect ref){
		effect = ref;
	}

	private void performWork() {
		effect.getBar().launchesWork(value, action);
	}
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		String text = "Do action: " + action;
		text += " with power " + value;
		return text;
	}
}
