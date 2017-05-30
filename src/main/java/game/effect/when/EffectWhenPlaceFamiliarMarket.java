package game.effect.when;

import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.state.StateEndingGame;

public class EffectWhenPlaceFamiliarMarket extends Effect{

	public EffectWhenPlaceFamiliarMarket(IEffectBehavior iEffectBehavior) {
		super(iEffectBehavior);
	}
	
	@Override
	public void effect (Effect when){
		when.effectWhenPlaceFamiliarMarket();
	}
	
	@Override
	public void effectWhenPlaceFamiliarMarket() {
		activateEffect();
	}

}
