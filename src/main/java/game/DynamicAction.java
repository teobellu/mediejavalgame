package game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import game.effect.Effect;
import game.effect.when.*;
import game.state.*;

public class DynamicAction {
	
	//cambio player corrente con pattern observer
	
	protected Player player;
	private GameBoard board;
	
	public DynamicAction(Player player){
		this.player = player;
	}
	
	
	public void activateEffect(Effect effect){
		player.getEffects().stream()
			.forEach(eff -> effect.effect(eff));
	}
	
	public Object activateEffect(Object param, Effect effect){
		for (Effect eff : player.getEffects())
			param = effect.effect(param, eff);
		return param;
	}
	
	public Object activateEffect(Object param, String string, Effect effect){
		for (Effect eff : player.getEffects())
			param = effect.effect(param, string, eff);
		return param;
	}

	public void gain (Resource res){
		res = (Resource) activateEffect(res, new EffectWhenGain(null));
		player.gain(res);
	}
	
	public void increaseWorker (FamilyMember familiar, int amount) throws GameException{
		Resource pay = new Resource();
		int increase = amount;
		
		amount = (Integer) activateEffect(amount, new EffectWhenIncreaseWorker(null));
		pay.add(GameConstants.RES_SERVANTS, amount);
		player.pay(pay);
		familiar.setValue(familiar.getValue() + increase);
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
	 * @throws GameException NON POSSO PIAZZARE
	 */
	public Resource findTaxToPay (FamilyMember familiar, Space space, int coloumn) throws GameException{
		Resource tax = new Resource();
		canOccupySpace(familiar, space);
		List<FamilyMember> familiarInSameColoumn = new ArrayList<>();
		for (int row = 0; row < GameBoard.MAX_ROW; row++)
			familiarInSameColoumn.addAll(board.getCell(row, coloumn).getFamiliar());
		canOccupyForColorLogic(familiar, familiarInSameColoumn);
		if (!familiarInSameColoumn.isEmpty()){
			tax.add(GameConstants.TAX_TOWER);
			tax = (Resource) activateEffect(tax, new EffectWhenPayTaxTower(null));
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
		if (familiar.getValue() < space.getRequiredDiceValue()) throw new GameException();
		//l'if qui sopra magari no
		if (space.isSingleObject()){
			if(space.getFamiliar().isEmpty()) return;
			activateEffect(new EffectWhenJoiningSpace(null));
			if (!player.isCheck()) throw new GameException();
			player.setCheck(false);	
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
		List<FamilyMember> playerFamiliar = placedFamiliar;
		playerFamiliar.add(familiar);
		long countNotTransparent = playerFamiliar.stream()
			.filter(fam -> fam.getOwner() == familiar.getOwner())
			.filter(fam -> fam.getColor() != GameConstants.FM_TRANSPARENT)
			.count();
		if(countNotTransparent > 1) throw new GameException();
	}
	

	public void canOccupySpace (FamilyMember familiar, Space space) throws GameException{
		canOccupyForSpaceLogic(familiar, space);
		canOccupyForColorLogic(familiar, space.getFamiliar());
	}
	
	/**
	 * TODO
	 * NON CANCELLARE PER IL MOMENTO ******************************************
	 * METODO STUPIDO DA SMEZZARE E O CANCELLARE
	 * @param familiar
	 * @param row
	 * @param coloumn
	 * @throws GameException 
	 */
	public void placeInTowerStupidBigMethod (FamilyMember familiar, int row, int coloumn) throws GameException{
		Resource cost = new Resource();
		Space space = board.getCell(row, coloumn);
		DevelopmentCard card = space.getCard();
		if (card == null) throw new GameException(); //vedi forum piazza
		cost.add(findTaxToPay(familiar, space, coloumn)); //aggiungo o no le 3 monete + controlli
		int value = familiar.getValue();
		value = (Integer) activateEffect(value, card.toString(), new EffectWhenFindValueAction(null));
		int index = 0;
		//ma potrebbe avere anche 2 tipi di costo
		cost.add(card.getRequirement(index));
		
		//canDicePaySpace(familiar, space);
		//canJoinSpace(familiar, space); //e quindi canJoinArraySpace(familiar, space);
		Resource totalReq = new Resource();
		totalReq.add(card.getRequirement());
		space.setFamiliar(familiar);
		gain(space.getInstantBonus());
	}
	
	/*
	public void canJoinSpace (FamilyMember familiar, Space space) throws GameException{
		if (space.getFamiliar().isEmpty()) return;
		player.getEffects().stream()
			.forEach(effect -> effect.effect(new StateJoiningSpace(null)));
		if (!player.isCheck()) throw new GameException();
		player.setCheck(false);
		canJoinArraySpace(familiar, space);
	}*/
	
	//VERIFICA SE HO LA TESSERA SCOMINICA VIOLA
	public void canPlaceMarket () throws GameException{
		activateEffect(new EffectWhenPlaceFamiliarMarket(null));
		if(!player.isCheck()) return;
		player.setCheck(false);
		throw new GameException();
	}
	
	public void placeMarket (FamilyMember familiar, int whichSpace) throws GameException{
		canPlaceMarket();
		Space space = board.getMarketSpace(whichSpace);
		//canDicePaySpace(familiar, space);
		canOccupySpace(familiar, space);
		space.setFamiliar(familiar);
		gain(space.getInstantBonus());
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
		gain(space.getInstantBonus());
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
	
	public void getYield (FamilyMember familiar, Space space) throws GameException{
		//canDicePaySpace(familiar, space);
		canJoinArraySpace(familiar, space);
		if(space.getFamiliar().isEmpty()){
			//do harvest max power
		}
		else
			//do harvest less power //potrei avere ludovico arisoto ?
		space.setFamiliar(familiar);
		gain(space.getInstantBonus());
	}
	
	public void placeHarvest (FamilyMember familiar) throws GameException{
		Space space = board.getHarvestSpace();
		//canDicePaySpace(familiar, space);
		canJoinArraySpace(familiar, space);
		if(space.getFamiliar().isEmpty()){
			//do harvest max power
		}
		else
			//do harvest less power
		space.setFamiliar(familiar);
		gain(space.getInstantBonus());
	}
	
	public void placeCouncilPalace (FamilyMember familiar) throws GameException{
		Space space = board.getCouncilPalaceSpace();
		//canDicePaySpace(familiar, space);
		space.setFamiliar(familiar);
		gain(space.getInstantBonus());
		//devo ora mettere il proprietario del familiare in coda a proxturno
	}
	
	public int placeValuePower (int power, String action){
		System.out.println(action + power);
		power = (Integer) activateEffect(power, action,  new EffectWhenFindValueAction(null));
		System.out.println(action + power);
		return power;
	}
	
	public int getRealActionValuePower (Integer power, String action){
		System.out.println(action + power);
		power = (Integer) activateEffect(power, action, new EffectWhenFindValueAction(null));
		/*for (Effect effect : player.getEffects()){
			power = (Integer) effect.effect(power, action,  new EffectWhenFindValueAction(null));
		}*/
		System.out.println(action + power);
		return power;
	}
	
	
	public void harvest (int power){
		System.out.println("harv " + power);
		power = getRealActionValuePower(power, "harvest");
		System.out.println("harv " + power);
	}
	
	public void product (int power){
		System.out.println("prod " + power);
		power = getRealActionValuePower(power, "production");
		System.out.println("prod " + power);
	}
	
	public void pay (Resource res){
		
	}
	
	public void addDevelopmentCard (DevelopmentCard newCard) throws GameException{
		if (newCard.getCost() != null)
			pay(newCard.getCost());
		player.addDevelopmentCard(newCard);
	}
	
	public void endGame (){
		activateEffect(new EffectWhenEnd(null));
		
		System.out.println(player.getDevelopmentCards(GameConstants.DEV_TERRITORY).size());
		System.out.println(player.getDevelopmentCards(GameConstants.DEV_CHARACTER).size());
		System.out.println(player.getDevelopmentCards(GameConstants.DEV_BUILDING).size());
		System.out.println(player.getDevelopmentCards(GameConstants.DEV_VENTURE).size());
	}
}
