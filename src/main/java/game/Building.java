package game;

public class Building extends DevelopmentCard{

	@Override
	public DevelopmentCard getBuilding(){
		return this;
	}

	@Override
	public void accept(DevelopmentCardVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return GameConstants.DEV_BUILDING;
	}
	
}
