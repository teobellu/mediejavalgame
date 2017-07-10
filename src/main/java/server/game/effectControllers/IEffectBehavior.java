package server.game.effectControllers;

import java.io.Serializable;

import model.Effect;

/**
 * @Strategy_Design_Pattern 
 * Interface that all different effects behaviors must implement
 */
@FunctionalInterface
public interface IEffectBehavior extends Serializable{
	
	/**
	 * Method that describes the behavior of the effect
	 * @param effect Overall effect with all parameters
	 */
	public void effect (Effect effect);
	
}