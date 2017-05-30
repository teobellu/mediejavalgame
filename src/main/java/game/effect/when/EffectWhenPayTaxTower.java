package game.effect.when;

import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.state.*;

public class EffectWhenPayTaxTower extends Effect{

	public EffectWhenPayTaxTower(IEffectBehavior iEffectBehavior) {
		super(iEffectBehavior);
	}
	
	@Override
	public Object effect (Object param, Effect when){
		return when.effectWhenPayTaxTower(param);
	}
	
	@Override
	public Object effectWhenPayTaxTower(Object param) {
		return activateEffect((Resource) param);
	}
}
