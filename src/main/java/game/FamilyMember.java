package game;

import java.io.Serializable;

/**
 * This class is designed for the family members, aka workers
 */
public class FamilyMember implements Serializable{
	
	/**
	 * Owner of the familiar
	 */
	private Player owner;
	
	/**
	 * Color of the familiar
	 */
	private String color;
	
	/**
	 * Power of the familiar
	 */
	private int value;
	
	/**
	 * Create a new familiar with power zero
	 * @param color New color of the familiar
	 */
	public FamilyMember(String color){
		this.color = color;
		value = 0;
	}
	
	/**
	 * Getter: get the color of the familiar
	 * @return Color of the familiar
	 */
	public String getColor(){
		return color;
	}
	
	/**
	 * Getter: get the power of the familiar
	 * @return Power of the familiar
	 */
	public int getValue(){
		return value;
	}
	
	/**
	 * Setter: set the power of the familiar
	 * @param value New power of the familiar
	 */
	public void setValue(int value){
		this.value = value;
	}

	/**
	 * Getter: get the owner (player) of the familiar
	 * @return Owner of the familiar
	 */
	public Player getOwner() {
		return owner;
	}

	/**
	 * Setter: set the owner (player) of the familiar
	 * @param player New owner of the familiar
	 */
	public void setOwner(Player player) {
		owner = player;
	}
}
