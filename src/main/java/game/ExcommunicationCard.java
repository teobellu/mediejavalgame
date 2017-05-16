package game;

import game.effect.Effect;

public class ExcommunicationCard implements ICard{

	private String name;
	private int age;
	private Effect effect;
	
	public ExcommunicationCard(UserConfig userconfig){
		//TODO
	}	
	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public Effect getEffect() {
		return effect;
	}
}
