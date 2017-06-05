package game;

import java.util.*;

import game.UserConfig;
import game.effect.Effect;
import game.effect.behaviors.EffectGetResource;
import game.state.StatePlaceFamiliar;


public class GameBoard {

	/**da cancellare**/
	public static final Resource ser3 = new Resource(GC.RES_SERVANTS, 3);
	public static final Resource ser2 = new Resource(GC.RES_SERVANTS, 2);
	public static final Resource ser1 = new Resource(GC.RES_SERVANTS, 1);
	public static final Resource sto1 = new Resource(GC.RES_STONES, 1);
	public static final Resource coi3 = new Resource(GC.RES_COINS, 3);
	public static final Resource vic3 = new Resource(GC.RES_VICTORYPOINTS, 3);
	public static final Resource vic1 = new Resource(GC.RES_VICTORYPOINTS, 1);
	public static final Resource mil3 = new Resource(GC.RES_MILITARYPOINTS, 3);
	
	public static final int MAX_ROW = 4;
	public static final int MAX_COLOUMN = 4;
	private static final int MAX_EXCOMUNNICATION_CARD = 3;
	private static final int MAX_DICES = 3;
	private static int MAX_MARKET_SPACE = 4;
	
	private final UserConfig userConfig;
	
	//O posso col polimorfismo?
	private Cell[][] tower = new Cell[MAX_ROW][MAX_COLOUMN];

	private ExcommunicationCard[] exCard = new ExcommunicationCard[MAX_EXCOMUNNICATION_CARD];
	
	private Space councilPalaceSpace;
	private Space harvestPos;
	private Space productionPos;
	
	private Space harvestLongPos;
	private Space productionLongPos;
	
	
	private Space[] market = new Space[MAX_MARKET_SPACE];
	
	//private int diceBlack;
	//private int diceOrange;
	//private int diceWhite;
	private int dices[] = new int[MAX_DICES];
	
	
	
	public GameBoard(UserConfig userConfig){
		
		this.userConfig = userConfig;
		Resource r1 = new Resource();
		simulator();
	}
	
	//TODO da cancellare in futuro, serve solo per test
	public void simulator(){
		
		DevelopmentCard terr0 = new Territory();
		DevelopmentCard terr1 = new Territory();
		DevelopmentCard terr2 = new Territory();
		DevelopmentCard terr3 = new Territory();
		
		terr0.setRequirement(ser1);
		
		terr0.setCost(sto1);
		terr1.setCost(sto1);
		terr2.setCost(sto1);
		terr3.setCost(sto1);
		
		tower[0][0] = new Cell(terr0, 1, null);
		tower[1][0] = new Cell(terr1, 3, null);
		tower[2][0] = new Cell(terr2, 5, 
				new Effect(GC.IMMEDIATE, new EffectGetResource(new Resource(GC.RES_WOOD, 1))));
		tower[3][0] = new Cell(terr3, 7,  
				new Effect(GC.IMMEDIATE, new EffectGetResource(new Resource(GC.RES_WOOD, 2))));
		
		
	}
	
	public void refresh(){
		clearPos();
		roll();
	}
	
	public void roll(){
		for (int i = 0; i < dices.length; i++)
			dices[i] = (int)(Math.random()*6) + 1;
	}
	
	public int[] getdices(){
		return dices;
	}
	
	public void clearPos(){
		//TODO
	}
	
	/*
	public boolean canPutFamiliar(Player player, Space space){
		
	}*/
	
	public boolean canGetCard(Player player, int row, int coloumn){
		row--; coloumn--;
		if (row < MAX_ROW && coloumn < MAX_COLOUMN && row >= 0 && coloumn >= 0) return false;
		if (tower[row][coloumn].getCard() == null) return false;
		//if ()
		return true;
	}
	
	public DevelopmentCard getCard(int row, int coloumn) throws GameException{
		return getCell(row,coloumn).getCard();
	}
	
	public void obtainCard(Player player, int row, int coloumn) throws GameException{
		//vari controlli, ottieni bonus, ecc.
		DevelopmentCard card = getCell(row, coloumn).getCard();
		player.addDevelopmentCard(card);
		
	}

	public Cell getCell(int row, int coloumn) throws GameException {
		//servono davvero controlli?
		if (row >= MAX_ROW || coloumn >= MAX_COLOUMN) throw new GameException();
		if (row < 0 || coloumn < 0) throw new GameException();
		return tower[row][coloumn];
	}
	
	public Space getHarvestSpace(){
		return harvestPos;
	}
	
	public Space getProductionSpace(){
		return productionPos;
	}
	
	public Space getCouncilPalaceSpace(){
		return councilPalaceSpace;
	}
	
	//ok
	public Space getMarketSpace(int whichSpace) throws GameException {
		if (whichSpace < 0 || whichSpace >= MAX_MARKET_SPACE) throw new GameException();
		return market[whichSpace];
	}
	
	public ExcommunicationCard[] getExCard() {
		return exCard;
	}
	
	public int[] getDices() {
		return dices;
	}

	public void setDices(int[] dices) {
		this.dices = dices;
	}

	public void setExCard(ExcommunicationCard[] exCard) {
		this.exCard = exCard;
	}

	public Space getHarvestLongPos() {
		return harvestLongPos;
	}

	public void setHarvestLongPos(Space harvestLongPos) {
		this.harvestLongPos = harvestLongPos;
	}

	public Space getProductionLongPos() {
		return productionLongPos;
	}

	public void setProductionLongPos(Space productionLongPos) {
		this.productionLongPos = productionLongPos;
	}
	
	//TODO magari rimuovere gameexception
	public List<FamilyMember> getFamiliarInSameColoumn(int coloumn) throws GameException{
		List<FamilyMember> familiarInSameColoumn = new ArrayList<>();
		for (int row = 0; row < GameBoard.MAX_ROW; row++)
			familiarInSameColoumn.addAll(getCell(row, coloumn).getFamiliar());
		return familiarInSameColoumn;
	}
	

}

class Cell extends Space{
	
	private DevelopmentCard card;
	
	public Cell(DevelopmentCard card, int cost, Effect instantEffect){
		super(cost, instantEffect, true);
		this.card = card;
	}
	
	/*
	@Override //del metodo sotto
	public void setFamiliar(FamilyMember member){
		//prima di piazzare devo vedere se posso pagare, e pago
		Resource cost = card.getCost();
		//ora piazzo il familiare
		super.setFamiliar(member);
	}*/
	
	@Override
	public void setCard(DevelopmentCard card) {
		this.card = card;
	}

	@Override
	public DevelopmentCard getCard(){
		return card;
	}

}

class Space{
	
	private int requiredDiceValue;
	private Effect instantEffect;
	private boolean singleObject;
	private ArrayList<FamilyMember> familiar;
	
	public Space(int cost, Effect instantEffect, boolean singleObject) {
		requiredDiceValue = cost;
		this.instantEffect = instantEffect;
		this.singleObject = singleObject;
		familiar = new ArrayList<>();
	}
	
	public void setFamiliar(FamilyMember member){
		familiar.add(member);
	}

	public int getRequiredDiceValue() {
		return requiredDiceValue;
	}

	public ArrayList<FamilyMember> getFamiliar() {
		return familiar;
	}

	public boolean isSingleObject() {
		return singleObject;
	}

	public void setSingleObject(boolean singleObject) {
		this.singleObject = singleObject;
	}
	
	public void setCard(DevelopmentCard card) {
		return;
	}

	public DevelopmentCard getCard() {
		return null;
	}

	public Effect getInstantEffect() {
		return instantEffect;
	}
}