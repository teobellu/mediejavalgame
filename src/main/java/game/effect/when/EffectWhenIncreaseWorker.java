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
	public Object effect (Object param, Effect when){
		return when.effectWhenIncreaseWorker(param);
	}
	
	@Override
	public Object effectWhenIncreaseWorker(Object param) {
		return activateEffect((Integer) param);
	}
}
