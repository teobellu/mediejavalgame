package model;

import java.util.HashMap;

public class DevelopmentCard implements Card{
	
	public enum colors { GREEN, BLUE, YELLOW, PURPLE }
	
	public enum effect { NONE, COUNCIL, PRODUZIONE, RACCOLTA, GREEN, BLUE, YELLOW, PURPLE, MULTICOLOR,
		FOREACHGREEN, FOREACHBLUE, FOREACHYELLOW, FOREACHPURPLE, CHOOSE }
	
	private final colors color;
	private final int age;
	private final Resource cost;
	private final Resource instantBenefit;
	private HashMap<effect,Integer> instantMap;
	private HashMap<effect,Integer> longtermMap;
	
	public DevelopmentCard(colors color, int age, Resource cost, Resource instantBenefit){
		this.color = color;
		this.age = age;
		this.cost = cost;
		this.instantBenefit = instantBenefit;
		if (color == colors.GREEN) return;
	}
	
	public Resource getCost(){
		return cost;
	}
	
	public Resource getInstantBenefit(){
		return instantBenefit;
	}
}