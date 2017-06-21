package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import exceptions.GameException;
import game.development.DevelopmentCard;
import game.development.DevelopmentCardManager;
import game.effect.Effect;
import server.Client;

public class Player implements Serializable{

	private final transient Client _client;
	
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * List in cui salvo la mia lista di carte leader temporanea, quella da cui posso selezionare all'inizio
	 */
	private List<LeaderCard> _tempLeaders;
	
	public List<LeaderCard> getTempLeaders(){
		return _tempLeaders;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private DevelopmentCardManager manager = new DevelopmentCardManager();

	protected Player(Client client){
		_client = client;
		id = 5555555;
		name = client.getName();
		resource = new Resource();
		developmentCard = new ArrayList<>();
		leaderCard = new ArrayList<>();
		effects = new ArrayList<>();
		setFreeMember(new ArrayList<>());
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
		if (eff == null) 
			return;
		eff.setPlayer(this);
		switch(eff.getWhenActivate()){
			case GC.IMMEDIATE : eff.activateEffect(GC.IMMEDIATE); 
				break;
			case GC.ON_CALL : eff.activateEffect(GC.ON_CALL); 
				break;
			default : effects.add(eff);
		}
	}
	
	public List<LeaderCard> getActivableLeaderCards(){
		return leaderCard.stream()
			.filter(card -> card.canPlayThis(this))
			.collect(Collectors.toList());
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
	
	public Resource getResource(){
		return resource;
	}
	
	public Integer getResource(String type){
		return resource.get(type);
	}

	public List<Effect> getEffects() {
		return effects;
	}
	
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

	public void setFreeMember(List<FamilyMember> freeMember) {
		this.freeMember = freeMember;
		this.freeMember.forEach(familiar -> familiar.setOwner(this));
	}

	public List<LeaderCard> getLeaderCards(){
		return leaderCard;
	}

	public Client getClient(){
		return _client;
	}

	public String getName() {
		return name;
	}

	public Resource getBonus(String action) {
		switch(action){
			case GC.HARVEST : return harvestBonus;
			case GC.PRODUCTION : return productionBonus;
			default : return new Resource();
		}
	}
	
}
