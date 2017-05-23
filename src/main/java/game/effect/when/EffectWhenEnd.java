package game.effect.when;

import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.state.*;

public class EffectWhenEnd extends Effect{

	public EffectWhenEnd(IEffectBehavior iEffectBehavior) {
		super(iEffectBehavior);
	}

	public void effect (StateEndingGame state){
		this.activateEffect();
	}
}

