package game.effect.behaviors;

import game.Player;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectWork implements IEffectBehavior{

	private Player player;			
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
		player = ref.getPlayer();
	}

	private void performWork() {
		player.getDynamicBar().launchesWork(value, action);
	}
}
