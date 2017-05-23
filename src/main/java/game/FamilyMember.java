package game;

public class FamilyMember {
	
	public enum familyColors {BLACK, ORANGE, WHITE, TRANSPARENT};
	
	private Player owner;
	private familyColors color;
	private int value;
	
	public familyColors getColor(){
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
