package game.effect.when;

import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.state.*;

public class EffectWhenJoiningSpace extends Effect{

	public EffectWhenJoiningSpace(IEffectBehavior iEffectBehavior) {
		super(iEffectBehavior);
	}

	@Override
	public void effect (StateEndingGame state){
		this.activateEffect();
	}
}

