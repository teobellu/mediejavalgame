package game.effect.behaviors;

import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.GameException;
import game.effect.Effect;
import game.effect.IEffectBehavior;

/**
 * This effect behavior allows you to perform a work action
 * 
 * @author M
 *
 */
public class EffectWork implements IEffectBehavior{
	
	private transient Logger _log = Logger.getLogger(EffectWork.class.getName());
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Effect that possesses this behavior
	 */
	private Effect effect;		
	
	/**
	 * Type of action
	 */
	private String action;
	
	/**
	 * Start power of the action
	 */
	private Integer value;

	/**
	 * Base constructor of the work effect
	 * @param action Type of action
	 * @param value Start power of the action
	 */
	public EffectWork(String action, Integer value) {
		this.action = action;
		this.value = value;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		performWork();
	}

	/**
	 * Initializes the behavior of the effect
	 * @param ref Effect that possesses this behavior
	 */
	private void initializes(Effect ref){
		effect = ref;
	}

	/**
	 * Performs a dynamic work action
	 */
	private void performWork() {
		try {
			effect.getBar().launchesWork(value, action);
		} catch (GameException e) {
			// TODO Auto-generated catch block Stai facendo lavoro a potere 0
			_log.log(Level.FINE, e.getMessage(), e);
		}
	}
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		String text = "Do action: " + action;
		text += " with power " + value;
		return text;
	}
}
