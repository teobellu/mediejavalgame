package model;

import model.Resource.type;

public class EffectVictoryForEach extends EffectDecorator{

	private Resource toAdd;
	private int count;
	
	public EffectVictoryForEach (Effect eff, Resource res){
		this.component = eff;
		this.player = eff.player;
		toAdd = new Resource();
	}
	
	@Override
	public void effect() {
		component.effect();
		count = 3; //supponiamo che il player abbia 3 carte sviluppo TODO TODO TODO TODO TODO TODO
		toAdd.add(type.VICTORYPOINTS, count);
		player.Gain(toAdd);
	}

}
