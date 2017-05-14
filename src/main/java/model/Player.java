package model;

import java.util.*;

import model.Resource.type;

public class Player {
	
	private final int id;
	private int turn;
	private final String name;
	private Resource resource;
	private ArrayList<DevelopmentCard> developmentCard;
	private ArrayList<LeaderCard> leaderCard;
	private ArrayList<Effect> effects;
	
	protected Player(){
		id = 5555555;
		name = new String();
		resource = new Resource();
		developmentCard = new ArrayList<>();
		leaderCard = new ArrayList<>();
		effects = new ArrayList<>();
	}
	
	public void controlGain (Resource res){
		//Resource oldResource = resource;
		
		for (Effect x : effects){
			if(x.getIEffectBehavior() instanceof EffectSufferRake){
				x.setToAnalyze(res);
				x.effect();
				res = x.getToAnalyze();
				x.setToAnalyze(null);
			}	
		}
		resource.add(res);/*
		for (type i : type.values())
			if (oldResource.get(i) > resource.get(i))
				resource.add(i, oldResource.get(i) - resource.get(i));
*/	
	}
	
	public void controlPay (Resource cost) throws GameException{
		for (Effect x : effects){
			if(x.getClass().equals(EffectSufferRake.class))
				System.out.println("i'm he");//x.setToAnalyze(cost);
		}
		resource.sub(cost);
	}
	
	public void Gain (Resource res){
		resource.add(res);
	}
	
	public void Pay (Resource cost) throws GameException{
		resource.sub(cost);
	}
	
	public void activateEffect (){
		for (Effect x : effects)
			x.effect();
	}
	
	public void addEffect (Effect eff){
		eff.setPlayer(this);
		effects.add(eff);
	}
	
	/*
	public void buyDevelopmentCard (DevelopmentCard newCard) throws GameException{
		this.controlPay(newCard.getCost());
		this.developmentCard.add(newCard);
	}
	*/
	
	public Resource getResource(){
		return resource;
	}
	
}
