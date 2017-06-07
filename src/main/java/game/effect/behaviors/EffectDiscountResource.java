package game.effect.behaviors;

import java.util.ArrayList;
import java.util.List;

import game.GC;
import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectDiscountResource implements IEffectBehavior{
	
	private Resource discountResource;		//la tassa
	private String specificAction;		//se string = null, sempre, altrimenti solo quando azione
	private List<String> actions;
	
	private Effect ref;			//mi serve per aggiornare toAnalyze (risorse in Effetto)
	private Resource normalGain; 	//ciò che guadagnerei normalmente
	private Resource newGain;
	private String thisAction;
	
	public EffectDiscountResource(Resource discountResource) {
		this.discountResource = discountResource;
	}
	
	public EffectDiscountResource(String specificAction, Resource discountResource) {
		this(discountResource);
		if (specificAction == null) return;
		actions = new ArrayList<>();
		actions.add(specificAction);
	}
	
	public EffectDiscountResource(List<String> multipleAction, Resource discountResource) {
		this(discountResource);
		if (multipleAction == null) return;
		actions = new ArrayList<>();
		actions.addAll(multipleAction);
	}

	@Override
	public void effect(Effect ref) {
		initializes(ref);
		if(discountResource == null) return;
		if(actions != null && !actions.contains(thisAction)) return;
		sufferMalus();
		skipMalus();
		applyTax();
	}
	
	private void initializes(Effect ref){
		this.ref = ref;
		newGain = new Resource();
		normalGain = (Resource) ref.getToAnalyze();
		thisAction = (String) ref.getToScan();
	}
	
	private void sufferMalus() {
		GC.RES_TYPES.stream()
			.filter(type -> normalGain.get(type)>discountResource.get(type) && discountResource.get(type)>0)
			.forEach(type -> newGain.add(type, normalGain.get(type) - discountResource.get(type)));
	}
	
	private void skipMalus() {
		GC.RES_TYPES.stream()
			.filter(type -> normalGain.get(type)==0 || discountResource.get(type)==0)
			.forEach(type -> newGain.add(type, normalGain.get(type)));
	}
	
	private void applyTax (){
		ref.setToAnalyze(newGain);
	}
	
}