package game;

public class Building extends DevelopmentCard{

	@Override
	public void accept(DevelopmentCardVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return GC.DEV_BUILDING;
	}
	
}
