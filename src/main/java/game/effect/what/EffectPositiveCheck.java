package game.effect.what;

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
	
	public void initializes(Effect ref){
		player = ref.getPlayer();
		
	}

	public void positiveCheck() {
		player.setCheck(true);
	}
}
