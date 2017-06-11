package game;

import game.effect.Effect;

/**
 * This model class represents the excommunication tiles
 */
public class ExcommunicationTile implements ICard{

	/**
	 * ID of the single card, for view;
	 * Written in lower case for sonar
	 */
	private int id;
	
	/**
	 * Age of the card, normally by 1 to 3
	 */
	private int age;
	
	/**
	 * Malus of the card
	 */
	private Effect effect;
	
	/**
	 * Constructor of a single excommunication card
	 * @param name Name of the card to set
	 * @param age Age of the card to set
	 * @param effect Malus of the card to set
	 */
	public ExcommunicationTile(int id, int age, Effect effect){
		this.id = id;
		this.age = age;
		this.effect = effect;
	}
	
	/**
	 * Getter, get the ID of the card
	 * @return ID of the card
	 */
	public int getID() {
		return id;
	}

	/**
	 * Getter, get the age of the card
	 * @return Age of the card
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Getter, get the malus, namely effect, of the card
	 * @return Malus of the card
	 */
	public Effect getEffect() {
		return effect;
	}
}
