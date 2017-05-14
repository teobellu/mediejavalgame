package model;

public class EffectGetResource implements IEffectBehavior{
	
	private Player player;
	private Resource toGain;
	
	public void effect(Effect ref){
		player = ref.getPlayer();
		toGain = ref.getResource();
		player.controlGain(toGain);
	}
}
