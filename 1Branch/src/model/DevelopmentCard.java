package model;

public class DevelopmentCard {
	
	public enum colors { GREEN, BLUE, YELLOW, PURPLE }
	
	public enum effect { NONE, PRODUCTION, COUNCIL }
	
	private colors color;
	private int era;
	private int dice;
	private Resource cost;
	private Resource instantBenefit;
	private effect eff = effect.NONE;
	
	public DevelopmentCard(colors color, int era, int dice, Resource cost, Resource instantBenefit){
		this.color = color;
		this.era = era;
		this.dice = dice;
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
