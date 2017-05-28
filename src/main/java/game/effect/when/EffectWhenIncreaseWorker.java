package game.effect.when;

import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.state.StateGaining;
import game.state.StateIncreaseWorker;

public class EffectWhenIncreaseWorker extends Effect{

	public EffectWhenIncreaseWorker(IEffectBehavior iEffectBehavior) {
		super(iEffectBehavior);
	}

	@Override
	public Object effect (Object param, StateIncreaseWorker state){
		return activateEffect((Integer) param);
	}
}
