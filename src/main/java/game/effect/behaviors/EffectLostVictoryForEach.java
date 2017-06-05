package game.effect.behaviors;

import game.GC;
import game.Player;
import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectLostVictoryForEach implements IEffectBehavior{

	private Resource payForEach;		//paga 1 victory per ogni forEach
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
		player.getResource().add(GC.RES_VICTORYPOINTS, -countVictoryTax);
	}
}
