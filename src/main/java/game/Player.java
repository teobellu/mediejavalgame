package game;

import java.util.*;

import game.effect.Effect;
import game.state.*;
import game.GC.*;

public class Player {
	
	private final int id;
	private final String name;
	private Resource resource;	
	private ArrayList<DevelopmentCard> developmentCard;
	private ArrayList<LeaderCard> leaderCard;

	private ArrayList<FamilyMember> freeMember;
	private ArrayList<Effect> effects;
	
	private DynamicAction dynamicBar;
	
	private boolean check;
	
	private DevelopmentCardManager manager = new DevelopmentCardManager();

	protected Player(){
		id = 5555555;
		name = new String();
		resource = new Resource();
		developmentCard = new ArrayList<>();
		leaderCard = new ArrayList<>();
		effects = new ArrayList<>();
		setFreeMember(new ArrayList<>());
		dynamicBar = new DynamicAction(this);
		setCheck(false);
	}
	
	public void gain (Resource res){
		resource.add(res);
	}
	
	public void pay (Resource cost) throws GameException{
		resource.sub(cost);
	}
	
	public void addEffect (Effect eff){
		eff.setPlayer(this);
		if (eff.getWhenActivate() == GC.IMMEDIATE)
			eff.activateEffect(GC.IMMEDIATE);
		else
			effects.add(eff);
	}
	
	public void addDevelopmentCard (DevelopmentCard newCard){
		developmentCard.add(newCard);
		manager.add(newCard);
		newCard.setPlayer(this);
		//newCard.activateImmediateEffect();
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

	public ArrayList<Effect> getEffects() {
		return effects;
	}

	public DynamicAction getDynamicBar() {
		return dynamicBar;
	}
	
	/*
	public ArrayList<Effect> getEffects (){
		ArrayList<Effect> effects = new ArrayList<>();
		for (DevelopmentCard card : developmentCard) {
		}
	}*/

	public void showRes() {
		for (String i : GC.RES_TYPES)
			if (getResource().get(i) > 0)
				System.out.println(i + " " + getResource().get(i));
	}

	public List<DevelopmentCard> getDevelopmentCards() {
		return developmentCard;
	}
	
	public void freeDevelopmentCards(String type) {
		manager.freeList(type);
	}
	
	public List<DevelopmentCard> getDevelopmentCards(String type) {
		return manager.getList(type);
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public List<FamilyMember> getFreeMember() {
		return freeMember;
	}

	public void setFreeMember(ArrayList<FamilyMember> freeMember) {
		this.freeMember = freeMember;
	}
	
}
