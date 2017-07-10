package server.game.effectControllers;

import model.Effect;

/**
 * This effect behavior does not do anything, is used to avoid null IEffectBehavior while parsing
 */
public class EffectDoNothing implements IEffectBehavior{
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Do nothing because is used to avoid null IEffectBehavior
	 */
	@Override
	public void effect(Effect ref) {
		// Do nothing because is used to avoid null IEffectBehavior
	}
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		return "Do nothing";
	}
	
}
