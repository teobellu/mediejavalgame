package game.effect;

import game.Player;
import game.state.*;

public abstract class Effect {
	
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
		iEffectBehavior.effect(this);
	}

	public Effect (IEffectBehavior iEffectBehavior){
		this.iEffectBehavior = iEffectBehavior;
	}
	
	public void activateEffect (){
		iEffectBehavior.effect(this);
	}
	
	public void effect (State state){}
	
	public void effect (StatePaying state){}
	public void effect (StateGaining state){}
	public void effect (StateEndingGame state){}
	
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
