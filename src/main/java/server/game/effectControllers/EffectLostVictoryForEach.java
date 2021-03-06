package server.game.effectControllers;

import model.Effect;
import model.GC;
import model.Player;
import model.Resource;

/**
 * Tiles of age 6 malus
 * @author Matteo
 * @author Jacopo
 *
 */
public class EffectLostVictoryForEach implements IEffectBehavior{
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;

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
	
	/**
	 * Initializes the behavior of the effect
	 * @param ref Effect that possesses this behavior
	 */
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
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		String text = "Pay 1 victory point for each ";
		for (String type : GC.RES_TYPES)
			if (payForEach.get(type) > 0)
				text += payForEach.get(type) + " " + type + " ";
		text += "you have";
		return text;
	}
}
