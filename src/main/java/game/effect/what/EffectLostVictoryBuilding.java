package game.effect.what;

import java.util.List;

import javax.xml.ws.RespectBinding;

import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.state.*;
import game.GameConstants.*;
import game.*;

public class EffectLostVictoryBuilding implements IEffectBehavior{

	private Resource payForEach;		//paga 1 victory per ogni forEach
	private Resource costOfFilteredCards;
	private Resource malus;			//quanto pagare effettivamente
	private int countVictoryTax;	//contatore punti da pagare
	private Resource playerRes;		//risorse possedute dal giocatore
	private Player player;
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		findCostOfAllFilteredCards();
		establishTax();
		payTax();
	}

	public void initializes(Effect ref){
		countVictoryTax = 0;
		costOfFilteredCards = new Resource();
		player = ref.getPlayer();
		playerRes = player.getResource();
		malus = new Resource();
		payForEach = (Resource) ref.getParameters();
	}
	
	private void findCostOfAllFilteredCards() {
		player.getDevelopmentCards(GameConstants.DEV_BUILDING).stream()
		.forEach(card -> costOfFilteredCards.add(card.getCost()));
	}
	
	public void establishTax() {
		countVictoryTax = GameConstants.RES_TYPES.stream()
			.filter(type -> costOfFilteredCards.get(type)>=payForEach.get(type) && payForEach.get(type)>0)
			.map(type -> costOfFilteredCards.get(type) / payForEach.get(type))
			.reduce(0 , (sum, type) -> sum + type);
	}
	
	public void payTax() {
		int playerVictory = playerRes.get(GameConstants.RES_VICTORYPOINTS);
		countVictoryTax = Math.min(countVictoryTax, playerVictory);
		malus.add(GameConstants.RES_VICTORYPOINTS, countVictoryTax);
		try {
			player.pay(malus);
		} catch (GameException e) {
			// Non entrerò mai qui dentro
			e.printStackTrace();
		}
	}
}