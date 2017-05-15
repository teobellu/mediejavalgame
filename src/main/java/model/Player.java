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
		for (Effect x : effects){
			if(x.getIEffectBehavior() instanceof EffectSufferRake){
				x.setToAnalyze(res);
				x.effect();
				res = (Resource) x.getToAnalyze();
				x.setToAnalyze(null);
			}	
		}
		Gain(res);
	}
	
	public void controlPay (Resource cost) throws GameException{
		for (Effect x : effects){
			if(x.getClass().equals(EffectSufferRake.class))
				System.out.println("i'm he");//x.setToAnalyze(cost);
		}
		Pay(cost);
	}
	
	public void Gain (Resource res){
		resource.add(res);
	}
	
	public void Pay (Resource cost) throws GameException{
		resource.sub(cost);
	}
	
	public void activateEffect (){
		for (Effect x : effects)
			if(x.getIEffectBehavior() instanceof EffectGetResource || 
					x.getIEffectBehavior() instanceof EffectDoHarvest)
				x.effect();
	}
	
	public void addEffect (Effect eff){
		eff.setPlayer(this);
		effects.add(eff);
	}
	
	public void addDevelopmentCard (DevelopmentCard newCard) throws GameException{
		if (newCard.getCost() != null)
			controlPay(newCard.getCost());
		developmentCard.add(newCard);
		newCard.setPlayer(this);
		newCard.activateImmediateEffect();
	}
	
	public void harvest (int power){
		
		if (resource.get(type.SERVANTS) > 0){
			//TODO aumentare power con i servitori
		}
		System.out.println("Sto facendo una produzione di valore " + power);
		
		for (DevelopmentCard card : developmentCard)
			if (card instanceof Territory)
				if (power >= card.getDice()){
					card.activatePermanentEffect();
					System.out.println("sss");
				}
		
	}
	
	public void endGame (){
		for (Effect x : effects){
			if(x.getIEffectBehavior() instanceof EffectLostVictoryForEach)
				x.effect();
		}
	}
	
	public Resource getResource(){
		return resource;
	}

	public void showRes() {
		for (type i : type.values())
			if (getResource().get(i) > 0)
				System.out.println(i.name() + " " + getResource().get(i));
	}
	
}
