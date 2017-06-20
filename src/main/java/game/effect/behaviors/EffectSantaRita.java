package game.effect.behaviors;

import java.io.Serializable;

import game.GC;
import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectSantaRita implements IEffectBehavior{
	
	private Resource resource;		//la tassa
	
	private Effect ref;			//mi serve per aggiornare toAnalyze (risorse in Effetto)
	private Resource normalGain; 	//cio' che guadagnerei normalmente
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
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		String text = "Each time you recieve ";
		for (String type : GC.RES_TYPES)
			if (resource.get(type) > 0)
				text += resource.get(type) + " " + type + " ";
		text += "from instant effect of a development card, you recieve the bonus twice";
		return text;
	}
}