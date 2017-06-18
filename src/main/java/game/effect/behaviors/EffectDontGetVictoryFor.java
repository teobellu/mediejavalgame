package game.effect.behaviors;

import java.io.Serializable;

import game.Player;
import game.effect.*;

public class EffectDontGetVictoryFor implements IEffectBehavior, Serializable{

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

	private void initializes(Effect ref){
		player = ref.getPlayer();
	}
	
	private void freeCards() {
		player.freeDevelopmentCards(typeOfCard);
	}
}