package game;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.omg.CORBA._PolicyStub;

import exceptions.GameException;
import game.development.DevelopmentCard;
import game.development.Venture;
import game.effect.Effect;

/**
 * An integral part of the game controller, acts as a player joystick.
 * Encloses the actions that the player can do/perform
 */
public class DynamicAction {
	
	private static final String MESSAGE_INCREASE_WORKER = "You can spend servants to increase this action! How much do you want to increase?";
	
	/**
	 * Current player, ad the joystick holder
	 */
	protected Player player;

	//TODO
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
	
	//TODO momentaneo ( o anche non momentaneo )
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
	 * Let player to get rewards from his cuncils
	 * @param res Resource with councils
	 * @return Resource without councils
	 * @throws RemoteException
	 */
	private Resource handleCouncil(Resource res) throws RemoteException{
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
	 * Method that is launched at the beginning of the turn
	 */
	public void startTurn(){
		activateEffect(GC.ONCE_PER_TURN);
	}
	
	/**
	 * Method that is launched just after the dices have been launched
	 */
	public void readDices(){
		activateEffect(GC.WHEN_ROLL);
	}
	
	/**
	 * TODO DESCRIP
	 * @param familiar
	 * @param amount
	 * @throws GameException
	 */
	public int increaseWorker () throws GameException{
		int playerServants = player.getResource(GC.RES_SERVANTS);
		int amount = 0;
		if (playerServants == 0)
			return 0;
		try {
			amount = player.getClient().getConnectionHandler().askInt(MESSAGE_INCREASE_WORKER, 0, playerServants);
		} catch (RemoteException e) {
			//TODO
		}
		int price = (Integer) activateEffect(amount, GC.WHEN_INCREASE_WORKER);
		player.pay(new Resource(GC.RES_SERVANTS, price));
		//familiar.setValue(familiar.getValue() + amount);TODO
		return amount;
	}
	
	/**
	 * Returns the tax to be paid according to the established rules of the game
	 * @param column Selected Tower
	 * @return Tax to pay, Fee to pay
	 * @throws GameException Column number is not valid
	 */
	public Resource findTaxToPay (int column) throws GameException{
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
				throw new GameException();
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
			throw new GameException();
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
		if (card == null || player.getDevelopmentCards(card.toString()).size() == GC.MAX_DEVELOPMENT_CARDS)
			throw new GameException();
		value += increaseWorker();
		cost.add(findTaxToPay(column));
		
		// qui dovrei chiedere al giocatore, se cost != null, se proseguire o no.
		
		/**
		* RESOURCE
		 */
		int index = 0;
		if(card.getCost().size() > 1){
			//player.getClient().getConnectionHandler().chooseCost();
		}
		//ma potrebbe avere anche 2 tipi di costo
		//if (card.getCost.size > 1)...		
		
		//card requirement
		tryToPayRequirement(card.toString(), card.getRequirement(index));
		//dashboard requirement (for example for territory you need military points)
		tryToPayRequirement(card.toString(), getDashboardRequirement(card));
		
		
		Resource cardCost = card.getCost(index);
		
		System.out.println("a+ " + cardCost.toString());//TODO
		
		cardCost = (Resource) activateEffect(cardCost, card.toString(), GC.WHEN_FIND_COST_CARD);
		
		
		System.out.println("b+ " + cardCost.toString());//TODO
		
		cost.add(cardCost);
		
		/**
		 * DICE
		 */
		value = (Integer) activateEffect(value, card.toString(), GC.WHEN_FIND_VALUE_ACTION);
		
		if (value < space.getRequiredDiceValue()) 
			throw new GameException();
		
		
		//canDicePaySpace(familiar, space);
		//canJoinSpace(familiar, space); //e quindi canJoinArraySpace(familiar, space);

		System.out.println("c+ " + cost.toString());//TODO
		
		player.pay(cost);
		
		/**
		 * posso
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
		if ((Boolean) activateEffect(true, GC.WHEN_PLACE_FAMILIAR_MARKET) == null) 
			throw new GameException();
		Space space = game.getBoard().getMarketSpace(whichSpace);
		canOccupySpace(familiar, space);
		player.addEffect(space.getInstantEffect());
		endAction(familiar, space);
	}
	
	/**
	 *  comments TODO
	 * @param familiar The familiar that the player wants to place
	 * @throws GameException TODO
	 */
	public void placeCouncilPalace (FamilyMember familiar) throws GameException{
		GameInformation infoGame = game.getGameInformation();
		Space space = game.getBoard().getCouncilPalaceSpace();
		int power = (Integer) activateEffect(familiar.getValue(), GC.WHEN_FIND_VALUE_ACTION);
		if (power < space.getRequiredDiceValue())
			throw new GameException();
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
		if (newPower < Math.max(space.getRequiredDiceValue(),1)) 
			throw new GameException();
		launchesWork(power, action);
		endAction(familiar, space);
	}
	
	/**
	 * Depending on the action specified, determines which cards must be analyzed;
	 * For example, production analyzes building cards;
	 * Then, perform the work action; In that example the production action
	 * @param power Value of the action
	 * @param action Type of action
	 */
	public void launchesWork(Integer power, String action){
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
	 */
	private void work (int power, String action, String cards){
		int realActionPower = (Integer) activateEffect(power, action, GC.WHEN_FIND_VALUE_ACTION);
		player.getDevelopmentCards(cards).stream()
			.filter(card -> realActionPower >= card.getDice())
			.forEach(card -> player.addEffect(card.getPermanentEffect()));
		player.gain(player.getBonus(action));
	}
	
	/**
	 * This method is called after the player has successfully performed a familiar placement 
	 * action
	 * @param familiar Family used to perform the action
	 * @param space The space where the action was performed
	 */
	private void endAction(FamilyMember familiar, Space space){
		space.setFamiliar(familiar);
		player.getFreeMember().removeIf(member -> member == familiar);
		player.getEffects().removeIf(effect -> effect.getSource().equals(GC.ACTION_SPACE));
	}
	
	/**
	 * This method allows the player to show support to the Vatican. 
	 * If he does not have the requirements this method will automatically call the method
	 * dontShowVaticanSupport()
	 * @param age Age //TODO anche senza
	 * @throws GameException It will generally not be launched unless there are errors in xml
	 */
	public void showVaticanSupport(int age) throws GameException{
		GameInformation infoGame = game.getGameInformation();
		int faithPoints = player.getResource(GC.RES_FAITHPOINTS);
		if (faithPoints < 2 + age){
			dontShowVaticanSupport(age);
			return;
		}
		boolean answer = false;
		try {
			answer = player.getClient().getConnectionHandler().ask("Do you what to show vatican support?");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!answer){
			dontShowVaticanSupport(age);
			return;
		}
		player.pay(new Resource(GC.RES_FAITHPOINTS, faithPoints));
		int indexFaith = Math.min(faithPoints, infoGame.getBonusFaith().size() - 1);
		int victory = infoGame.getBonusFaith().get(indexFaith);
		player.gain(new Resource(GC.RES_VICTORYPOINTS, victory));
		activateEffect(GC.WHEN_SHOW_SUPPORT);
	}
	
	/**
	 * This method will be called by players who can not or do not want to show support to the Vatican
	 * @param age TODO, non dovrebbe ricevere in input niente
	 */
	public void dontShowVaticanSupport(int age){
		ExcommunicationTile tile = game.getBoard().getExCard()[age];
		Effect malus = tile.getEffect();
		player.addEffect(malus);
		//TODO GUI ?
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
			throw new GameException();
		player.removeLeaderCard(card);
		player.addEffect(card.getEffect());
		infoGame.addDiscardedLeader(card, player);
	}
	
	/**
	 * The method allows the player to discard a leader card to gain a council privilege
	 * @param card Leader card to discard
	 * @throws RemoteException 
	 */
	public void discardLeaderCard(LeaderCard card) throws RemoteException{
		player.removeLeaderCard(card);
		gain(new Resource(GC.RES_COUNCIL, 1));
	}
	
	/**
	 * Add player to a list because he has the delay first action malus
	 */
	public void addDelayMalus() {
		GameInformation infoGame = game.getGameInformation();
		infoGame.getTailPlayersTurn().add(player);
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
		index--;
		if (index < 0) 
			return;
		Resource finalReward = new Resource(GC.RES_VICTORYPOINTS, rewardList.get(index));
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
	
	//TODO description
	public Map<LeaderCard, Player> getDiscardedLeaderCards() {
		GameInformation infoGame = game.getGameInformation();
		return infoGame.getDiscardedLeader();
	}

	
	
}
