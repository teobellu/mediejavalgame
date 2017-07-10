package model;

import java.util.List;

/**
 * This class is designed to contain all information about character cards
 * 
 * @author M
 *
 */
public class Character extends DevelopmentCard{
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Base constructor of a character card
	 * @param age Age of the card
	 * @param name Name of the card
	 * @param cost Costs to activate the card
	 * @param immediate Immediate effects of the card
	 * @param permanent Permanent effects of the card
	 */
	public Character(int age, String name, Resource cost, List<Effect> immediate, List<Effect> permanent) {
		super();
		this.age = age;
		this.name = name;
		setCost(cost);
		addImmediateEffect(immediate);
		addPermanentEffect(permanent);
	}

	@Override
	public void accept(DevelopmentCardVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		return GC.DEV_CHARACTER;
	}
	
}
