package game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import game.effect.Effect;
import game.state.*;

public class DynamicAction {
	
	//cambio player corrente con pattern observer
	
	protected Player player;
	private GameBoard board;
	
	public void setBoardForTestOnly(GameBoard board){
		this.board = board;
	}
	
	
	public DynamicAction(Player player){
		this.player = player;
	}
	
	
	public void activateEffect(String time){
		player.getEffects().stream()
			.forEach(eff -> eff.activateEffect(time));
	}
	
	public Object activateEffect(Object param, String time){
		for (Effect eff : player.getEffects())
			param = eff.activateEffect(time, param);
		return param;
	}
	
	public Object activateEffect(Object param, String string, String time){
		for (Effect eff : player.getEffects())
			param =  eff.activateEffect(time, param, string);
		return param;
	}

	public void gain (Resource res){
		if (res == null) return;
		res = (Resource) activateEffect(res, GC.WHEN_GAIN);
		player.gain(res);
	}
	
	public void gain (Effect source, Resource res){
		if (res == null) return;
		gain(res);
		if (source.getWhenActivate() == GC.IMMEDIATE)
			activateEffect(res, source.getSource(), GC.WHEN_GAIN);
	}
	
	public void startTurn(){
		activateEffect(GC.ONCE_PER_TURN);
	}
	
	public void readDices(){
		activateEffect(GC.WHEN_ROLL);
	}
	
	public void increaseWorker (FamilyMember familiar, int amount) throws GameException{
		int price = (Integer) activateEffect(amount, GC.WHEN_INCREASE_WORKER);
		player.pay(new Resource(GC.RES_SERVANTS, price));
		familiar.setValue(familiar.getValue() + amount);
	}
	
	/*
	public void canDicePaySpace (FamilyMember familiar, Space space) throws GameException{
		if (familiar.getValue() < space.getRequiredDiceValue())
			throw new GameException();
	}*/
	
	//tipo 3 monete
	public boolean playerHasToPayExtra (FamilyMember familiar, List<FamilyMember> placedFamiliar){
		long countTransparent = placedFamiliar.stream()
			.filter(fam -> fam.getOwner() == familiar.getOwner())
			.count();
		if(countTransparent > 0) return true;
		return false;
	}
	
	
	/**
	 * DA RIVEDERE I COMMENTI
	 * @param familiar
	 * @param space
	 * @param coloumn
	 * @return TRUE = DEVO PAGARE 3 MONETE, FALSE = NON DEVO
	 * @throws GameException andrebbe tolta
	 */
	public Resource findTaxToPay (Space space, int coloumn) throws GameException{
		Resource tax = new Resource();
		if (!board.getFamiliarInSameColoumn(coloumn).isEmpty()){
			tax.add(GC.TAX_TOWER);
			tax = (Resource) activateEffect(tax, GC.WHEN_PAY_TAX_TOWER);
		}
		return tax;
	}
	
	/**
	 * VERIFICA SE HO IL DADO ABBASTANZA GRANDE
	 * VERIFICA, SE LO SPAZIO E' SINGOLO, SE E' VUOTO O NO
	 * 			SE E' PIENO VERIFICA SE HO LUDOVICO ARIOSTO
	 * @param familiar
	 * @param space
	 * @throws GameException
	 */
	public void canOccupyForSpaceLogic(FamilyMember familiar, Space space) throws GameException{
		//if (familiar.getValue() < space.getRequiredDiceValue()) throw new GameException();
		//l'if qui sopra magari no
		if (space.isSingleObject()){
			if(space.getFamiliar().isEmpty()) return;
			Boolean x = true;
			x = (Boolean) activateEffect(x, GC.WHEN_JOINING_SPACE);
			if (x != null) throw new GameException();
		}
	}
	
	/**
	 * SI CREA UNA LISTA UNENDO GLI INPUT
	 * SI VERIFICA CHE LA LISTA POSSA SUSSISTERE
	 * @param familiar
	 * @param placedFamiliar
	 * @throws GameException
	 */
	public void canOccupyForColorLogic(FamilyMember familiar, List<FamilyMember> placedFamiliar) throws GameException{
		List<FamilyMember> playerFamiliar = new ArrayList<>();
		playerFamiliar.addAll(placedFamiliar);
		playerFamiliar.add(familiar);
		long countNotTransparent = playerFamiliar.stream()
			.filter(fam -> fam.getOwner() == familiar.getOwner())
			.filter(fam -> fam.getColor() != GC.FM_TRANSPARENT)
			.count();
		if(countNotTransparent > 1) throw new GameException();
	}
	

