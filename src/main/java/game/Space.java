package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import game.development.DevelopmentCard;
import game.effect.Effect;

/**
 * This class is designed for store all information about an action space
 */
public class Space implements Serializable{
	
	/**
	 * A default serial version ID to the selected type
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Dice value required for place here
	 */
	private int requiredDiceValue;
	
	/**
	 * Instant effect of the space
	 */
	private Effect instantEffect;

	/**
	 * True I can place only 1 familiar here (except Ludovico Ariosto & other stuff)
	 */
	private boolean singleObject;
	
	/**
	 * List of familiars placed here
	 */
	private List<FamilyMember> familiars;
	
	/**
	 * Base constructor of a single space
	 * @param cost Dice value required for place here
	 * @param instantEffect Instant effect of the space
	 * @param singleObject True I can place only 1 familiar here 
	 * (except Ludovico Ariosto & other stuff)
	 */
	public Space(int cost, Effect instantEffect, boolean singleObject) {
		requiredDiceValue = cost;
		if (instantEffect != null)
			instantEffect.setSource(GC.ACTION_SPACE);
		this.instantEffect = instantEffect;
		this.singleObject = singleObject;
		familiars = new ArrayList<>();
	}
	
	/**
	 * Used to place a familiar in the space
	 * @param member Familiar to place
	 */
	public void placeFamiliar(FamilyMember member){
		familiars.add(member);
	}

	/**
	 * Getter: get the dice of the space
	 * @return dice value
	 */
	public int getRequiredDiceValue() {
		return requiredDiceValue;
	}

	/**
	 * Get the familiars in the space
	 * @return familiars placed
	 */
	public List<FamilyMember> getFamiliars() {
		return familiars;
	}

	/**
	 * Getter: used for know if the space is a single object
	 * @return true if the space is a single object
	 */
	public boolean isSingleObject() {
		return singleObject;
	}

	/**
	 * Setter: set if the space is a single object
	 * @param singleObject check
	 */
	public void setSingleObject(boolean singleObject) {
		this.singleObject = singleObject;
	}
	
	/**
	 * Setter: set the card of the space
	 * @param card the card of the space
	 */
	public void setCard(DevelopmentCard card) {
		card.toString();
		return;
	}

	/**
	 * Getter: get the card of the space
	 * @return the card of the space
	 */
	public DevelopmentCard getCard() {
		return null;
	}

	/**
	 * Getter: get instant effect of the space
	 * @return instant effect of the space
	 */
	public Effect getInstantEffect() {
		return instantEffect;
	}
}

/**
 * This is a friendly class, it works like a pair <DevelopmentCard, Space>
 */
class Cell extends Space{
	
	/**
	 * A default serial version ID to the selected type
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Development card of the cell
	 */
	private DevelopmentCard card;
	
	/**
	 * Base constructor
	 * @param cost required dice value
	 * @param instantEffect Effect of the space
	 */
	public Cell(int cost, Effect instantEffect){
		super(cost, instantEffect, true);
	}
	
	@Override
	public void setCard(DevelopmentCard card) {
		this.card = card;
	}

	@Override
	public DevelopmentCard getCard(){
		return card;
	}

}