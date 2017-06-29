package game.effect.behaviors;

import java.rmi.RemoteException;

import game.GC;
import game.Player;
import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectRecieveRewardForEach implements IEffectBehavior{

	private Effect ref;
	private Resource reward;
	private String card;
	private Resource loot;
	
	private Player player;
	
	private int count;
	
	private EffectRecieveRewardForEach(Resource reward) {
		count = 0;
		this.reward = reward;
		loot = new Resource();
	}
	
	public EffectRecieveRewardForEach(Resource reward, String forEach) {
		this(reward);
		this.card = forEach;
	}
	
	public EffectRecieveRewardForEach(Resource reward, Resource forEach) {
		this(reward);
		this.loot = forEach;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		establishReward();
		try {
			addReward();
		} catch (RemoteException e) {
			//TODO
		}
	}
	
	/**
	 * Initializes the behavior of the effect
	 * @param ref Effect that possesses this behavior
	 */
	private void initializes(Effect ref){
		this.ref = ref;
		player = ref.getPlayer();
	}
	
	private void establishReward(){
		GC.DEV_TYPES.stream()
			.filter(type -> type == card)
			.forEach(type -> count += player.getDevelopmentCards(type).size());
		GC.RES_TYPES.stream()
			.filter(type -> player.getResource(type) > 0 && loot.get(type) > 0)
			.forEach(type -> count += player.getResource(type) / loot.get(type));
	}
	
	private void addReward() throws RemoteException{
		for (int i = 0; i < count; i++)
			ref.getBar().gain(ref, reward); //TODO  giusta la dynamic bar?
	}
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		String text = "Recieve " + reward.toString();
		text += "for each ";
		if (card != null)
			text += card;
		else
			text += loot.toString();
		return text;
	}

}
