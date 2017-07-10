package server.game.effectControllers;

import java.util.logging.Level;
import java.util.logging.Logger;

import model.Effect;
import model.GC;
import model.Player;
import model.Resource;
import model.exceptions.GameException;

/**
 * Malus for tiles 6 of age 3
 * @author M
 *
 */
public class EffectLostVictoryDepicted implements IEffectBehavior{
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;

	private transient Logger _log = Logger.getLogger(EffectLostVictoryDepicted.class.getName());
	
	private Resource payForEach;		//paga 1 victory per ogni forEach
	private String typeOfCard;
	private Resource costOfFilteredCards;
	private Resource malus;			//quanto pagare effettivamente
	private int countVictoryTax;	//contatore punti da pagare
	private Resource playerRes;		//risorse possedute dal giocatore
	private Player player;
	
	public EffectLostVictoryDepicted(String typeOfCard, Resource payForEach) {
		this.typeOfCard = typeOfCard;
		this.payForEach = payForEach;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		findCostOfAllFilteredCards();
		establishTax();
		payTax();
	}

	/**
	 * Initializes the behavior of the effect
	 * @param ref Effect that possesses this behavior
	 */
	private void initializes(Effect ref){
		countVictoryTax = 0;
		costOfFilteredCards = new Resource();
		player = ref.getPlayer();
		playerRes = player.getResource();
		malus = new Resource();
	}
	
	private void findCostOfAllFilteredCards() {
		player.getDevelopmentCards(typeOfCard).stream()
		.forEach(card -> costOfFilteredCards.add(card.getCost(0))); //TODO 0 is ok?
	}
	
	private void establishTax() {
		countVictoryTax = GC.RES_TYPES.stream()
			.filter(type -> costOfFilteredCards.get(type)>=payForEach.get(type) && payForEach.get(type)>0)
			.map(type -> costOfFilteredCards.get(type) / payForEach.get(type))
			.reduce(0 , (sum, type) -> sum + type);
	}
	
	private void payTax() {
		int playerVictory = playerRes.get(GC.RES_VICTORYPOINTS);
		countVictoryTax = Math.min(countVictoryTax, playerVictory);
		malus.add(GC.RES_VICTORYPOINTS, countVictoryTax);
		
		//Sostituito al try catch sotto. TODO riguardare
		//player.getResource().add(GC.RES_VICTORYPOINTS, -countVictoryTax);
		
		try {
			player.pay(malus);
		} catch (GameException e) {
			_log.log(Level.OFF, e.getMessage(), e);
		}
	}
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		String text = "Pay 1 victory point for each ";
		for (String type : GC.RES_TYPES)
			if (payForEach.get(type) > 0)
				text += payForEach.get(type) + " " + type + " ";
		text += "depicted on your " + typeOfCard + " cards";
		return text;
	}
}