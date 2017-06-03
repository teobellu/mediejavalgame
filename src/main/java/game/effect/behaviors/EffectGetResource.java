package game.effect.behaviors;

import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectGetResource implements IEffectBehavior{

	private Effect ref;			//mi serve per aggiornare toAnalyze (risorse in Effetto)
	private Resource bonus; 	//ciò che guadagnerei normalmente

	public EffectGetResource(Resource bonus) {
		this.bonus = new Resource(); //si può togliere? TODO
		this.bonus = bonus;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		addResource();
	}

	private void initializes(Effect ref){
		this.ref = ref;
	}

	private void addResource() {
		ref.getPlayer().gain(bonus);
	}
}
