package game.effect.behaviors;

import java.io.Serializable;

import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

//TODO DA RIFARE!		
public class EffectMultiplyResource implements IEffectBehavior{
	/*
	private String specificAction;		//se string = null, sempre, altrimenti solo quando azione
	private Integer multiplier;
	
	
	private Effect ref;			//mi serve per aggiornare toAnalyze (risorse in Effetto)
	private Resource normalGain; 	//ciò che guadagnerei normalmente
	private Resource newGain;
	private String thisAction;
	
	public EffectMultiplyResource(Integer multiplier) {
		this.multiplier = multiplier;
	}
	
	public EffectMultiplyResource(String specificAction, Integer multiplier) {
		this(multiplier);
		this.specificAction = specificAction;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		if(specificAction != null && specificAction != thisAction) return;
		doubleResource();
		apply();
	}
	
	private void initializes(Effect ref){
		this.ref = ref;
		newGain = new Resource();
		normalGain = (Resource) ref.getToAnalyze();
		thisAction = (String) ref.getToScan();
	}
	
	private void doubleResource(){
		for (int i = 0; i < multiplier; i++)
			newGain.add(normalGain);
	}
	
	private void apply (){
		ref.setToAnalyze(newGain);
	}
	*/
}