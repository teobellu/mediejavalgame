package game.effect.when;

import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.state.StateGaining;
import game.state.StateIncreaseWorker;

public class EffectWhenSetFamiliarStartPower extends Effect{

	public EffectWhenSetFamiliarStartPower(IEffectBehavior iEffectBehavior) {
		super(iEffectBehavior);
	}

	@Override
	public void effect (Effect when){
		when.effectWhenSetFamiliarStartPower();
	}
	
	@Override
	public void effectWhenSetFamiliarStartPower() {
		activateEffect();
	}
}
