package server.game.effectControllers;

import model.Effect;

/**
 * Malus for servants (exc. tile 6 age 2) 
 * @author M
 *
 */
public class EffectPayMoreForIncreaseWorker implements IEffectBehavior{
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;
	
	private Effect ref;			
	private Integer normalPay; 	
	private Integer malus;		
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
	
	/**
	 * Initializes the behavior of the effect
	 * @param ref Effect that possesses this behavior
	 */
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
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		String text = "Insted of pay 1 servant for increase familiar's power you will pay ";
		text += malus;
		return text;
	}
	
}