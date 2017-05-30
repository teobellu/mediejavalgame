package game.effect.what;

import game.GameConstants;
import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectSufferRake implements IEffectBehavior{
	
	private Effect ref;			//mi serve per aggiornare toAnalyze (risorse in Effetto)
	private Resource normalGain; 	//ciò che guadagnerei normalmente
	private Resource resMalus;		//la tassa
	private Resource newGain;
	
	
	public void effect(Effect ref) {
		initializes(ref);
		if(resMalus == null) return;
		sufferMalus();
		skipMalus();
		applyTax();
	}
	
	public void initializes(Effect ref){
		this.ref = ref;
		normalGain = new Resource();
		newGain = new Resource();
		normalGain = (Resource) ref.getToAnalyze();
		resMalus = (Resource) ref.getParameters();
	}
	
	private void sufferMalus() {
		GameConstants.RES_TYPES.stream()
		.filter(type -> normalGain.get(type)>resMalus.get(type) && resMalus.get(type)>0)
		.forEach(type -> newGain.add(type, normalGain.get(type) - resMalus.get(type)));
	}
	
	private void skipMalus() {
		GameConstants.RES_TYPES.stream()
		.filter(type -> normalGain.get(type)==0 || resMalus.get(type)==0)
		.forEach(type -> newGain.add(type, normalGain.get(type)));
	}
	
	public void applyTax (){
		ref.setToAnalyze(newGain);
	}
	
}