package model;

import java.util.HashMap;

import model.Constants.cardType;

public class DevelopmentCard implements Card{
	
	public enum effect { NONE, COUNCIL, PRODUZIONE, RACCOLTA, GREEN, BLUE, YELLOW, PURPLE, MULTICOLOR,
		FOREACHGREEN, FOREACHBLUE, FOREACHYELLOW, FOREACHPURPLE, CHOOSE }
	
	private final cardType type;
	private final int age;
	private final Resource cost;
	private final Resource instantBenefit;
	private HashMap<effect,Integer> instantMap;
	private HashMap<effect,Integer> longtermMap;
	
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
	}
}