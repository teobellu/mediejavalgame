package game.effect.behaviors;

import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectPayMoreForIncreaseWorker implements IEffectBehavior{
	
	private Effect ref;			//mi serve per aggiornare toAnalyze (integer in Effetto)
	private Integer normalPay; 	//ciò che guadagnerei normalmente
	private Integer malus;		//la tassa
	private Integer newPay;
	
	public EffectPayMoreForIncreaseWorker(Integer malus) {
		this.malus = malus;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		sufferMalus();
		applyTax();
	}
	
	private void initializes(Effect ref){
		this.ref = ref;
		normalPay = new Integer(0);
		newPay = new Integer(0);
		normalPay = (Integer) ref.getToAnalyze();
		
	}
	
	private void sufferMalus() {
		newPay = normalPay * malus;
	}
	
	private void applyTax (){
		ref.setToAnalyze(newPay);
	}
	
}