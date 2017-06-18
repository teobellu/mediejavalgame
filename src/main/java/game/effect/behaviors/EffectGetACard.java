package game.effect.behaviors;

import java.io.Serializable;

import exceptions.GameException;

import game.GC;
import game.Player;
import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectGetACard implements IEffectBehavior, Serializable{

	private Player player;			
	private String typeOfCard;
	private Resource discount;
	private Integer value;
	private Effect effect;

	public EffectGetACard(Integer value) {
		this.value = value;
	}
	
	public EffectGetACard(String typeOfCard, Integer value) {
		this.typeOfCard = typeOfCard;
		this.value = value;
	}
	
	public EffectGetACard(String typeOfCard, Integer value, Resource discount) {
		this(typeOfCard, value);
		this.discount = discount;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		try {
			selectCard();
			getCard();
		} catch (GameException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	private void initializes(Effect ref){
		player = ref.getPlayer();
		effect = ref;
	}

	private void selectCard() {
		int row = 0, column = 0;
		//player.getClient().getConnectionHandler().sendToClient("gimme a numbah");
		
	}
	
	private void getCard() throws GameException {
		Effect eff = new Effect(GC.WHEN_FIND_COST_CARD, new EffectDiscountResource(typeOfCard, discount));
		int index = player.getEffects().size();
		player.addEffect(eff);
		effect.getBar().visitTower(value, 0, 0);
		player.getEffects().remove(index);
	}
}
