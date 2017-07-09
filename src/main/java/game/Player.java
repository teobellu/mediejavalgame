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

	/**
	 * A default serial version ID to the selected type
	 */
	private static final long serialVersionUID = 1L;

	private final transient Client _client;
	
	private boolean afk = false;
	
	private boolean vaticanSupport = false;
	
	private final String _playerColour;
	private final String name;
	private Resource resource;	
	private List<DevelopmentCard> developmentCard;
	private List<LeaderCard> leaderCard;

	private List<FamilyMember> freeMember;
	private List<Effect> effects;
	
	/* da file: */
	private Resource harvestBonus;	
	
	private Resource productionBonus;
	
	private DevelopmentCardManager manager;
	
	/**
	 * TODO
	 * 0 -> NORMAL
	 * 1 -> JUMP
	 * 2 -> RECUPERA JUMP
	 */
	private int delay;

	private boolean OPTActivated;

	protected Player(Client client, String colour){
		_client = client;
		_playerColour = colour;
		name = client.getName();
		resource = new Resource();
		manager = new DevelopmentCardManager();
		developmentCard = new ArrayList<>();
		leaderCard = new ArrayList<>();
		effects = new ArrayList<>();
		setFreeMember(new ArrayList<>());
		delay = GC.NORMAL;
		OPTActivated = false;
		vaticanSupport = false;
	}
	
	public void gain(Resource res){
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
		if (eff.getIEffectBehavior().toString().equals("Do nothing"))
			return;
		eff.setBar(_client.getRoom().getGame().getDynamicBar());
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
	
	public void addDevelopmentCard (DevelopmentCard newCard){
		if (newCard == null || newCard.getId() == 0)
			return;
		developmentCard.add(newCard);
		manager.add(newCard);
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

	public List<DevelopmentCard> getDevelopmentCards() {
		return developmentCard;
	}
	
	public void freeDevelopmentCards(String type) {
		manager.freeList(type);
	}
	
	public List<DevelopmentCard> getDevelopmentCards(String type) {
		return manager.getList(type);
	}

	public List<FamilyMember> getFreeMembers() {
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

	public void setHarvestBonus(Resource harvestBonus) {
		this.harvestBonus = harvestBonus;
	}

	public void setProductionBonus(Resource productionBonus) {
		this.productionBonus = productionBonus;
	}
	
	public String getColour(){
		return _playerColour;
	}

	public boolean isAfk() {
		return afk;
	}

	public void setAfk(boolean afk) {
		this.afk = afk;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public boolean isVaticanSupport() {
		return vaticanSupport;
	}

	public void setVaticanSupport(boolean vaticanSupport) {
		this.vaticanSupport = vaticanSupport;
	}
	
	public void setOPTActivated(boolean check) {
		OPTActivated = check;
	}

	public boolean getOPTActivated() {
		return OPTActivated ;
	}
}
