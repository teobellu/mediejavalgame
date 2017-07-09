package game.effect.behaviors;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.GameException;
import game.GC;
import game.GameBoard;
import game.Player;
import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectGetACard implements IEffectBehavior{

	private transient Logger _log = Logger.getLogger(EffectGetACard.class.getName());
	
	private static final String MESSAGE = "Do you want to get a card? Maybe you have to pay the tax tower! The start power is: ";
	private static final String MESS_COLUMN = "Select a column";
	private static final String MESS_ROW = "Select a row";
	
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
		try {
			useEffect();
		} catch (RemoteException e) {
			Logger.getLogger(EffectGetACard.class.getName()).log(Level.WARNING, e.getMessage(), e);
		}
		
	}

	/**
	 * Initializes the behavior of the effect
	 * @param ref Effect that possesses this behavior
	 */
	private void initializes(Effect ref){
		player = ref.getPlayer();
		effect = ref;
	}
	
	private void useEffect() throws RemoteException{
		do{
			if (!wantToPickCard())
				return;
			try {
				selectCard();
				getCard();
				break;
			} catch (GameException e) {
				//TODO non puoi
				_log.log(Level.OFF, e.getMessage(), e);
			}
		}while(true);
	}
	
	private boolean wantToPickCard() throws RemoteException{
		return player.getClient().getConnectionHandler().askBoolean(MESSAGE + value);
	}
	
	private void selectCard() throws RemoteException {
		if (GC.DEV_TYPES.contains(typeOfCard))//TODO CONTAINS FUNZIONA COME EQUALs?
			column = GC.DEV_TYPES.indexOf(typeOfCard);
		else{
			column = player.getClient().getConnectionHandler().askInt(MESS_COLUMN, 0, GameBoard.MAX_COLUMN - 1);
		}
		row = player.getClient().getConnectionHandler().askInt(MESS_ROW, 0, GameBoard.MAX_ROW - 1);
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
		text += discount.toString();
		return text;
	}
}