	public void canOccupySpace (FamilyMember familiar, Space space) throws GameException{
		canOccupyForSpaceLogic(familiar, space);
		canOccupyForColorLogic(familiar, space.getFamiliar());
	}
	
	public void placeInTowerStupidBigMethod(Integer value, int row, int coloumn) throws GameException {
		Resource cost = new Resource();
		Space space = board.getCell(row, coloumn);
		DevelopmentCard card = space.getCard();
		if (card == null || player.getDevelopmentCards(card.toString()).size() == GC.MAX_DEVELOPMENT_CARDS)
			throw new GameException();
		cost.add(findTaxToPay(space, coloumn));
		
		// qui dovrei chiedere al giocatore, se cost != null, se proseguire o no.
		
		/**
		* RESOURCE
		 */
		int index = 0;
		//ma potrebbe avere anche 2 tipi di costo
				
		tryToPayRequirement(card.toString(), card.getRequirement(index));
		tryToPayRequirement(card.toString(), getDashboardRequirement(card));
		
		Resource cardCost = card.getCost(index);
		cardCost = (Resource) activateEffect(cardCost, card.toString(), GC.WHEN_FIND_COST_CARD);
		cost.add(cardCost);
		
		/**
		 * DICE
		 */
		value = (Integer) activateEffect(value, card.toString(), GC.WHEN_FIND_VALUE_ACTION);
		
		if (value < space.getRequiredDiceValue()) throw new GameException();
		
		
		//canDicePaySpace(familiar, space);
		//canJoinSpace(familiar, space); //e quindi canJoinArraySpace(familiar, space);

		player.pay(cost);
		
		/**
		 * posso
		 */
		
		Effect spaceEffect = space.getInstantEffect();
		if(row > 1)
			spaceEffect = (Effect) activateEffect(spaceEffect, GC.WHEN_GET_TOWER_BONUS);
		player.addEffect(spaceEffect);
		
		player.addDevelopmentCard(card);
		space.setCard(null);
	}
	
	public void placeInTowerStupidBigMethod (FamilyMember familiar, int row, int coloumn) throws GameException{
		Space space = board.getCell(row, coloumn);
		canOccupySpace(familiar, space);
		canOccupyForColorLogic(familiar, board.getFamiliarInSameColoumn(coloumn));
		placeInTowerStupidBigMethod(familiar.getValue(), row, coloumn);
		space.setFamiliar(familiar);
	}
	
	public void tryToPayRequirement(String typeOfCard, Resource requirement) throws GameException{
		requirement = (Resource) activateEffect(requirement, typeOfCard, GC.WHEN_PAY_REQUIREMENT);
		player.pay(requirement);
		player.gain(requirement);
	}
	
	public Resource getDashboardRequirement(DevelopmentCard newCard){
		int index = player.getDevelopmentCards(newCard.toString()).size();
		int requirement = 0;
		if (newCard.toString() == GC.DEV_TERRITORY)
			requirement = GC.REQ_TERRITORY.get(index);
		return new Resource(GC.RES_MILITARYPOINTS, requirement);
	}
	
	//VERIFICA SE HO LA TESSERA SCOMINICA VIOLA
	public void canPlaceMarket () throws GameException{
		Boolean x = true;
		x = (Boolean) activateEffect(x, GC.WHEN_PLACE_FAMILIAR_MARKET);
		if (x == null) throw new GameException();
	}
	
	public void placeMarket (FamilyMember familiar, int whichSpace) throws GameException{
		canPlaceMarket();
		Space space = board.getMarketSpace(whichSpace);
		canOccupySpace(familiar, space);
		space.setFamiliar(familiar);
		player.addEffect(space.getInstantEffect());
	}
	
	public void placeTower (FamilyMember familiar, int row, int coloumn) throws GameException{
		Space space = board.getCell(row, coloumn);
		DevelopmentCard card = space.getCard();
		if (card == null) throw new GameException(); //vedi forum piazza
		
		//canDicePaySpace(familiar, space);
		//canJoinSpace(familiar, space); //e quindi canJoinArraySpace(familiar, space);
		Resource totalReq = new Resource();
		totalReq.add(card.getRequirement());
		space.setFamiliar(familiar);
		player.addEffect(space.getInstantEffect());
	}
	
