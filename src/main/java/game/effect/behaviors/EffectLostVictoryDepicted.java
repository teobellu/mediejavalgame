package game.effect.behaviors;

import game.effect.Effect;
import game.effect.IEffectBehavior;

import java.io.Serializable;

import exceptions.GameException;
import game.*;

public class EffectLostVictoryDepicted implements IEffectBehavior, Serializable{

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

	private void initializes(Effect ref){
		countVictoryTax = 0;
		costOfFilteredCards = new Resource();
		player = ref.getPlayer();
		playerRes = player.getResource();
		malus = new Resource();
	}
	
	private void findCostOfAllFilteredCards() {
		player.getDevelopmentCards(typeOfCard).stream()
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
		
		//Sostituito al try catch sotto. TODO riguardare
		//player.getResource().add(GC.RES_VICTORYPOINTS, -countVictoryTax);
		
		try {
			player.pay(malus);
		} catch (GameException e) {
			
		}
	}
}