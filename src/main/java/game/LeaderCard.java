package game;

import java.util.function.Function;

import game.effect.Effect;

public class LeaderCard implements ICard{
	private String name;
	private Effect effect;
	private Function<Player, Boolean> requirement;
	
	public LeaderCard(String name, Effect effect, Function<Player, Boolean> requirement) {
		this.name = name;
		this.effect = effect;
		if (effect != null)
			this.effect.setSource(GC.LEADER_CARD);
		if (requirement != null)
			this.requirement = requirement;
		else
			this.requirement = player -> true;
	}
	
	public boolean canPlayThis(Player player){
		return requirement.apply(player);
	}

	public String getName() {
		return name;
	}

	public Effect getEffect() {
		return effect;
	}
	
}
