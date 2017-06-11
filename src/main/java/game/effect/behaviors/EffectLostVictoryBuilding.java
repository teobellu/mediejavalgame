package game.effect.behaviors;

import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.*;

public class EffectLostVictoryBuilding implements IEffectBehavior{

	private Resource payForEach;		//paga 1 victory per ogni forEach
	private Resource costOfFilteredCards;
	private Resource malus;			//quanto pagare effettivamente
	private int countVictoryTax;	//contatore punti da pagare
	private Resource playerRes;		//risorse possedute dal giocatore
	private Player player;
	
	public EffectLostVictoryBuilding(Resource payForEach) {
		this.payForEach = payForEach;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		findCostOfAllFilteredCards();
		establishTax();
		payTax();
	}

	private void initializes(Effect ref){
		countVictoryTax = 0;
		costOfFilteredCards = new Resource();
		player = ref.getPlayer();
		playerRes = player.getResource();
		malus = new Resource();
	}
	
	private void findCostOfAllFilteredCards() {
		player.getDevelopmentCards(GC.DEV_BUILDING).stream()
		.forEach(card -> costOfFilteredCards.add(card.getCost()));
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
		try {
			player.pay(malus);
		} catch (GameException e){
			;
		}
	}
}