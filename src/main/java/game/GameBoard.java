package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import exceptions.GameException;
import game.development.DevelopmentCard;
import game.effect.Effect;
import game.effect.behaviors.EffectGetResource;
import game.effect.behaviors.EffectIncreaseActionPower;



public class GameBoard implements Serializable{

	

	/**da cancellare**/
	public static final Resource ser3 = new Resource(GC.RES_SERVANTS, 3);
	public static final Resource ser2 = new Resource(GC.RES_SERVANTS, 2);
	public static final Resource ser1 = new Resource(GC.RES_SERVANTS, 1);
	public static final Resource sto1 = new Resource(GC.RES_STONES, 1);
	public static final Resource coi3 = new Resource(GC.RES_COINS, 3);
	public static final Resource vic3 = new Resource(GC.RES_VICTORYPOINTS, 3);
	public static final Resource vic1 = new Resource(GC.RES_VICTORYPOINTS, 1);
	public static final Resource mil3 = new Resource(GC.RES_MILITARYPOINTS, 3);
	
	public static final Effect eff = new Effect(GC.IMMEDIATE, new EffectGetResource(mil3));
	
	public static final int MAX_ROW = 4;
	public static final int MAX_COLUMN = 4;
	private static final int MAX_EXCOMUNNICATION_CARD = 3;
	protected static final int MAX_DICES = 3;
	private static final int MAX_MARKET_SPACE = 4;
	
	//TODO O posso col polimorfismo?
	private Cell[][] tower = new Cell[MAX_ROW][MAX_COLUMN];

	private ExcommunicationTile[] exCard = new ExcommunicationTile[MAX_EXCOMUNNICATION_CARD];
	
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
	
	public GameBoard(){
		
		Resource r1 = new Resource();
		Effect x = new Effect(GC.WHEN_FIND_VALUE_ACTION, new EffectIncreaseActionPower(GC.HARVEST, -3));
		x.setSource(GC.ACTION_SPACE);
		
		councilPalaceSpace = new Space(1, null, false);
		
		market[0] = new Space(1, null, true);
		harvestPos = new Space(1, null, true);
		productionPos = new Space(1, null, true);
		harvestLongPos = new Space(1, x, false);
		productionLongPos = new Space(1, null, false);
	}
	
	public GameBoard(Map<String, List<Effect>> spaceBonus) {
		councilPalaceSpace = new Space(1, spaceBonus.get(GC.COUNCIL_PALACE).get(0), false);
		harvestPos = new Space(1, spaceBonus.get(GC.HARVEST).get(0), true);
		harvestLongPos = new Space(1, spaceBonus.get(GC.HARVEST).get(1), false);
		productionPos = new Space(1, spaceBonus.get(GC.PRODUCTION).get(0), true);
		productionLongPos = new Space(1, spaceBonus.get(GC.PRODUCTION).get(1), false);
		for (int index = 0; index < MAX_MARKET_SPACE; index++)
			market[index] = new Space(1, spaceBonus.get(GC.COUNCIL_PALACE).get(index), true);
		int effectIndex = 0;
		for (int column = 0; column < MAX_COLUMN; column++)
			for(int row = MAX_ROW - 1; row >= 0; row--){
				int cost = 2 * row + 1;
				Effect effect = spaceBonus.get(GC.TOWER).get(effectIndex);
				tower[row][column] = new Cell(cost, effect);
				effectIndex++;
			}		
	}
	
	public void generateDevelopmentCards(List<DevelopmentCard> cards, int age){
		for (int column = 0; column < MAX_COLUMN; column++)
			for(int row = 0; row < MAX_ROW; row++)
				putCard(cards, age, row, column);
	}
	
	private void putCard(List<DevelopmentCard> cards, int age, int row, int column){
		for(DevelopmentCard card : cards)
			if (card.toString() == GC.DEV_TYPES.get(column) && card.getAge() == age){
				tower[row][column].setCard(card);
				cards.removeIf(elem -> elem == card);
				return;
			}
	}
	
	public void refresh(){
		clearPos();
		roll();
	}
	
	public void roll(){
		for (int dice : dices)
			dice = (int)(Math.random()*6) + 1;
	}
	
	public int[] getdices(){
		return dices;
	}
	
	public void clearPos(){
		//TODO
	}
	
	public boolean canGetCard(Player player, int row, int column){
		row--; 
		column--;
		if (row < MAX_ROW && column < MAX_COLUMN && row >= 0 && column >= 0) 
			return false;
		if (tower[row][column].getCard() == null) 
			return false;
		return true;
	}
	
	public DevelopmentCard getCard(int row, int column) throws GameException{
		return getCell(row,column).getCard();
	}
	
	public void obtainCard(Player player, int row, int column) throws GameException{
		//vari controlli, ottieni bonus, ecc.
		DevelopmentCard card = getCell(row, column).getCard();
		player.addDevelopmentCard(card);
		
	}

	public Cell getCell(int row, int column) throws GameException {
		//servono davvero controlli?
		if (row >= MAX_ROW || column >= MAX_COLUMN) throw new GameException();
		if (row < 0 || column < 0) throw new GameException();
		return tower[row][column];
	}
	
	
	
	public Space getCouncilPalaceSpace(){
		return councilPalaceSpace;
	}
	
	//ok
	public Space getMarketSpace(int whichSpace) throws GameException {
		if (whichSpace < 0 || whichSpace >= MAX_MARKET_SPACE) throw new GameException();
		return market[whichSpace];
	}
	
	public ExcommunicationTile[] getExCard() {
		return exCard;
	}
	
	public int[] getDices() {
		return dices;
	}

	public void setDices(int[] dices) {
		this.dices = dices;
	}

	public void setExCard(ExcommunicationTile[] exCard) {
		this.exCard = exCard;
	}
	
	public Space getWorkSpace(String action){
		switch(action){
			case GC.HARVEST : return harvestPos;
			case GC.PRODUCTION : return productionPos;
			default : return null;
		}
	}
	
	public Space getWorkLongSpace(String action){
		switch(action){
			case GC.HARVEST : return harvestLongPos;
			case GC.PRODUCTION : return productionLongPos;
			default : return null;
		}
	}
	
	//TODO magari rimuovere gameexception
	public List<FamilyMember> getFamiliarInSameColumn(int column) throws GameException{
		List<FamilyMember> familiarInSameColumn = new ArrayList<>();
		for (int row = 0; row < GameBoard.MAX_ROW; row++)
			familiarInSameColumn.addAll(getCell(row, column).getFamiliar());
		return familiarInSameColumn;
	}

}

class Cell extends Space{
	
	private DevelopmentCard card;
	
	public Cell(int cost, Effect instantEffect){
		super(cost, instantEffect, true);
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
		if (instantEffect != null)
			instantEffect.setSource(GC.ACTION_SPACE);
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