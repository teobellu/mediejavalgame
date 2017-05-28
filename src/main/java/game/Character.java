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
	
}
