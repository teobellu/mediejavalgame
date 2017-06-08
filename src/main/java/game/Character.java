package game;

public class Character extends DevelopmentCard{

	@Override
	public void accept(DevelopmentCardVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		return GC.DEV_CHARACTER;
	}
	
}
