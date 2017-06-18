package game.effect.behaviors;

import java.io.Serializable;

import game.GC;
import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectSantaRita implements IEffectBehavior, Serializable{
	
	private Resource resource;		//la tassa
	
	private Effect ref;			//mi serve per aggiornare toAnalyze (risorse in Effetto)
	private Resource normalGain; 	//ciò che guadagnerei normalmente
	private Resource newGain;
	private String source;
	
	public EffectSantaRita(Resource resource) {
		this.resource = resource;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		if(resource == null) 
			return;
		if(!GC.DEV_TYPES.contains(source)) 
			return;
		findNextGain();
		applyTax();
	}
	
	private void initializes(Effect ref){
		this.ref = ref;
		newGain = new Resource();
		normalGain = (Resource) ref.getToAnalyze();
		source = ref.getToScan();
	}
	
	public void findNextGain(){
		if(GC.DEV_TYPES.contains(source))
			GC.RES_TYPES.stream()
				.filter(type -> resource.get(type) > 0 && normalGain.get(type) > 0)
				.forEach(type -> newGain.add(type, normalGain.get(type)));
	}
	
	private void applyTax (){
		ref.getPlayer().gain(newGain);
	}
	
}