package server.game;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.DevelopmentCard;
import model.Effect;
import model.ExcommunicationTile;
import model.FamilyMember;
import model.GC;
import model.LeaderCard;
import model.Messages;
import model.Player;
import model.Resource;
import model.Space;
import model.Venture;
import model.exceptions.GameException;

/**
 * An integral part of the game controller, acts as a player joystick.
 * Encloses the actions that the player can do/perform
 */
public class DynamicAction {
	
	/**
	 * The logger
	 */
	Logger _log = Logger.getLogger(DynamicAction.class.getName());
	
	/**
	 * Current player, ad the joystick holder
	 */
	protected Player player;

	/**
	 * Reference to the corresponding game
	 */
	private Game game;
	
	/**
	 * Constructor
	 * @param player Current player
	 */
	public DynamicAction(Game game){
		this.game = game;
	}
	
	private void setBarEffect(){
		player.getEffects().forEach(effect -> effect.setBar(this));
	}
	
	/**
	 * Set current player
	 * @param player current player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * Activates the effects without modifying objects
	 * @Overload
	 * @param time Type of effect to activate
	 */
	public void activateEffect(String time){
		setBarEffect();
		player.getEffects().stream()
			.forEach(eff -> eff.activateEffect(time));
	}
	
	/**
	 * Activates the effects modifying objects
	 * @Overload
	 * @param param Object to be modified
	 * @param time Type of effect to activate
	 * @return Object modified by the effect
	 */
	public Object activateEffect(Object param, String time){
		setBarEffect();
		Object output = param;
		for (Effect eff : player.getEffects())
			output = eff.activateEffect(time, output);
		return output;
	}
	
	/**
	 * Activates the effects modifying objects depending on a string
	 * @Overload
	 * @param param Object to be modified
	 * @param string The string from which the effect depends
	 * @param time Type of effect to activate
	 * @return Object modified by the effect
	 */
	public Object activateEffect(Object param, String string, String time){
		setBarEffect();
		Object output = param;
		for (Effect eff : player.getEffects())
			output =  eff.activateEffect(time, output, string);
		return output;
	}

	/**
	 * Allows the player to earn resources
	 * @Overload
	 * @param res Resource to gain
	 * @throws RemoteException 
	 */
	public void gain (Resource res) throws RemoteException{
		if (res == null) 
			return;
		Resource toGain = new Resource();
		toGain.add(res);
		toGain = (Resource) activateEffect(toGain, GC.WHEN_GAIN);
		if(toGain.get(GC.RES_COUNCIL) > 0)
			toGain = handleCouncil(toGain);
		player.gain(toGain);
	}
	
	/**
	 * Let player to get rewards from his councils
	 * @param res Resource with councils
	 * @return Resource without councils
	 * @throws RemoteException
	 */
	public Resource handleCouncil(Resource res) throws RemoteException{
		List<Resource> options = new ArrayList<>(GC.COUNCIL_REWARDS);
		int councils = res.get(GC.RES_COUNCIL);
		res.add(GC.RES_COUNCIL, -councils);
		int index;
		do{
			index = player.getClient().getConnectionHandler().spendCouncil(options);
			player.gain(options.get(index));
			options.remove(index);
			councils--;
		}while(councils > 0);
		return res;
	}
	
	/**
	 * Allows the player to earn resources depending on the source
	 * @Overload
	 * @param source Source of earnings
	 * @param res Resource to gain
	 * @throws RemoteException 
	 */
	public void gain (Effect source, Resource res) throws RemoteException{
		if (res == null) 
			return;
		gain(res);
		if (source.getWhenActivate().equals(GC.IMMEDIATE))
			activateEffect(res, source.getSource(), GC.WHEN_GAIN);
	}
	
