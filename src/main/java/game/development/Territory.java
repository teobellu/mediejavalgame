package game.development;

import game.GC;
import game.effect.Effect;

public class Territory extends DevelopmentCard{
	
	public Territory(String name, int age, Effect immediate, Effect permanent, int dice) {
		super();
		this.name = name;
		this.age = age;
		addImmediateEffect(immediate);
		addPermanentEffect(permanent);
		this.dice = dice;
	}
	
	@Override
	public void accept(DevelopmentCardVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		return GC.DEV_TERRITORY;
	}
	
}
