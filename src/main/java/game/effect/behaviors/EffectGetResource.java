package game.effect.behaviors;

import java.io.Serializable;

import game.GC;
import game.Player;
import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectGetResource implements IEffectBehavior{

	private Effect ref;			//mi serve per aggiornare toAnalyze (risorse in Effetto)
	private Resource bonus; 	//ciò che guadagnerei normalmente

	public EffectGetResource(Resource bonus) {
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
		ref.getBar().gain(ref,bonus);
	}
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		String text = "Get resource: ";
		for (String type : GC.RES_TYPES){
			if (bonus.get(type) > 0)
				text += bonus.get(type) + " " + type + " ";
		}
		if(text == "Get resource: ")
			return "Nothing";
		return text;
	}
}
