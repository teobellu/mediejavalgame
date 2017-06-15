package game.effect.behaviors;

import game.effect.Effect;
import game.effect.IEffectBehavior;

/**
 * This effect behavior does not do anything, is used to avoid null IEffectBehavior while parsing
 */
public class EffectDoNothing implements IEffectBehavior{

	/**
	 * Do nothing because is used to avoid null IEffectBehavior
	 */
	@Override
	public void effect(Effect ref) {
		// Do nothing because is used to avoid null IEffectBehavior
	}
	
}
