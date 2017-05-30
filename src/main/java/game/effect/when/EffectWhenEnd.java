package game.effect.when;

import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.state.*;

public class EffectWhenEnd extends Effect{

	public EffectWhenEnd(IEffectBehavior iEffectBehavior) {
		super(iEffectBehavior);
	}

	@Override
	public void effect (Effect when){
		when.effectWhenEnd();
	}
	
	@Override
	public void effectWhenEnd(){
		activateEffect();
	}
}

