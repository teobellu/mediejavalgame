package model;

public class Effect {
	
	private IEffectBehavior iEffectBehavior;
	private Object parameters;
	private Object toAnalyze;
	private Player player;
	
	public Effect (IEffectBehavior iEffectBehavior){
		this.iEffectBehavior = iEffectBehavior;
	}
	
	public void effect (){
		iEffectBehavior.effect(this);
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
