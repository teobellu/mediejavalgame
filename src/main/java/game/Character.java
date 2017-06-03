package game;

public class Character extends DevelopmentCard{

	@Override
	public DevelopmentCard getCharacter(){
		return this;
	}

	@Override
	public void accept(DevelopmentCardVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		return GC.DEV_CHARACTER;
	}
	
}
