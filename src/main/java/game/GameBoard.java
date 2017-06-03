package game;

import java.util.*;

import game.UserConfig;
import game.effect.Effect;
import game.state.StatePlaceFamiliar;


public class GameBoard {

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
	private Space[] market = new Space[MAX_MARKET_SPACE];
	
	//private int diceBlack;
	//private int diceOrange;
	//private int diceWhite;
	private int dices[] = new int[MAX_DICES];
	
	
	public GameBoard(UserConfig userConfig){
		
		this.userConfig = userConfig;
		Resource r1 = new Resource();
		market[0] = new Space(1, r1, true);
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
	
	public void market(int i, FamilyMember f) throws GameException{
		Player p = f.getOwner();
		market[i].setFamiliar(f);
		p.gain(market[i].getInstantBonus());
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

}

class Cell extends Space{
	
	private DevelopmentCard card;
	
	public Cell(DevelopmentCard card, int cost, Resource resource){
		super(cost, resource, true);
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
	private Resource instantBonus;
	private boolean singleObject;
	private ArrayList<FamilyMember> familiar;
	
	public Space(int cost, Resource resource, boolean singleObject) {
		requiredDiceValue = cost;
		instantBonus = resource;
		this.singleObject = singleObject;
	}
	
	public void setFamiliar(FamilyMember member){
		familiar.add(member);
	}

	public Resource getInstantBonus() {
		return instantBonus;
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
}