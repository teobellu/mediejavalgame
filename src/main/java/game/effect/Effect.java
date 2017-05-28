package game.effect;

import java.util.ArrayList;

import game.Player;
import game.Resource;
import game.state.*;

public abstract class Effect {
	
	private IEffectBehavior iEffectBehavior;
	private ArrayList<Object> parameters;
	private Object toAnalyze;
	private String toScan;
	private Player player;
	protected State state;
	
	public Effect (IEffectBehavior iEffectBehavior){
		this.iEffectBehavior = iEffectBehavior;
		parameters = new ArrayList<>();
		toScan = new String();
	}
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
		iEffectBehavior.effect(this);
	}

	
	
	public void activateEffect (){
		iEffectBehavior.effect(this);
	}
	
	public Object activateEffect (Object param){
		toAnalyze = param;
		activateEffect();
		param = toAnalyze;
		toAnalyze = null;
		return param;
	}
	
	public Object activateEffect (Object param, String message){
		toScan = message;
		param = activateEffect (param);
		toScan = null;
		return param;
	}
	
	public void effect (State state){return;}
	
	public void effect (StatePaying state){return;}
	public void effect (StateGaining state){return;}
	public void effect (StateEndingGame state){return;}
	public void effect (StateIncreaseWorker state){return;}
	public void effect (StateJoiningSpace state){return;}
	
	public Object effect(Object param, State state) {return param;}
	
	public Object effect(Object param, StateGaining state) {return param;}
	public Object effect(Object param, StateHarvest state) {return param;}
	public Object effect(Object param, StateProduct state) {return param;}
	public Object effect(Object param, StateIncreaseWorker state) {return param;}
	public Object effect(Object param, StateJoiningSpace state) {return param;}
	
	public Object effect(Object param, String string, State state) {return param;}	
	
	public Object effect(Object param, String string, StateActionValue state) {return param;}
	public Object effect(Object param, String string, StateJoiningSpace state) {return param;}
	
	public IEffectBehavior getIEffectBehavior(){
		return iEffectBehavior;
	}
	public Object getParameters (){
		return parameters.get(0);
	}
	public Object getParameters (int index){
		return parameters.get(index);
	}
	public void setParameters (Object parameters){
		this.parameters.add(parameters);
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
	
	public void setToScan(String message) {
		toScan = message;
	}

	public String getToScan() {
		return toScan;
	}

	

	

	

	

	
}
