package model;

import model.Resource.type;

public class EffectSufferRake implements IEffectBehavior{
	
	private Effect ref;
	private Resource toGain; 	//ciò che guadagnerei normalmente
	private Resource toPay;		//la tassa
	private Resource toNewGain;
	private Player player;
	
	public void effect(Effect ref){
		this.ref = ref;
		this.toGain = ref.getToAnalyze();
		this.toPay = ref.getResource();
		this.toNewGain = new Resource();
		this.player = ref.getPlayer();
		if (toGain == null) return;
		establishTax();
		applyTax();
	}
	
	public void establishTax (){
		for (type i : type.values())
			if (toGain.get(i)>0 && toPay.get(i)>0){
				if (toGain.get(i) >= toPay.get(i))
					toNewGain.add(i, toGain.get(i) - toPay.get(i));
				else
					toNewGain.add(i, toPay.get(i) - toGain.get(i));
			}
			else
				toNewGain.add(i, toGain.get(i));
	}
	
	public void applyTax (){
		ref.setToAnalyze(toNewGain);
	}
}

