package game.effect.behaviors;

import game.GC;
import game.GameException;
import game.Player;
import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectGetACard implements IEffectBehavior{

	private Player player;			
	private String typeOfCard;
	private Resource discount;
	private Integer value;

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
			e.printStackTrace();
		}
	}

	

	private void initializes(Effect ref){
		player = ref.getPlayer();
	}

	private void selectCard() {
		int row = 0, column = 0;
		
		//TODO
	}
	
	private void getCard() throws GameException {
		Effect eff = new Effect(GC.WHEN_FIND_COST_CARD, new EffectDiscountResource(typeOfCard, discount));
		int index = player.getEffects().size();
		player.addEffect(eff);
		player.getDynamicBar().placeInTowerStupidBigMethod(value, 0, 0);
		player.getEffects().remove(index);
	}
}
