package game.development;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import game.ICard;
import game.Resource;
import game.effect.Effect;

public abstract class DevelopmentCard implements ICard, Serializable{

	private int id;
	
	protected String name;
	
	protected int age;
	private List<Effect> immediateEffect;
	private List<Effect> permanentEffect;
	
	private List<Resource> cost;
	private List<Resource> requirement;
	
	protected int dice;
	
	public DevelopmentCard (){
		immediateEffect = new ArrayList<>();
		permanentEffect = new ArrayList<>();
		cost = new ArrayList<>();
		requirement = new ArrayList<>();
		cost.add(new Resource());
		requirement.add(null);
	}
	
	public abstract void accept(DevelopmentCardVisitor visitor);
	public abstract String toString();
	
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
	
	protected void addImmediateEffect(Effect effect) {
		immediateEffect.add(effect);
	}
	
	protected void addPermanentEffect(Effect effect) {
		permanentEffect.add(effect);
	}
	
	public Resource getCost(int index) {
		return cost.get(index);
	}

	public List<Resource> getCost() {
		return cost;
	}
	
	protected void setCost(Resource res) {
		cost.add(res);
	}
	
	protected void setRequirement(Resource res) {
		requirement.set(0, res);
	}
	
	public Resource getRequirement(int index) {
		return requirement.get(index);
	}

	public List<Resource> getRequirement() {
		return requirement;
	}
	
	public List<Effect> getImmediateEffect() {
		configureEffect(immediateEffect);
		return immediateEffect;
	}
	
	public List<Effect> getPermanentEffect() {
		configureEffect(permanentEffect);
		return permanentEffect;
	}
	
	protected void addImmediateEffect(List<Effect> immediate) {
		immediateEffect.addAll(immediate);
	}
	
	protected void addPermanentEffect(List<Effect> permanent) {
		permanentEffect.addAll(permanent);
	}
	
	protected void setCost(List<Resource> cost) {
		this.cost.addAll(cost);
	}
	
	protected void setRequirement(List<Resource> requires) {
		requirement.addAll(requires);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}