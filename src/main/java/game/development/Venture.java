package game.development;

import java.util.List;

import game.GC;
import game.Resource;
import game.effect.Effect;

public class Venture extends DevelopmentCard{
	
	int victoryReward;
	
	public Venture(int age, String name, List<Resource> requires, List<Resource> cost, List<Effect> immediate, int reward) {
		super();
		this.age = age;
		this.name = name;
		this.requirement.addAll(requires);
		this.cost.addAll(cost);
		immediateEffect.addAll(immediate);
		victoryReward = reward;
	}

	@Override
	public void accept(DevelopmentCardVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		return GC.DEV_VENTURE;
	}
	
}
