package game.effect;

/**
 * @Strategy_Design_Pattern 
 * Interface that all different effects behaviors must implement
 */
public interface IEffectBehavior {
	
	/**
	 * Method that describes the behavior of the effect
	 * @param effect Overall effect with all parameters
	 */
	public void effect (Effect effect);
}