	//verifica che non ci siano due familiari blu in una volta
	public void canJoinArraySpace (FamilyMember familiar, Space space) throws GameException{
		/*
		List<FamilyMember> list = space.getFamiliar();
		for (FamilyMember fam : list)
			if(fam.getOwner() == familiar.getOwner())
				throw new GameException();
				*/
		List<FamilyMember> playerFamiliar = space.getFamiliar().stream()
			.filter(fam -> fam.getOwner() == familiar.getOwner())
			.collect(Collectors.toList());
		if (!playerFamiliar.isEmpty()) throw new GameException();
	}
	
	public void placeCouncilPalace (FamilyMember familiar) throws GameException{
		Space space = board.getCouncilPalaceSpace();
		//canDicePaySpace(familiar, space);
		space.setFamiliar(familiar);
		player.addEffect(space.getInstantEffect());
		//devo ora mettere il proprietario del familiare in coda a proxturno
	}
	
	//fatto come se fosse Harvest
	public void placeWork (FamilyMember familiar, String action) throws GameException{
		int power = familiar.getValue();
		/**
		 * SPACE
		 */
		
		Space space = board.getWorkSpace(action);
		try{
			canOccupySpace(familiar, space);
		}
		catch (GameException e) {
			space = board.getWorkLongSpace(action);
			canOccupySpace(familiar, space);
		}
		//posso entrare in spazio
		//gestire il malus di -3 produzione
		space.getInstantEffect(); //TODO
		player.addEffect(space.getInstantEffect()); //TODO
		
		//if instanceof... lo si toglie?
		//OR
		//EFFETTI DRAFT CHE SI TOLGONO ALLA FINE DEL TURNO? geniale o non funziona?
		
		/**
		 * DICE
		 */
		int newPower = (Integer) activateEffect(power, action, GC.WHEN_FIND_VALUE_ACTION);
		if (newPower < 1) throw new GameException();
		launchesWork(power, action);
		space.setFamiliar(familiar);
	}
	
	public void launchesWork(Integer power, String action){
		switch(action){
			case GC.HARVEST : work(power, action, GC.DEV_TERRITORY);
			case GC.PRODUCTION : work(power, action, GC.DEV_BUILDING);
			default : return;
		}
	}
	
	private void work (int power, String action, String cards){
		int realActionPower = (Integer) activateEffect(power, action, GC.WHEN_FIND_VALUE_ACTION);
		player.getDevelopmentCards(cards).stream()
			.filter(card -> realActionPower >= card.getDice())
			.forEach(card -> player.addEffect(card.getPermanentEffect()));
	}
	
	public void pay (Resource res){
		if (res != null)
			System.out.println("i'm paying nothing");
	}
	
	public void showVaticanSupport(){
		activateEffect(GC.WHEN_SHOW_SUPPORT);
	}
	
	public void addDevelopmentCard (DevelopmentCard newCard) throws GameException{
		if (newCard.getCost() != null)
			pay(newCard.getCost());
		player.addDevelopmentCard(newCard);
	}
	
	//do per scontato che la carta sia del giocatore
	public void activateLeaderCard(LeaderCard card) throws GameException{
		if (!card.canPlayThis(player))
			throw new GameException();
		player.removeLeaderCard(card);
		player.addEffect(card.getEffect());
	}
	
	public void discardLeaderCard(LeaderCard card){
		player.removeLeaderCard(card);
		player.gain(new Resource(GC.RES_COUNCIL, 1));
	}
	
	public void endGame (){
		activateEffect(GC.WHEN_END);
		int territory = player.getDevelopmentCards(GC.DEV_TERRITORY).size();
		int character = player.getDevelopmentCards(GC.DEV_CHARACTER).size();
		int building = player.getDevelopmentCards(GC.DEV_BUILDING).size();
		int venture = player.getDevelopmentCards(GC.DEV_VENTURE).size();
		addFinalReward(GC.REW_TERRITORY, territory);
		addFinalReward(GC.REW_CHARACTER, character);
		exchangeResourceToVictory();
	}
	
	public void addFinalReward (List<Integer> rewardList, int index){
		Resource finalReward = new Resource(GC.RES_VICTORYPOINTS, rewardList.get(index));
		player.gain(finalReward);
		//player.gain(new Resource(GC.RES_VICTORYPOINTS, rewardList.get(index)));
	}
	

	/**
	 * At the end of the game, assign to the player victory points 
	 * based on his stock of resources.
	 */
	public void exchangeResourceToVictory(){
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


	
}
