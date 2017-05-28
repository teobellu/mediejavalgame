package game.effect.what;

import game.Player;
import game.effect.*;

public class EffectDontGetVictoryFor implements IEffectBehavior{

	private String typeOfCard;
	private Player player;
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		freeCards();
	}

	public void initializes(Effect ref){
		player = ref.getPlayer();
		typeOfCard = (String) ref.getParameters();
	}
	
	public void freeCards() {
		player.freeDevelopmentCards(typeOfCard);
	}
}