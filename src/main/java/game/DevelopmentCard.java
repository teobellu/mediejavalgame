package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import game.effect.Effect;

public abstract class DevelopmentCard implements ICard{
	
	private String name;
	
	private int age;
	private List<Effect> immediateEffect;
	private List<Effect> permanentEffect;
	
	private List<Resource> cost;
	private List<Resource> requirement;
	
	private int dice;

	public abstract void accept(DevelopmentCardVisitor visitor);
	public abstract String toString();
	
	public DevelopmentCard (){
		immediateEffect = new ArrayList<>();
		permanentEffect = new ArrayList<>();
		cost = new ArrayList<>();
		//effect.setSource(this.toString());
		requirement = new ArrayList<>();
		requirement.add(null);
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

	public void activateImmediateEffect() {
		for (Effect x : immediateEffect){
//			x.effect();
		}
	}
	
	public void activatePermanentEffect() {
		for (Effect x : permanentEffect){
//			x.effect();
		}
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

	public void setPlayer(Player player) {
		for (Effect x : immediateEffect){
			x.setPlayer(player);
		}
		for (Effect x : permanentEffect){
			x.setPlayer(player);
		}
	}
	
	public DevelopmentCard getTerritory(){
		return null;
	}
	
	public DevelopmentCard getBuilding(){
		return null;
	}
	
	public DevelopmentCard getCharacter(){
		return null;
	}
	
	public DevelopmentCard getVenture(){
		return null;
	}
	
	public DevelopmentCard getCard(){
		return this;
	}
	
	
	
	/*
	
	public DevelopmentCard(cardType type, int age, Resource cost, Resource instantBenefit){
		this.type = type;
		this.age = age;
		this.cost = cost;
		this.instantBenefit = instantBenefit;
		if (type == cardType.TERRITORY) return;
	}
	
	public Resource getCost(){
		return cost;
	}
	
	public Resource getInstantBenefit(){
		return instantBenefit;
	}*/
}