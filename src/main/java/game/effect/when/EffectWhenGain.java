package game.effect.when;

import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.state.*;

public class EffectWhenGain extends Effect{

	public EffectWhenGain(IEffectBehavior iEffectBehavior) {
		super(iEffectBehavior);
	}

	public void effect (StateGaining state){
		activateEffect();
	}
}
