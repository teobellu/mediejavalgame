package server.game.effectControllers;

import model.Effect;

/**
 * Generic effect, do differents things. This is the only particular effect
 * @author M
 *
 */
public class EffectOverruleObject implements IEffectBehavior{
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;
	
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
	
	/**
	 * Initializes the behavior of the effect
	 * @param ref Effect that possesses this behavior
	 */
	private void initializes(Effect ref){
		this.ref = ref;
		thisAction = ref.getToScan();
	}
	
	private void overruleResource (){
		ref.setToAnalyze(null);
	}
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		String text = "Overrule a specific target, see rules for this effect for more info";
		return text;
	}
	
}