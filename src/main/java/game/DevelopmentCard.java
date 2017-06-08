package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import game.effect.Effect;

public abstract class DevelopmentCard implements ICard{

	protected String name;
	
	protected int age;
	protected List<Effect> immediateEffect;
	protected List<Effect> permanentEffect;
	
	protected List<Resource> cost;
	protected List<Resource> requirement;
	
	protected int dice;

	public abstract void accept(DevelopmentCardVisitor visitor);
	public abstract String toString();
	
	public DevelopmentCard (){
		immediateEffect = new ArrayList<>();
		permanentEffect = new ArrayList<>();
		cost = new ArrayList<>();
		requirement = new ArrayList<>();
		cost.add(new Resource());
		requirement.add(null);
	}
	
	protected void configureEffect(Effect effect){
		if (effect != null)
			effect.setSource(toString());
	}
	
	protected void configureEffect(List<Effect> effects){
		if (effects != null)
			effects.forEach(effect -> configureEffect(effect));
	}

	public int getDice() {
		return dice;
	}

	public void setDice(int dice) {
		this.dice = dice;
	}
	
	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}
	
	public void addImmediateEffect(Effect effect) {
		immediateEffect.add(effect);
	}
	
	public void addPermanentEffect(Effect effect) {
		permanentEffect.add(effect);
	}
	
	public Resource getCost(int index) {
		return cost.get(index);
	}

	public Resource getCost() {
		return cost.get(0);
	}
	
	//for test ONLY
	public void setCost(Resource res) {
		cost.add(res);
	}
	//for test only
	public void setRequirement(Resource res) {
		requirement.set(0, res);
	}
	
	public Resource getRequirement(int index) {
		return requirement.get(index);
	}

	public Resource getRequirement() {
		return requirement.get(0);
	}
	
	public List<Effect> getImmediateEffect() {
		configureEffect(immediateEffect);
		return immediateEffect;
	}
	
	public List<Effect> getPermanentEffect() {
		configureEffect(permanentEffect);
		return permanentEffect;
	}
	
	/*
	
	
	
	public Resource getCost(){
		return cost;
	}
	
	public Resource getInstantBenefit(){
		return instantBenefit;
	}*/
}