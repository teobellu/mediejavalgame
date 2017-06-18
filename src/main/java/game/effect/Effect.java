package game.effect;

import java.io.Serializable;

import game.DynamicAction;
import game.GC;
import game.Player;

public class Effect implements Serializable{
	
	/**
	 * @Strategy_Design_Pattern Behavior of the Effect
	 */
	private IEffectBehavior iEffectBehavior;
	private String whenActivate;
	private String source;
	private Object toAnalyze;
	private String toScan;
	private Player player;
	private DynamicAction bar;
	
	public Effect (String whenActivate, IEffectBehavior iEffectBehavior){
		this.whenActivate = whenActivate;
		this.iEffectBehavior = iEffectBehavior;
		source = GC.DEFAULT;
	}
	
	public void activateEffect (String time){
		if (canActivate(time))
			iEffectBehavior.effect(this);
	}
	
	public Object activateEffect (String time, Object param){
		Object newParam;
		toAnalyze = param;
		activateEffect(time);
		newParam = toAnalyze;
		toAnalyze = null;
		return newParam;
	}
	
	public Object activateEffect (String time, Object param, String message){
		Object newParam;
		toScan = message;
		newParam = activateEffect (time, param);
		toScan = null;
		return newParam;
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
	
	public String getWhenActivate() {
		return whenActivate;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public DynamicAction getBar() {
		return bar;
	}

	public void setBar(DynamicAction bar) {
		this.bar = bar;
	}
	
}
