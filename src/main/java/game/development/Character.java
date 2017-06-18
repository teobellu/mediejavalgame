package game.development;

import java.util.List;

import game.GC;
import game.Resource;
import game.effect.Effect;

public class Character extends DevelopmentCard{

	public Character(int age, String name, Resource cost, List<Effect> immediate, List<Effect> permanent) {
		super();
		this.age = age;
		this.name = name;
		setCost(cost);
		addImmediateEffect(immediate);
		addPermanentEffect(permanent);
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
