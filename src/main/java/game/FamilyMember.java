package game;

public class FamilyMember {
	
	private Player owner;
	private String color;
	private int value;
	
	public String getColor(){
		return color;
	}
	
	public int getValue(){
		return value;
	}
	
	public void setValue(int value){
		this.value = value;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player player) {
		owner = player;
	}
}
