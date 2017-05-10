package model;

import model.Resource.type;

public class EffectGetResource extends EffectDecorator{

	private Resource toAdd;
	
	public EffectGetResource (Effect eff, Resource res){
		this.component = eff;
		this.player = this.component.player;
		toAdd = new Resource();
		toAdd = res;
	}
	
	@Override
	public void effect() {
		component.effect();
		player.Gain(toAdd);
	}

}
