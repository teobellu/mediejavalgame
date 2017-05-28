package game.effect.when;

import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.state.*;

public class EffectWhenHarvest extends Effect{

	public EffectWhenHarvest(IEffectBehavior iEffectBehavior) {
		super(iEffectBehavior);
	}
	
	@Override
	public Object effect (Object param, StateHarvest state){
		return activateEffect((Integer) param);
	}
}
