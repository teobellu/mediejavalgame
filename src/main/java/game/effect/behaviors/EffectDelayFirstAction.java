package game.effect.behaviors;

import game.Player;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectDelayFirstAction implements IEffectBehavior{

	private Player player;
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		delayTurn();
	}

	private void initializes(Effect ref){
		player = ref.getPlayer();
	}
	
	private void delayTurn(){
		//TODO
	}
}
