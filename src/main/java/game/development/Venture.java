package game.development;

import game.GC;

public class Venture extends DevelopmentCard{
	
	@Override
	public void accept(DevelopmentCardVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		return GC.DEV_VENTURE;
	}
	
}
