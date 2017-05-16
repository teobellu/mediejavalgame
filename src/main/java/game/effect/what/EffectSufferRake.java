package game.effect.what;

import game.Resource;
import game.Resource.type;
import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.state.*;

public class EffectSufferRake implements IEffectBehavior{
	
	private Effect ref;			//mi serve per aggiornare toAnalyze (risorse in Effetto)
	private Resource toGain; 	//ciò che guadagnerei normalmente
	private Resource toPay;		//la tassa
	private Resource toNewGain;
	
	
	public void effect(Effect ref) {
		initializes(ref);
		establishTax();
		applyTax();
	}
	
	public void initializes(Effect ref){
		toGain = new Resource();
		this.ref = ref;
		this.toGain = (Resource) ref.getToAnalyze();
		this.toPay = (Resource) ref.getParameters();
		this.toNewGain = new Resource();
	}
	
	public void establishTax (){
		int newGain;
		for (type i : type.values())
			if (toGain.get(i)>0 && toPay.get(i)>0){
				newGain = toGain.get(i) - toPay.get(i);
				toNewGain.add(i, Math.abs(newGain));
			}
			else
				toNewGain.add(i, toGain.get(i));
	}
	
	public void applyTax (){
		ref.setToAnalyze(toNewGain);
	}
	
}