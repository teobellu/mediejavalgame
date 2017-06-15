package game.development;

import game.GC;
import game.Resource;
import game.effect.Effect;

public class Building extends DevelopmentCard{

	public Building(int age, String name, Resource cost, Effect immediate, Effect permanent, int dice) {
		super();
		this.age = age;
		this.name = name;
		this.cost.add(cost);
		immediateEffect.add(immediate);
		permanentEffect.add(permanent);
		this.dice = dice;
	}

	@Override
	public void accept(DevelopmentCardVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return GC.DEV_BUILDING;
	}
	
}
