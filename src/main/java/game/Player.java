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

/**
 * Simply the player model class
 * @author M
 *
 */
public class Player implements Serializable{

	/**
	 * A default serial version ID to the selected type
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Client of the player
	 */
	private final transient Client _client;
	
	/**
	 * Is the player afk? Means offline, you know...
	 */
	private boolean afk = false;
	
	/**
	 * True if the player is a vatican supporter
	 */
	private boolean vaticanSupport = false;
	
	/**
	 * Colour of the player
	 */
	private final String _playerColour;
	
	/**
	 * Name of the player
	 */
	private final String name;
	
	/**
	 * His loot
	 */
	private Resource resource;
	
	/**
	 * Player's development cards
	 */
	private List<DevelopmentCard> developmentCard;
	
	/**
	 * Player's not activated / not discarded leader cards
	 */
	private List<LeaderCard> leaderCard;

	/**
	 * Player's free family members
	 */
	private List<FamilyMember> freeMember;
	
	/**
	 * Player's effects
	 */
	private List<Effect> effects;
	
	/**
	 * Player's harvest bonus (from dashboard bonus, xml)
	 */
	private Resource harvestBonus;	
	
	/**
	 * Player's production bonus (from dashboard bonus, xml)
	 */
	private Resource productionBonus;
	
	/**
	 * @Visitor_Design_Pattern
	 * Handler of development cards
	 */
	private DevelopmentCardManager manager;
	
	/**
<<<<<<< HEAD
	 * Delay malus turn
	 * {@link: GC.NORMAL} -> Normal player
	 * {@link: GC.DELAY} -> Has to jump turn
=======
	 * 
	 * 0 -> NORMAL
	 * 1 -> JUMP
	 * 2 -> RECUPERA JUMP
>>>>>>> 2ddb80416f380a2dd2fc621f9892d7753bfbc5c0
	 */
	private int delay;

	/**
	 * True if the player activated OPT Leader cards
	 */
	private boolean OPTActivated;

	/**
	 * Base constructor of a player
	 * @param client His client
	 * @param colour His colour
	 */
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
	
	/**
	 * Gain some resources
	 * @param res Q.ty
	 */
	public void gain(Resource res){
		resource.add(res);
	}
	
	/**
	 * Pay some resources
	 * @param cost Q.ty to pay
	 * @throws GameException Can't pay
	 */
	public void pay (Resource cost) throws GameException{
		resource.sub(cost);
	}
	
	/**
	 * Add new permanent effects
	 * @param permanentEffect
	 */
	public void addEffect(List<Effect> permanentEffect) {
		permanentEffect.forEach(effect -> addEffect(effect));
	}
	
	/**
	 * Add new effect
	 * @param eff Generic effect
	 */
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
	
	/**
	 * @Lambda_expression
	 * Get activable leader cards {@link LeaderCard}
	 * @return activable leader cards 
	 */
	public List<LeaderCard> getActivableLeaderCards(){
		return leaderCard.stream()
			.filter(card -> card.canPlayThis(this))
			.collect(Collectors.toList()); 
	}
	
	/**
	 * Add new leader card
	 * @param newCard
	 */
	public void addLeaderCard (LeaderCard newCard){
		leaderCard.add(newCard);
	}
	
	/**
	 * Remove a leader card
	 * @param cardToDiscard
	 */
	public void removeLeaderCard (LeaderCard cardToDiscard){
		leaderCard.removeIf(card -> card == cardToDiscard);
	}
	
	/**
	 * Add a new development card
	 * @param newCard
	 */
	public void addDevelopmentCard (DevelopmentCard newCard){
		if (newCard == null || newCard.getId() == 0)
			return;
		developmentCard.add(newCard);
		manager.add(newCard);
	}
	
	/**
	 * Get bonus from dashboard
	 * @param action harvest or production
	 * @return Bonus
	 */
	public Resource getBonus(String action) {
		switch(action){
			case GC.HARVEST : return harvestBonus;
			case GC.PRODUCTION : return productionBonus;
			default : return new Resource();
		}
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

	public boolean isVaticanSupporter() {
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
