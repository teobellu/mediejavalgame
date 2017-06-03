package game.effect;

import game.Player;

public class Effect {
	
	/**
	 * @Strategy_Design_Pattern Behavior of the Effect
	 */
	private IEffectBehavior iEffectBehavior;
	private String whenActivate;
	private Object toAnalyze;
	private String toScan;
	private Player player;
	
	public Effect (String whenActivate, IEffectBehavior iEffectBehavior){
		this.whenActivate = whenActivate;
		this.iEffectBehavior = iEffectBehavior;
		toScan = new String();
	}
	
	public void activateEffect (String time){
		if (canActivate(time))
			iEffectBehavior.effect(this);
	}
	
	public Object activateEffect (String time, Object param){
		toAnalyze = param;
		activateEffect(time);
		param = toAnalyze;
		toAnalyze = null;
		return param;
	}
	
	public Object activateEffect (String time, Object param, String message){
		toScan = message;
		param = activateEffect (time, param);
		toScan = null;
		return param;
	}
	
	public boolean canActivate(String time){
		return whenActivate.equals(time);
	}
	
	public IEffectBehavior getIEffectBehavior(){
		return iEffectBehavior;
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
	
	/**
	 * TODO utile per gli effetti istantanei
	 * @return
	 */
	public String getWhenActivate() {
		return whenActivate;
	}
	
}
