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
	private boolean check;
	
	public DynamicAction(Player player){
		this.player = player;
		check = false;
	}

	public void gain (Resource res){
		for (Effect effect : player.getEffects())
			res = (Resource) effect.effect(res, new StateGaining(null));
		player.gain(res);
	}
	
	public void increaseWorker (FamilyMember familiar, int amount) throws GameException{
		Resource pay = new Resource();
		int increase = amount;
		for (Effect effect : player.getEffects())
			amount = (Integer) effect.effect(amount, new StateIncreaseWorker(null));
		pay.add(Resource.SERVANTS, amount);
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
	
	//overloading
	public void canOccupyTowerSpace (FamilyMember familiar, Space space, int coloumn) throws GameException{
		canOccupySpace(familiar, space);
		List<FamilyMember> familiarInSameColoumn = new ArrayList<>();
		for (int row = 0; row < GameBoard.MAX_ROW; row++)
			familiarInSameColoumn.addAll(board.getCell(row, coloumn).getFamiliar());
		canOccupyForColorLogic(familiar, familiarInSameColoumn);
		boolean hasToPayExtra = playerHasToPayExtra(familiar, familiarInSameColoumn);
	}
	
	public void canOccupyForSpaceLogic(FamilyMember familiar, Space space) throws GameException{
		if (familiar.getValue() < space.getRequiredDiceValue()) throw new GameException();
		if (space.isSingleObject()){
			if(space.getFamiliar().isEmpty()) return;
			player.getEffects().stream()
				.forEach(effect -> effect.effect(new StateJoiningSpace(null)));
			if (!player.isCheck()) throw new GameException();
			player.setCheck(false);	
		}
	}
	
	public void canOccupyForColorLogic(FamilyMember familiar, List<FamilyMember> placedFamiliar) throws GameException{
		List<FamilyMember> playerFamiliar = placedFamiliar;
		playerFamiliar.add(familiar);
		long countNotTransparent = playerFamiliar.stream()
			.filter(fam -> fam.getOwner() == familiar.getOwner())
			.filter(fam -> fam.getColor() != GameContants.FM_TRANSPARENT)
			.count();
		if(countNotTransparent > 1) throw new GameException();
	}
	
	public void canOccupySpace (FamilyMember familiar, Space space) throws GameException{
		canOccupyForSpaceLogic(familiar, space);
		canOccupyForColorLogic(familiar, space.getFamiliar());
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
	
	public void canPlaceMarket () throws GameException{
		player.getEffects().stream()
			.forEach(effect -> effect.effect(new StatePlaceFamiliarMarket(null)));
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
		for (Effect effect : player.getEffects()){
			power = (Integer) effect.effect(power, action,  new StateJoiningSpace(null));
		}
		System.out.println(action + power);
		return power;
	}
	
	public int getRealActionValuePower (int power, String action){
		System.out.println(action + power);
		for (Effect effect : player.getEffects()){
			power = (Integer) effect.effect(power, action,  new StateJoiningSpace(null));
		}
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
	
	public void placeInSpace (FamilyMember familiar, Space space) throws GameException{
		//kill this
		if (familiar.getValue() >= space.getRequiredDiceValue()){
			if (space.getFamiliar().isEmpty())
				check = true;
			for (Effect effect : player.getEffects())
				check = true;
			if (check){
				gain(space.getInstantBonus());
				space.setFamiliar(familiar);
				return;
			}
		}
		boolean isPossible = false;
	}
	
	public void pay (Resource res){
		
	}
	
	public void addDevelopmentCard (DevelopmentCard newCard) throws GameException{
		if (newCard.getCost() != null)
			pay(newCard.getCost());
		player.addDevelopmentCard(newCard);
	}
	
	public void endGame (){
		player.getEffects().stream()
			.forEach(effect -> effect.effect(new StateEndingGame(null)));
		
		System.out.println(player.getDevelopmentCards(GameContants.DEV_TERRITORY).size());
		System.out.println(player.getDevelopmentCards(GameContants.DEV_CHARACTER).size());
		System.out.println(player.getDevelopmentCards(GameContants.DEV_BUILDING).size());
		System.out.println(player.getDevelopmentCards(GameContants.DEV_VENTURE).size());
	}
}
