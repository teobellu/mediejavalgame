package model;

import java.util.List;

/**
 * This class is designed to contain all information about venture cards
 * 
 * @author M
 *
 */
public class Venture extends DevelopmentCard{
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Victory points that the card gives you at the end of the game
	 */
	private int victoryReward;

	/**
	 * Base constructor of a venture card
	 * @param age Age of the card
	 * @param name Name of the card
	 * @param requires Requirements to activate the card
	 * @param cost Costs to activate the card
	 * @param immediate Immediate effects of the card
	 * @param reward Victory points that the card gives you at the end of the game
	 */
	public Venture(int age, String name, List<Resource> requires, List<Resource> cost, List<Effect> immediate, int reward) {
		super();
		this.age = age;
		this.name = name;
		super.setRequirement(requires);
		setCost(cost);
		addImmediateEffect(immediate);
		victoryReward = reward;
	}

	@Override
	public void accept(DevelopmentCardVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		return GC.DEV_VENTURE;
	}
	
	/**
	 * Getter: Get victory points that the card gives you at the end of the game
	 * @return
	 */
	public int getVictoryReward() {
		return victoryReward;
	}
	
}
