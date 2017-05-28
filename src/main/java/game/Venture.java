package game;

public class Venture extends DevelopmentCard{

	@Override
	public DevelopmentCard getVenture(){
		return this;
	}
	
	@Override
	public void accept(DevelopmentCardVisitor visitor) {
		visitor.visit(this);
	}
	
}
