package game.effect.behaviors;

import java.rmi.RemoteException;

import exceptions.GameException;

import game.GC;
import game.Player;
import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectGetACard implements IEffectBehavior{

	private static final String MESSAGE = "Do you want to get a card? Maybe you have to pay the tax tower!";
	
	private Player player;			
	private String typeOfCard;
	private Resource discount;
	private Integer value;
	private Effect effect;
	
	private int row;
	private int column;
	private boolean activate = true;
	
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
		do{
			 //TODO REMOTE EXCEPTION
			if (!wantToPickCard())
				return;
			try {
				selectCard();
				getCard();
			} catch (GameException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}while(true);
		
	}

	private void initializes(Effect ref){
		player = ref.getPlayer();
		effect = ref;
	}
	
	private boolean wantToPickCard() throws RemoteException{
		return player.getClient().getConnectionHandler().ask(MESSAGE);
	}
	
	private void selectCard() {
		if (GC.DEV_TYPES.contains(typeOfCard))
			column = GC.DEV_TYPES.indexOf(typeOfCard);
		else{
			//TODO ottieni colonna
			//column = player.getClient().getConnectionHandler()
		}
		//TODO ottieni riga
		
	}
	
	private void getCard() throws GameException {
		Effect eff = new Effect(GC.WHEN_FIND_COST_CARD, new EffectDiscountResource(typeOfCard, discount));
		int index = player.getEffects().size();
		player.addEffect(eff);
		effect.getBar().visitTower(value, row, column);
		player.getEffects().remove(index);
	}
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		String text = "Get a ";
		if (typeOfCard != null)
			text += typeOfCard + " ";
		text += "card from towers with an action of power " + value;
		if (discount == null)
			return text;
		text += " and a get discout equals to ";
		for (String type : GC.RES_TYPES){
			if (discount.get(type) > 0)
				text += discount.get(type) + " " + type + " ";
		}
		return text;
	}
}
