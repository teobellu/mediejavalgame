package game;

import game.effect.Effect;

public class LeaderCard implements ICard{
	private String name;
	private Resource requirement;
	private int requirementTerritory;
	private int requirementCharacter;
	private int requirementBuilding;
	private int requirementVenture;
	private Effect effect;
	private boolean isPermanent;

	public LeaderCard(String name, Resource requirement, int rT, int rC, int rB, int rV, Effect effect, boolean isPermanent) {
		this.name = name;
		this.requirement = requirement;
		this.effect = effect;
		this.isPermanent = isPermanent;
		requirementTerritory = rT;
		requirementCharacter = rC;
		requirementBuilding = rB;
		requirementVenture = rV;
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

	public int getRequirementCharacter() {
		return requirementCharacter;
	}

	public int getRequirementBuilding() {
		return requirementBuilding;
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
