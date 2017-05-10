package model;

public class FamilyMember {
	
	public enum colors {BLACK, ORANGE, WHITE, TRANSPARENT};
	
	private colors color;
	private int value;
	
	public colors getColor(){
		return color;
	}
	
	public int getValue(){
		return value;
	}
	
	public void setValue(int value){
		this.value = value;
	}
}
