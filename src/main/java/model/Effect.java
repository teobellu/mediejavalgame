package game;

import game.state.StatePaying;

public class Effect {
	
	private IEffectBehavior iEffectBehavior;
	private Object parameters;
	private Object toAnalyze;
	private Player player;
	protected State state;
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
		iEffectBehavior.effect(this, state);
	}

	public Effect (IEffectBehavior iEffectBehavior){
		this.iEffectBehavior = iEffectBehavior;
	}
	
	public void effect (State state){
		System.out.println(223332);
		//this.state = state;
		iEffectBehavior.effect(this, state);
	}
	
	public void effect (StatePaying state){
		System.out.println(222222);	//omg
		//this.state = state;
		iEffectBehavior.effect(this, state);
	}
	
	public IEffectBehavior getIEffectBehavior(){
		return iEffectBehavior;
	}
	
	public Object getParameters (){
		return parameters;
	}
	public void setParameters (Object parameters){
		this.parameters = parameters;
	}
	
	public Player getPlayer (){
		return player;
	}
	public void setPlayer (Player player){
		this.player = player;
	}

	public Object getToAnalyze() {
		return toAnalyze;
	}
	public void setToAnalyze(Object toAnalyze) {
		this.toAnalyze = toAnalyze;
	}
}
