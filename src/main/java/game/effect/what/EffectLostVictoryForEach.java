package game.effect.what;

import javax.xml.ws.RespectBinding;

import game.GameConstants;
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
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		establishTax();
		payTax();
	}
	
	public void initializes(Effect ref){
		countVictoryTax = 0;
		player = ref.getPlayer();
		playerRes = player.getResource();
		malus = new Resource();
		payForEach = (Resource) ref.getParameters();
	}

	public void establishTax() {
		countVictoryTax = GameConstants.RES_TYPES.stream()
			.filter(type -> playerRes.get(type)>=payForEach.get(type) && payForEach.get(type)>0)
			.map(type -> playerRes.get(type) / payForEach.get(type))
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
