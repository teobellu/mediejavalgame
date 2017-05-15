package model;

public class EffectDoHarvest implements IEffectBehavior{
	
	private Player player;
	private Integer power;
	
	@Override
	public void effect(Effect ref){
		initializes(ref);
		doAction();
	}
	
	public void initializes(Effect ref){
		player = ref.getPlayer();
		power = (Integer) ref.getParameters();
	}
	
	public void doAction(){
		player.harvest(power);
	}
}
