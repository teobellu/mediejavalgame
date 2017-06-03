package game.effect.behaviors;

import game.Player;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectPositiveCheck implements IEffectBehavior{

	private Player player;
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		positiveCheck();
	}
	
	private void initializes(Effect ref){
		player = ref.getPlayer();
		
	}

	private void positiveCheck() {
		player.setCheck(true);
	}
}
