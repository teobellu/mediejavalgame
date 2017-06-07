package game.effect.behaviors;

import game.GC;
import game.Player;
import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectRecieveRewardForEach implements IEffectBehavior{

	private Effect ref;
	private Resource reward;
	private String forEach;
	
	private Player player;
	
	private int count = 0;
	
	public EffectRecieveRewardForEach(Resource reward, String forEach) {
		this.reward = reward;
		this.forEach = forEach;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		establishReward();
		addReward();
	}
	
	private void initializes(Effect ref){
		this.ref = ref;
		player = ref.getPlayer();
	}
	
	private void establishReward(){
		for (String type : GC.DEV_TYPES){
			if (type == forEach){
				count += player.getDevelopmentCards(type).size();
			}
		}
		for (String type : GC.RES_TYPES){
			if (type == forEach){
				count += player.getResource().get(type);
			}
		}
		
		/**
		 * TODO DELETE OR CONVERT
		 */
		GC.DEV_TYPES.stream()
			.filter(type -> type == forEach);
			
	}
	
	private void addReward(){
		for (int i = 0; i < count; i++)
			player.getDynamicBar().gain(ref, reward); //TODO è giusta la getDynamicBar?
	}

}
