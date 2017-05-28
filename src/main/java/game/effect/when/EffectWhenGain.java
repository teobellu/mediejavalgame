package game.effect.when;

import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.state.*;

public class EffectWhenGain extends Effect{

	public EffectWhenGain(IEffectBehavior iEffectBehavior) {
		super(iEffectBehavior);
	}
	
	@Override
	public Object effect (Object param, StateGaining state){
		return activateEffect((Resource) param);
	}
}
