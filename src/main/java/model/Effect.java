package model;

public class Effect {
	
	private IEffectBehavior iEffectBehavior;
	private Resource resource;
	private Resource toAnalyze;
	private Player player;
	
	public Effect (IEffectBehavior iEffectBehavior){
		this.iEffectBehavior = iEffectBehavior;
	}
	
	public void effect (){
		this.iEffectBehavior.effect(this);
	}
	
	public IEffectBehavior getIEffectBehavior(){
		return iEffectBehavior;
	}
	
	public Resource getResource (){
		return resource;
	}
	public void setResource (Resource res){
		resource = res;
	}
	
	public Player getPlayer (){
		return player;
	}
	public void setPlayer (Player player){
		this.player = player;
	}

	
	public Resource getToAnalyze() {
		return toAnalyze;
	}

	public void setToAnalyze(Resource toAnalyze) {
		this.toAnalyze = toAnalyze;
	}
}
