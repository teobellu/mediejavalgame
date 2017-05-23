package game;

import java.util.*;

import game.Resource.type;
import game.effect.Effect;
import game.state.*;

public class Player {
	
	private final int id;
	private final String name;
	private Resource resource;	
	private ArrayList<DevelopmentCard> developmentCard;
	private ArrayList<LeaderCard> leaderCard;

	private ArrayList<FamilyMember> freeMember;
	private ArrayList<Effect> effects;
	
	protected Player(){
		id = 5555555;
		name = new String();
		resource = new Resource();
		developmentCard = new ArrayList<>();
		leaderCard = new ArrayList<>();
		effects = new ArrayList<>();
		freeMember = new ArrayList<>();
	}
	
	public void controlGain (Resource res){
		for (Effect x : effects){
			x.setToAnalyze(res);
			x.effect(new StateGaining(null));
			res = (Resource) x.getToAnalyze();
			x.setToAnalyze(null);	
		}
		Gain(res);
	}
	
	public void controlPay (Resource cost) throws GameException{
		Pay(cost);
	}
	
	public void Gain (Resource res){
		resource.add(res);
	}
	
	public void Pay (Resource cost) throws GameException{
		resource.sub(cost);
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
	/*
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
	
	public void market(int i){
		//seleziona familiare
		FamilyMember cosimo = new FamilyMember();
		cosimo.setOwner(this);
		cosimo.setValue(1);
		freeMember.add(cosimo);
		try {
			b.market(i, freeMember.get(0));
		} catch (GameException e) {
			System.err.println("potrei finire qua");
		}
	}
	
	public void endGame (){
		for (Effect x : effects){
			x.effect(new StateEndingGame(null));
		}
	}
	*/
	public Resource getResource(){
		return resource;
	}
	
	/*
	public ArrayList<Effect> getEffects (){
		ArrayList<Effect> effects = new ArrayList<>();
		for (DevelopmentCard card : developmentCard) {
		}
	}*/
/*
	public void showRes() {
		for (type i : type.values())
			if (getResource().get(i) > 0)
				System.out.println(i.name() + " " + getResource().get(i));
	}*/
	
}
