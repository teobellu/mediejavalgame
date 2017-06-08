package game;

import java.util.*;
import java.util.stream.Collectors;

import game.effect.Effect;
import server.ConnectionHandler;
import game.state.*;
import game.GC.*;

public class Player {
	
	private final int id;
	private final String name;
	private Resource resource;	
	private List<DevelopmentCard> developmentCard;
	private List<LeaderCard> leaderCard;

	private List<FamilyMember> freeMember;
	private List<Effect> effects;
	
	/* da file: */
	private Resource harvestBonus;	
	private Resource productionBonus;
	
	private DynamicAction dynamicBar;
	
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
	}
	
	public void gain (Resource res){
		//TODO Se res contiene privilegi del consiglio?
		resource.add(res);
	}
	
	public void pay (Resource cost) throws GameException{
		resource.sub(cost);
	}
	
	public void addEffect(List<Effect> permanentEffect) {
		permanentEffect.forEach(effect -> addEffect(effect));
	}
	
	public void addEffect (Effect eff){
		if (eff == null) return;
		eff.setPlayer(this);
		switch(eff.getWhenActivate()){
			case GC.IMMEDIATE : eff.activateEffect(GC.IMMEDIATE); break;
			case GC.ON_CALL : eff.activateEffect(GC.ON_CALL); break;
			default : effects.add(eff);
		}
	}
	
	public void addLeaderCard (LeaderCard newCard){
		leaderCard.add(newCard);
	}
	
	public void removeLeaderCard (LeaderCard cardToDiscard){
		leaderCard.removeIf(card -> card == cardToDiscard);
	}
	
	public void showLeaderCard (){
		leaderCard.stream().forEach(card -> System.out.println(card.getName()));
	}
	
	public void addDevelopmentCard (DevelopmentCard newCard){
		developmentCard.add(newCard);
		manager.add(newCard);
		//newCard.setPlayer(this);
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

	public List<Effect> getEffects() {
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
	
	public void showFam() {
		freeMember.stream().forEach(fam -> System.out.println(fam.getColor() + " " + fam.getValue()));
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

	public List<FamilyMember> getFreeMember() {
		return freeMember;
	}

	public void setFreeMember(ArrayList<FamilyMember> freeMember) {
		this.freeMember = freeMember;
	}

	

	
	
}
