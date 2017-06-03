package game.effect.behaviors;

import javax.xml.ws.RespectBinding;

import game.GC;
import game.GameException;
import game.Player;
import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.state.*;

public class EffectLostVictoryForEach implements IEffectBehavior{

	private Resource payForEach;		//paga 1 victory per ogni forEach
	private Resource malus;			//quanto pagare effettivamente
	private int countVictoryTax;	//contatore punti da pagare
	private Resource playerRes;		//risorse possedute dal giocatore
	private Player player;
	
	public EffectLostVictoryForEach(Resource payForEach) {
		this.payForEach = payForEach;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		establishTax();
		payTax();
	}
	
	private void initializes(Effect ref){
		countVictoryTax = 0;
		player = ref.getPlayer();
		playerRes = player.getResource();
		malus = new Resource();
	}

	private void establishTax() {
		countVictoryTax = GC.RES_TYPES.stream()
			.filter(type -> playerRes.get(type)>=payForEach.get(type) && payForEach.get(type)>0)
			.map(type -> playerRes.get(type) / payForEach.get(type))
			.reduce(0 , (sum, type) -> sum + type);
	}
	
	private void payTax() {
		int playerVictory = playerRes.get(GC.RES_VICTORYPOINTS);
		countVictoryTax = Math.min(countVictoryTax, playerVictory);
		malus.add(GC.RES_VICTORYPOINTS, countVictoryTax);
		try {
			player.pay(malus);
		} catch (GameException e) {
			// Non entrerò mai qui dentro
			e.printStackTrace();
		}
	}
}
