package game.effect.behaviors;

import game.GC;
import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectDiscountResource implements IEffectBehavior{
	
	private Resource resMalus;		//la tassa
	private String specificAction;		//se string = null, sempre, altrimenti solo quando azione
	
	private Effect ref;			//mi serve per aggiornare toAnalyze (risorse in Effetto)
	private Resource normalGain; 	//ciò che guadagnerei normalmente
	private Resource newGain;
	
	public EffectDiscountResource(Resource malusResource) {
		resMalus = malusResource;
		//this.specificAction = specificAction;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		if(resMalus == null) return;
		sufferMalus();
		skipMalus();
		applyTax();
	}
	
	private void initializes(Effect ref){
		this.ref = ref;
		normalGain = new Resource();
		newGain = new Resource();
		normalGain = (Resource) ref.getToAnalyze();
	}
	
	private void sufferMalus() {
		GC.RES_TYPES.stream()
		.filter(type -> normalGain.get(type)>resMalus.get(type) && resMalus.get(type)>0)
		.forEach(type -> newGain.add(type, normalGain.get(type) - resMalus.get(type)));
	}
	
	private void skipMalus() {
		GC.RES_TYPES.stream()
		.filter(type -> normalGain.get(type)==0 || resMalus.get(type)==0)
		.forEach(type -> newGain.add(type, normalGain.get(type)));
	}
	
	private void applyTax (){
		ref.setToAnalyze(newGain);
	}
	
}