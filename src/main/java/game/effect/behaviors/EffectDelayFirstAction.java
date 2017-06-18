package game.effect.behaviors;

import java.io.Serializable;

import game.Player;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectDelayFirstAction implements IEffectBehavior, Serializable{

	private Effect effect;
	private Player player;
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		delayTurn();
	}

	private void initializes(Effect ref){
		effect = ref;
		player = ref.getPlayer();
	}
	
	private void delayTurn(){
		effect.getBar().addDelayMalus();
	}
}
