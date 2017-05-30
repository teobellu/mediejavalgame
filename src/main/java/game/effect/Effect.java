package game.effect;

import java.util.ArrayList;

import game.Player;
import game.Resource;
import game.effect.when.*;
import game.state.*;

public abstract class Effect {
	
	private IEffectBehavior iEffectBehavior;
	private ArrayList<Object> parameters;
	private Object toAnalyze;
	private String toScan;
	private Player player;
	
	public Effect (IEffectBehavior iEffectBehavior){
		this.iEffectBehavior = iEffectBehavior;
		parameters = new ArrayList<>();
		toScan = new String();
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
	
	public void effect (Effect when){return;}
	
	public void effectWhenEnd() {return;}
	public void effectWhenJoiningSpace() {return;}
	public void effectWhenPlaceFamiliarMarket() {return;}
	public void effectWhenSetFamiliarStartPower() {return;}
	
	public Object effect(Object param, Effect when) {return param;}
	
	public Object effectWhenGain(Object param) {return param;}
	public Object effectWhenPayTaxTower(Object param) {return param;}
	public Object effectWhenIncreaseWorker(Object param) {return param;}
	
	public Object effect(Object param, String string, Effect when) {return param;}	
	
	public Object effectWhenFindValueAction(Object param, String message) {return param;}

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

	public void WhenSetFamiliarStartPower() {
		// TODO Auto-generated method stub
		
	}

	
	
}
