package game.effect.behaviors;

import game.GC;
import game.GameException;
import game.Player;
import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectConvertResource implements IEffectBehavior{

	private Resource resourceToConvert;		//paga 1 victory per ogni forEach
	private Resource resourceToGain;		//risorse possedute dal giocatore
	private Player player;
	
	public EffectConvertResource(Resource resourceToConvert, Resource resourceToGain) {
		this.resourceToConvert = resourceToConvert;
		this.resourceToGain = resourceToGain;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		askInformation();
		convert();
	}
	
	private void initializes(Effect ref){
		player = ref.getPlayer();
	}

	private void askInformation() {
		//chiede cose al client: tipo, vuoi convertire?
	}
	
	private void convert() {
		try {
			player.pay(resourceToConvert);
			player.gain(resourceToGain);
		} catch (GameException e) {
			// TODO il giocatore non pu� converire perch� non ha le risorse necessarie
		}
		
	}
}