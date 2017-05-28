package game.effect.what;

import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectPayMoreForIncreaseWorker implements IEffectBehavior{
	
	private Effect ref;			//mi serve per aggiornare toAnalyze (integer in Effetto)
	private Integer normalPay; 	//ciò che guadagnerei normalmente
	private Integer malus;		//la tassa
	private Integer newPay;
	
	
	public void effect(Effect ref) {
		initializes(ref);
		sufferMalus();
		applyTax();
	}
	
	public void initializes(Effect ref){
		this.ref = ref;
		normalPay = new Integer(0);
		malus = new Integer(0);
		newPay = new Integer(0);
		normalPay = (Integer) ref.getToAnalyze();
		malus = (Integer) ref.getParameters();
	}
	
	private void sufferMalus() {
		newPay = normalPay * malus;
	}
	
	public void applyTax (){
		ref.setToAnalyze(newPay);
	}
	
}