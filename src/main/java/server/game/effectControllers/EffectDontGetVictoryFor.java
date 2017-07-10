package server.game.effectControllers;

import model.Effect;
import model.GC;
import model.Player;

/**
 * Malus don't get victory for something
 * @author M
 *
 */
public class EffectDontGetVictoryFor implements IEffectBehavior{
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;

	private String typeOfCard;
	private Player player;
	
	public EffectDontGetVictoryFor(String typeOfCard) {
		this.typeOfCard = typeOfCard;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		freeCards();
	}

	/**
	 * Initializes the behavior of the effect
	 * @param ref Effect that possesses this behavior
	 */
	private void initializes(Effect ref){
		player = ref.getPlayer();
	}
	
	private void freeCards() {
		player.freeDevelopmentCards(typeOfCard);
	}
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		if (!GC.DEV_TYPES.contains(typeOfCard))
			return "Nothing";
		return "Don't get victory points from " + typeOfCard;
	}
}