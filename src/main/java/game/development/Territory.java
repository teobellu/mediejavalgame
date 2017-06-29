package game.development;

import game.GC;
import game.effect.Effect;

/**
 * This class is designed to contain all information about territory cards
 * 
 * @author M
 *
 */
public class Territory extends DevelopmentCard{
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Base constructor of a territory card
	 * @param name Age of the card
	 * @param age Name of the card
	 * @param immediate Immediate effect of the card
	 * @param permanent Permanent effect of the card
	 * @param dice The value of the dice required for the harvest
	 */
	public Territory(String name, int age, Effect immediate, Effect permanent, int dice) {
		super();
		this.name = name;
		this.age = age;
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
		return GC.DEV_TERRITORY;
	}
	
}
