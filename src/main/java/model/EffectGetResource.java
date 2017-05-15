package model;

public class EffectGetResource implements IEffectBehavior{
	
	private Player player;
	private Resource toGain;
	
	@Override
	public void effect(Effect ref){
		initializes(ref);
		gainBonus();
	}
	
	public void initializes(Effect ref){
		player = ref.getPlayer();
		toGain = (Resource) ref.getParameters();
	}
	
	public void gainBonus(){
		player.controlGain(toGain);
	}
}
