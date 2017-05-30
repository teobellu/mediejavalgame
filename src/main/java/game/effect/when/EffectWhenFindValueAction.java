package game.effect.when;

import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.state.StateActionValue;
import game.state.StateIncreaseWorker;
import game.state.StateJoiningSpace;

public class EffectWhenFindValueAction extends Effect{

	public EffectWhenFindValueAction(IEffectBehavior iEffectBehavior) {
		super(iEffectBehavior);
	}
	
	@Override
	public Object effect (Object param, String message, Effect when){
		return when.effectWhenFindValueAction(param, message);
	}
	
	@Override
	public Object effectWhenFindValueAction(Object param, String message) {
		return activateEffect((Integer) param, message);
	}

}
