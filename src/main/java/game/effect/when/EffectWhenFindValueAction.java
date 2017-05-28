package game.effect.when;

import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.state.StateActionValue;
import game.state.StateIncreaseWorker;
import game.state.StateJoiningSpace;

public class EffectWhenFindValueAction extends Effect{

	public EffectWhenFindValueAction(IEffectBehavior iEffectBehavior) {
		super(iEffectBehavior);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Object effect (Object param, String message, StateJoiningSpace state){
		return activateEffect((Integer) param, message);
	}
	
	@Override
	public Object effect (Object param, String message, StateActionValue state){
		return activateEffect((Integer) param, message);
	}

}
