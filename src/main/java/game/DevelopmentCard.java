package game;

import java.util.ArrayList;
import java.util.HashMap;

import game.effect.Effect;

public class DevelopmentCard implements ICard{
	
	private String name;
	
	private int age;
	private ArrayList<Effect> immediateEffect;
	private ArrayList<Effect> permanentEffect;
	
	private Resource cost;
	private Resource requirement;
	
	private int dice;

	public DevelopmentCard (){
		immediateEffect = new ArrayList<>();
		permanentEffect = new ArrayList<>();
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

	public Resource getCost() {
		return cost;
	}

	public Resource getRequirement() {
		return requirement;
	}

	public void setPlayer(Player player) {
		for (Effect x : immediateEffect){
			x.setPlayer(player);
		}
		for (Effect x : permanentEffect){
			x.setPlayer(player);
		}
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