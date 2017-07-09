package model.effects;

import model.Effect;
import model.GC;
import model.IEffectBehavior;
import model.Player;

public class EffectDelayFirstAction implements IEffectBehavior{

	private Effect effect;
	private Player player;
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		delayTurn();
	}

	/**
	 * Initializes the behavior of the effect
	 * @param ref Effect that possesses this behavior
	 */
	private void initializes(Effect ref){
		effect = ref;
		player = ref.getPlayer();
	}
	
	private void delayTurn(){
		player.setDelay(GC.DELAY);
	}
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		return "Delay first action";
	}
}