	/**
	 * Allows the player to increase the value of his action by spending servants
	 * @param actualCost actual cost of the action, here will be added the servants to pay
	 */
	public int increaseWorker (Resource actualCost){
		int playerServants = player.getResource(GC.RES_SERVANTS);
		int amount = 0;
		if (playerServants == 0)
			return 0;
		try {
			amount = player.getClient().getConnectionHandler().askInt(Messages.MESS_INCREASE_WORKER, 0, playerServants);
		} catch (RemoteException e) {
			player.setAfk(true);
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		int price = (Integer) activateEffect(amount, GC.WHEN_INCREASE_WORKER);
		actualCost.add(new Resource(GC.RES_SERVANTS, price));
		return amount;
	}
	
	/**
	 * Returns the tax to be paid according to the established rules of the game
	 * @param column Selected Tower
	 * @return Tax to pay, Fee to pay
	 * @throws GameException Column number is not valid
	 */
	public Resource findTaxToPay (int column){
		Resource tax = new Resource();
		if (!game.getBoard().getFamiliarInSameColumn(column).isEmpty()){
			tax.add(GC.TAX_TOWER);
			tax = (Resource) activateEffect(tax, GC.WHEN_PAY_TAX_TOWER);
		}
		return tax;
	}
	
	/**
	 * If space is single it checks that the player can place his familiar
	 * @param space Action space to analyze
	 * @throws GameException The player can not place his familiar in the space selected;
	 * The space is single and is already occupied
	 */
	private void canOccupyForSpaceLogic(Space space) throws GameException{
		if (space.isSingleObject()){
			if(space.getFamiliars().isEmpty()) 
				return;
			if ((Boolean) activateEffect(true, GC.WHEN_JOINING_SPACE) != null) 
				throw new GameException("The are no free position in this space!");
		}
	}
	
	/**
	 * Designed if space is not single; It checks that the player can place his familiar;
	 * Is called even if space is single, see Ludovico Ariosto's effect
	 * @param familiar The familiar that the player wants to place
	 * @param placedFamiliar Like family members in space
	 * @throws GameException There are two non-transparent familiars of the same color
	 */
	private void canOccupyForColorLogic(FamilyMember familiar, List<FamilyMember> placedFamiliar) throws GameException{
		List<FamilyMember> playerFamiliar = new ArrayList<>();
		playerFamiliar.addAll(placedFamiliar);
		playerFamiliar.add(familiar);
		long countNotTransparent = playerFamiliar.stream()
			.filter(fam -> fam.getOwner() == familiar.getOwner())
			.filter(fam -> !fam.getColor().equals(GC.FM_TRANSPARENT))
			.count();
		if(countNotTransparent > 1) 
			throw new GameException("Your familiar's color not allows you to occupy this space!");
	}
	
	/**
	 * It checks that the player can place his familiar in the space selected
	 * @param familiar The familiar that the player wants to place
	 * @param space Action space to analyze
	 * @throws GameException The player can not place his familiar in the space selected
	 */
	private void canOccupySpace (FamilyMember familiar, Space space) throws GameException{
		canOccupyForSpaceLogic(space);
		canOccupyForColorLogic(familiar, space.getFamiliars());
	}
	
	/**
	 * The method allows the player to get a card from a cell of ​​four towers
	 * @param value Power of the action
	 * @param row Floor selected
	 * @param column Tower selected
	 * @throws GameException The player does not meet the requirements for getting the card, 
	 * for example he already has six development cards of the same type
	 */
	public void visitTower(Integer value, int row, int column) throws GameException {
		Resource cost = new Resource();
		Space space = game.getBoard().getFromTowers(row, column);
		DevelopmentCard card = space.getCard();
		if (card == null)
			throw new GameException("There is no card here");
		if (player.getDevelopmentCards(card.toString()).size() == GC.MAX_DEVELOPMENT_CARDS)
			throw new GameException("You already have 6 cards of the same type");
		int newValue = value + increaseWorker(cost);
		cost.add(findTaxToPay(column));
		/**
		 * Resource holder
		 */
		int index = 0;
		if(card.getCosts().size() > 1){
			try {
				index = player.getClient().getConnectionHandler().askInt("The card has more costs. Which do you prefer to pay?", 0, card.getCosts().size()-1);
			} catch (RemoteException e) {
				player.setAfk(true);
				_log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		//card requirement
		tryToPayRequirement(card.toString(), card.getRequirement(index));
		//dashboard requirement (for example for territory you need military points)
		tryToPayRequirement(card.toString(), getDashboardRequirement(card));
		
		Resource cardCost = new Resource();
		cardCost.add(card.getCost(index));
		cardCost = (Resource) activateEffect(cardCost, card.toString(), GC.WHEN_FIND_COST_CARD);
		cost.add(cardCost);
		
		/**
		 * Power of the action
		 */
		newValue = (Integer) activateEffect(newValue, card.toString(), GC.WHEN_FIND_VALUE_ACTION);
		if (newValue < space.getRequiredDiceValue()) 
			throw new GameException(Messages.MESS_LOW_POWER);
		player.pay(cost);
		
		/**
		 * I can get the card, so...
		 */
		Effect spaceEffect = space.getInstantEffect();
		if(row > 1)
			spaceEffect = (Effect) activateEffect(spaceEffect, GC.WHEN_GET_TOWER_BONUS);
		player.addEffect(spaceEffect);
		player.addDevelopmentCard(card);
		player.addEffect(card.getImmediateEffect());
		if (card.getDice() == 0)
			player.addEffect(card.getPermanentEffect());
		space.setCard(null);
	}
	
	/**
	 * The method allows the player to place his familiar in a space of ​​four towers
	 * @param familiar The familiar that the player wants to place
	 * @param row Floor selected
	 * @param column Tower selected
	 * @throws GameException The player can not place his familiar in the space selected
	 */
	public void placeInTower (FamilyMember familiar, int row, int column) throws GameException{
		Space space = game.getBoard().getFromTowers(row, column);
		canOccupySpace(familiar, space);
		canOccupyForColorLogic(familiar, game.getBoard().getFamiliarInSameColumn(column));
		visitTower(familiar.getValue(), row, column);
		endAction(familiar, space);
	}
	
	/**
	 * Verifies that the player has the established requirement
	 * @param typeOfCard Simply the type of cards
	 * @param requirement Resource requirement that the player has to possess
	 * @throws GameException The player does not have the necessary requirements
	 */
	private void tryToPayRequirement(String typeOfCard, Resource requirement) throws GameException{
		Resource resourcesRequired = requirement;
		resourcesRequired = (Resource) activateEffect(resourcesRequired, typeOfCard, GC.WHEN_PAY_REQUIREMENT);
		player.pay(resourcesRequired);
		player.gain(resourcesRequired);
	}
	
	/**
	 * To add a new card you need to make sure that you have the requirement set on the 
	 * player dashboard; This method returns that requirement
	 * @param newCard New card that the player would like to add to his dashboard
	 * @return Returns the requirement set by the player dashboard
	 */
	public Resource getDashboardRequirement(DevelopmentCard newCard){
		int index = player.getDevelopmentCards(newCard.toString()).size();
		int requirement = 0;
		if (newCard.toString().equals(GC.DEV_TERRITORY))
			requirement = GC.REQ_TERRITORY.get(index);
		return new Resource(GC.RES_MILITARYPOINTS, requirement);
	}
	
	/**
	 * The method allows the player to place his familiar in the market
	 * @param familiar The familiar that the player wants to place
	 * @param whichSpace In which space the player wants to place the family
	 * @throws GameException The player can not place his familiar in the space selected
	 */
	public void placeMarket (FamilyMember familiar, int whichSpace) throws GameException{
		if (whichSpace >= 2 && game.getPlayers().size() < 4)
			throw new GameException(Messages.MESS_FEW_PLAYERS);
		if ((Boolean) activateEffect(true, GC.WHEN_PLACE_FAMILIAR_MARKET) == null) 
			throw new GameException("You have a malus that not allow you to put your family member in the market");
		Space space = game.getBoard().getMarketSpace(whichSpace);
		Resource cost = new Resource();
		canOccupySpace(familiar, space);
		int value = familiar.getValue() + increaseWorker(cost);
		if (value < space.getRequiredDiceValue())
			throw new GameException(Messages.MESS_LOW_POWER);
		player.pay(cost);
		player.addEffect(space.getInstantEffect());
		endAction(familiar, space);
	}
	
	/**
	 * The method allows the player to place his familiar in the council palace
	 * @param familiar The familiar that the player wants to place
	 * @throws GameException The value of player's familiar is too low
	 */
	public void placeCouncilPalace (FamilyMember familiar) throws GameException{
		GameInformation infoGame = game.getGameInformation();
		Space space = game.getBoard().getCouncilPalaceSpace();
		int power = (Integer) activateEffect(familiar.getValue(), GC.WHEN_FIND_VALUE_ACTION);
		Resource cost = new Resource();
		int value = power + increaseWorker(cost);
		if (value < space.getRequiredDiceValue())
			throw new GameException(Messages.MESS_LOW_POWER);
		if (!infoGame.getHeadPlayersTurn().contains(player))
			infoGame.getHeadPlayersTurn().add(player);
		player.addEffect(space.getInstantEffect());
		endAction(familiar, space);
	}
	
	/**
	 * If you choose to make an action such as harvest or production, 
	 * the following method determines which space is appropriate for doing the action
	 * @param familiar The familiar that the player wants to place
	 * @param action Type of action
	 * @return The appropriate space
	 * @throws GameException No space is suitable for perform the action
	 */
	private Space findCorrectWorkSpace(FamilyMember familiar, String action) throws GameException{
		Space space = game.getBoard().getWorkSpace(action);
		try{
			canOccupySpace(familiar, space);
		}
		catch (GameException e) {
			_log.log(Level.OFF, e.getMessage(), e);
			if (game.getPlayers().size() < 3)
				throw new GameException(Messages.MESS_FEW_PLAYERS);
			canOccupyForColorLogic(familiar, space.getFamiliars());
			space = game.getBoard().getWorkLongSpace(action);
			canOccupySpace(familiar, space);
		}
		return space;
	}
	
	/**
	 * The method is called when a player wants to do a work action, 
	 * such as harvest or production
	 * @param familiar The familiar that the player wants to place
	 * @param action Type of action
	 * @throws GameException The player can not perform the action
	 */
	public void placeWork (FamilyMember familiar, String action) throws GameException{
		int power = familiar.getValue();
		Space space = findCorrectWorkSpace(familiar, action);
		player.addEffect(space.getInstantEffect());
		int newPower = (Integer) activateEffect(power, action, GC.WHEN_FIND_VALUE_ACTION);
		if (newPower < Math.max(space.getRequiredDiceValue(),1)){
			player.getEffects().removeIf(eff -> eff.getSource().equals(GC.ACTION_SPACE));
			throw new GameException(Messages.MESS_LOW_POWER);
		}
		launchesWork(power, action);
		endAction(familiar, space);
	}
	
	/**
	 * Depending on the action specified, determines which cards must be analyzed;
	 * For example, production analyzes building cards;
	 * Then, perform the work action; In that example the production action
	 * @param power Value of the action
	 * @param action Type of action
	 * @throws GameException 
	 */
	public void launchesWork(Integer power, String action) throws GameException{
		switch(action){
			case GC.HARVEST : work(power, action, GC.DEV_TERRITORY); 
				break;
			case GC.PRODUCTION : work(power, action, GC.DEV_BUILDING); 
				break;
			default : return;
		}
	}
	
	/**
	 * This method is used to perform the harvest action and production action
	 * @param power Value of the action
	 * @param action Type of action
	 * @param cards Type of cards, generally chosen by launchesWork method
	 * @throws GameException 
	 */
	private void work (int power, String action, String cards) throws GameException{
		Resource cost = new Resource();
		int realActionPower = (Integer) activateEffect(power, action, GC.WHEN_FIND_VALUE_ACTION) + increaseWorker(cost);
		if (realActionPower < 1){
			player.getEffects().removeIf(eff -> eff.getSource().equals(GC.ACTION_SPACE));
			throw new GameException(Messages.MESS_LOW_POWER);
		}
		try{
			player.pay(cost);
		}catch (GameException e){
			_log.log(Level.OFF, e.getMessage(), e);
			player.getEffects().removeIf(eff -> eff.getSource().equals(GC.ACTION_SPACE));
			throw new GameException("You can't pay those servants");
		}
		player.getDevelopmentCards(cards).stream()
			.filter(card -> realActionPower >= card.getDice())
			.forEach(card -> player.addEffect(card.getPermanentEffect()));
		player.gain(player.getBonus(action));
		player.getEffects().removeIf(eff -> eff.getSource().equals(GC.ACTION_SPACE));
	}
	
	/**
	 * This method is called after the player has successfully performed a familiar placement 
	 * action
	 * @param familiar Family used to perform the action
	 * @param space The space where the action was performed
	 */
	private void endAction(FamilyMember familiar, Space space){
		space.placeFamiliar(familiar);
		player.getFreeMembers().removeIf(member -> member == familiar);
		player.getEffects().removeIf(effect -> effect.getSource().equals(GC.ACTION_SPACE));
	}
	
	/**
	 * This method allows the player to show support to the Vatican. 
	 * If he does not have the requirements this method will automatically call the method
	 * dontShowVaticanSupport()
	 * @param age Age Curren age
	 * @throws GameException It will generally not be launched unless there are errors in xml
	 */
	public void showVaticanSupport(int age){
		GameInformation infoGame = game.getGameInformation();
		int faithPoints = player.getResource(GC.RES_FAITHPOINTS);
		if (!player.isVaticanSupporter() || faithPoints < 2 + age){
			dontShowVaticanSupport(age);
			return;
		}
		player.getResource().add(GC.RES_FAITHPOINTS, -faithPoints);
		int indexFaith = Math.min(faithPoints, infoGame.getBonusFaith().size() - 1);
		int victory = infoGame.getBonusFaith().get(indexFaith);
		player.gain(new Resource(GC.RES_VICTORYPOINTS, victory));
		game.otherPlayersInfo(player.getName() + Messages.MESS_NOT_EXCOMMUNICATED, player);
		activateEffect(GC.WHEN_SHOW_SUPPORT);
	}
	
	/**
	 * This method will be called by players who can not or do not want to show support to the Vatican
	 * @param age Curren age
	 */
	private void dontShowVaticanSupport(int age){
		ExcommunicationTile tile = game.getBoard().getExCard()[age - 1];
		Effect malus = tile.getEffect();
		player.addEffect(malus);
		game.broadcastInfo(player.getName() + Messages.MESS_EXCOMMUNICATED);
	}
	
	/**
	 * The method allows the player to activate a specific leader card
	 * @param card Leader card to activate
	 * @throws GameException The player does not have the necessary requirements to activate 
	 * the leader card
	 */
	public void activateLeaderCard(LeaderCard card) throws GameException{
		GameInformation infoGame = game.getGameInformation();
		if (!card.canPlayThis(player))
			throw new GameException("You don't have the necessary requirements to activate this card");
		player.removeLeaderCard(card);
		player.addEffect(card.getEffect());
		infoGame.addDiscardedLeader(card, player);
		game.otherPlayersInfo(player.getName() + Messages.MESS_ACTIVATED_LEADER + card.getName(), player);
	}
	
	/**
	 * The method allows the player to discard a leader card to gain a council privilege
	 * @param card Leader card to discard
	 * @throws RemoteException 
	 */
	public void discardLeaderCard(LeaderCard card) throws RemoteException{
		player.removeLeaderCard(card);
		gain(new Resource(GC.RES_COUNCIL, 1));
		game.otherPlayersInfo(player.getName() + Messages.MESS_DISCARDED_LEADER + card.getName(), player);
	}
	
	/**
	 * This method is launched by each player at the end of the game, 
	 * it counts the final victory points by consulting the rules of the game
	 */
	public void endGame (){
		activateEffect(GC.WHEN_END);
		int territory = player.getDevelopmentCards(GC.DEV_TERRITORY).size();
		int character = player.getDevelopmentCards(GC.DEV_CHARACTER).size();
		addFinalReward(GC.REW_TERRITORY, territory);
		addFinalReward(GC.REW_CHARACTER, character);
		addVentureReward();
		exchangeResourceToVictory();
	}
	
	/**
	 * Add victory points depicted on venture cards using functional programming
	 */
	private void addVentureReward(){
		List<DevelopmentCard> venture = player.getDevelopmentCards(GC.DEV_VENTURE);
		Integer ventureReward = venture.stream()
			.map(card -> ((Venture)card).getVictoryReward())
			.reduce(0, (sum, card) -> sum + card);
		player.gain(new Resource(GC.RES_VICTORYPOINTS, ventureReward));
	}
	
	/**
	 * Adds victory points depending on the amount of cards, of a certain type, 
	 * accumulated until the end of the game
	 * @param rewardList List of points provided by the game rules
	 * @param index Amount of cards, of a certain type, that the player has
	 */
	public void addFinalReward (List<Integer> rewardList, int index){
		int position = index;
		position--;
		if (position < 0) 
			return;
		Resource finalReward = new Resource(GC.RES_VICTORYPOINTS, rewardList.get(position));
		player.gain(finalReward);
	}
	

	/**
	 * At the end of the game, assign to the player victory points 
	 * based on his stock of resources.
	 */
	private void exchangeResourceToVictory(){
		int amount = 0;
		Resource toCount = player.getResource();
		amount += toCount.get(GC.RES_COINS);
		amount += toCount.get(GC.RES_WOOD);
		amount += toCount.get(GC.RES_STONES);
		amount += toCount.get(GC.RES_SERVANTS);
		if(GC.END_REWARD_RESOURCE > 0){
			amount = amount / GC.END_REWARD_RESOURCE;
			player.gain(new Resource(GC.RES_VICTORYPOINTS, amount));
		}
	}
	
	/**
	 * Get discarded leader cards from game information
	 * @return Map of discarded leader cards
	 */
	public Map<LeaderCard, Player> getDiscardedLeaderCards() {
		GameInformation infoGame = game.getGameInformation();
		return infoGame.getDiscardedLeader();
	}
	 
}
