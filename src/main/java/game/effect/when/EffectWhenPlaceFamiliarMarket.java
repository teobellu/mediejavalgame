package game.effect.when;

import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.state.StateEndingGame;

public class EffectWhenPlaceFamiliarMarket extends Effect{

	public EffectWhenPlaceFamiliarMarket(IEffectBehavior iEffectBehavior) {
		super(iEffectBehavior);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void effect (StateEndingGame state){
		this.activateEffect();
	}

}
