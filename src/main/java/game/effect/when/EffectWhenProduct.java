package game.effect.when;

import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.state.*;

public class EffectWhenProduct extends Effect{

	public EffectWhenProduct(IEffectBehavior iEffectBehavior) {
		super(iEffectBehavior);
	}
	
	@Override
	public Object effect (Object param, StateProduct state){
		return activateEffect((Integer) param);
	}
}
