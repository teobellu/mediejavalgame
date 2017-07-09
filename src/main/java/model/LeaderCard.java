package model;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Class used to represent Leader Cards
 * @author M
 *
 */
public class LeaderCard implements ICard, Serializable{
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Name of the Leader
	 */
	private String name;
	
	/**
	 * Effect/Bonus of the Leader
	 */
	private Effect effect;
	
	/**
	 * @Lambda_Function
	 * Requirement, return true if the player can activate the card
	 */
	private transient Function<Player, Boolean> requirement;
	
	/**
	 * Leader Card constructor
	 * @param name Name of Leader
	 * @param effect Effect, once per turn (OPT) or permanent
	 * @param requirement Lambda function, return true if the player can activate the card
	 */
	public LeaderCard(String name, Effect effect, Function<Player, Boolean> requirement) {
		this.name = name;
		this.effect = effect;
		if (effect != null)
			this.effect.setSource(GC.LEADER_CARD);
		if (requirement != null)
			this.requirement = requirement;
		else
			this.requirement = player -> true;
		
	}
	
	/**
	 * @Lambda_Function
	 * Requirement, return true if the player can activate the card
	 * @param player Player who wants to activate the card
	 * @return True, if he can activate this card, false otherwise
	 */
	public boolean canPlayThis(Player player){
		return requirement.apply(player);
	}

	/**
	 * Getter: get the name of Leader card
	 * @return Leader's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter: get the effect of the Leader card
	 * @return Bonus of the card
	 */
	public Effect getEffect() {
		return effect;
	}
	
}
