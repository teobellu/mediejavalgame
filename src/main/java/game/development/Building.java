package game.development;

import game.GC;
import game.Resource;
import game.effect.Effect;

/**
 * This class is designed to contain all information about building cards
 * 
 * @author M
 *
 */
public class Building extends DevelopmentCard{
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Base constructor of a building card
	 * @param age Age of the card
	 * @param name Name of the card
	 * @param cost Cost of the card
	 * @param immediate Immediate effect of the card
	 * @param permanent Permanent effect of the card
	 * @param dice The value of the dice required for the production
	 */
	public Building(int age, String name, Resource cost, Effect immediate, Effect permanent, int dice) {
		super();
		this.age = age;
		this.name = name;
		setCost(cost);
		addImmediateEffect(immediate);
		addPermanentEffect(permanent);
		this.dice = dice;
	}

	@Override
	public void accept(DevelopmentCardVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return GC.DEV_BUILDING;
	}
	
}
