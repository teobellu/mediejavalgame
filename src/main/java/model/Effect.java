package model;

import java.io.Serializable;

import server.game.DynamicAction;
import server.game.effectControllers.IEffectBehavior;

/**
 * Store all info about an effect
 * @Strategy_Design_Pattern
 * IeffectBehavior stores his behavior and it use the game controller
 * @author Jacopo
 * @author Matteo
 *
 */
public class Effect implements Serializable{
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @Strategy_Design_Pattern 
	 * Behavior of the Effect
	 */
	private IEffectBehavior iEffectBehavior;
	
	/**
	 * Determines when the effect must be activated
	 */
	private String whenActivate;
	
	/**
	 * Source or the effect
	 */
	private String source;
	
	/**
	 * Object to analyze for activate effect (e.g. resources to double)
	 */
	private transient Object toAnalyze;
	
	/**
	 * Get a string to scan (e.g. play 1 coins for each "territory")
	 */
	private String toScan;
	
	/**
	 * Effect owner
	 */
	private Player player;
	
	/**
	 * Dynamic bar
	 */
	private transient DynamicAction bar;
	
	/**
	 * Base constructor
	 * @param whenActivate When activate effect
	 * @param iEffectBehavior What do when effect is activated
	 */
	public Effect (String whenActivate, IEffectBehavior iEffectBehavior){
		this.whenActivate = whenActivate;
		this.iEffectBehavior = iEffectBehavior;
		source = GC.DEFAULT;
	}
	
	/**
	 * Activate simple effects
	 * @param time
	 */
	public void activateEffect (String time){
		if (canActivate(time))
			iEffectBehavior.effect(this);
	}
	
	/**
	 * Activate effects based on time that modify an object
	 * @param time
	 * @param param
	 * @return Modified object
	 */
	public Object activateEffect (String time, Object param){
		Object newParam;
		toAnalyze = param;
		activateEffect(time);
		newParam = toAnalyze;
		toAnalyze = null;
		return newParam;
	}
	
	/**
	 * Activate effects based on time and a another string that modify an object
	 * @param time Time
	 * @param param Central object of the effect
	 * @param message The string
	 * @return Modified object
	 */
	public Object activateEffect (String time, Object param, String message){
		Object newParam;
		toScan = message;
		newParam = activateEffect (time, param);
		toScan = null;
		return newParam;
	}
	
	/**
	 * True if I can activate this effect
	 * @param time
	 * @return True for yes
	 */
	public boolean canActivate(String time){
		return whenActivate.equals(time);
	}
	
	/**
	 * @Strategy_Design_Pattern
	 * Get behavior
	 * @return
	 */
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
	
	@Override
	public String toString(){
		return iEffectBehavior.toString();
	}
	
}
