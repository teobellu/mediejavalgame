package game;

import game.effect.Effect;

public class LeaderCard implements ICard{
	private String name;
	private Resource requirement;
	private int requirementTerritory;
	private int requirementBuilding;
	private int requirementCharacter;
	private int requirementVenture;
	private Effect effect;
	private boolean isPermanent;
	
	public LeaderCard(UserConfig userconfig){
		//TODO
	}

	public String getName() {
		return name;
	}

	public Resource getRequirement() {
		return requirement;
	}

	public int getRequirementTerritory() {
		return requirementTerritory;
	}

	public int getRequirementBuilding() {
		return requirementBuilding;
	}

	public int getRequirementCharacter() {
		return requirementCharacter;
	}

	public int getRequirementVenture() {
		return requirementVenture;
	}

	public Effect getEffect() {
		return effect;
	}

	public boolean isPermanent() {
		return isPermanent;
	}
	
	
}
