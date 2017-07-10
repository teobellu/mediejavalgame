package server.game.effectControllers;

import model.Effect;
import model.GC;
import model.Player;

/**
 * Effect delay first action each turn
 * @author Matteo
 * @author Jacopo
 *
 */
public class EffectDelayFirstAction implements IEffectBehavior{

	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;
	
	private Player player;
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		delayTurn();
	}

	/**
	 * Initializes the behavior of the effect
	 * @param ref Effect that possesses this behavior
	 */
	private void initializes(Effect ref){
		player = ref.getPlayer();
	}
	
	private void delayTurn(){
		player.setDelay(GC.DELAY);
	}
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		return "Delay first action";
	}
}
