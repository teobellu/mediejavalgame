package game.effect.behaviors;

import java.io.Serializable;

import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectOverruleObject implements IEffectBehavior, Serializable{
	
	private String specificAction;		//se string = null, sempre, altrimenti solo quando azione
	
	private Effect ref;			//mi serve per aggiornare toAnalyze (risorse in Effetto)
	private String thisAction;
	
	public EffectOverruleObject() {
		
	}
	
	public EffectOverruleObject(String specificAction) {
		this.specificAction = specificAction;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		if(specificAction != null && specificAction != thisAction) 
			return;
		overruleResource();
	}
	
	private void initializes(Effect ref){
		this.ref = ref;
		thisAction = ref.getToScan();
	}
	
	private void overruleResource (){
		ref.setToAnalyze(null);
	}
	
}