package game;

import java.util.*;

import game.UserConfig;
import game.Resource.type;


public class GameBoard {
	
	public static final int MAX_ROW = 4;
	public static final int MAX_COLOUMN = 4;
	private static final int MAX_EXCOMUNNICATION_CARD = 3;
	
	private final UserConfig userConfig;
	
	private Cell[][] tower = new Cell[MAX_ROW][MAX_COLOUMN];
	/*
	private ArrayList <Cell> territoryCol = new ArrayList <Cell>();
	private ArrayList <Cell> characterCol = new ArrayList <Cell>();
	private ArrayList <Cell> buildingCol = new ArrayList <Cell>();
	private ArrayList <Cell> ventureCol = new ArrayList <Cell>();
	*/
	private ExcommunicationCard[] exCard = new ExcommunicationCard[MAX_EXCOMUNNICATION_CARD];
	
	private ArrayList <Space> turnPos = new ArrayList <Space>();
	private ArrayList <Space> harvestPos = new ArrayList <Space>();
	private ArrayList <Space> productionPos = new ArrayList <Space>();
	private Space[] market = new Space[4];
	
	//private int diceBlack;
	//private int diceOrange;
	//private int diceWhite;
	private int dices[] = new int[3];
	
	//i player sono salvati in un'altra classe
	public Player TEST;
	
	public GameBoard(UserConfig userConfig){
		
		this.userConfig = userConfig;
		Resource r1 = new Resource();
		r1.add(type.COINS, 5);
		market[0] = new Space(1, r1);
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
		//TODO towers
		turnPos.clear();
		harvestPos.clear();
		productionPos.clear();
		market = null;
	}
	
	public void market(int i, FamilyMember f) throws GameException{
		Player p = f.getOwner();
		market[i].setFamiliar(f);
		p.controlGain(market[i].getInstantBonus());
	}
	
	public DevelopmentCard getCard(int row, int coloumn){
		return getCell(row,coloumn).getCard();
	}
	
	public void obtainCard(Player player, int row, int coloumn){
		//vari controlli, ottieni bonus, ecc.
		DevelopmentCard card = getCell(row, coloumn).getCard();
		try {
			player.addDevelopmentCard(card);
		} catch (GameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Cell getCell(int row, int coloumn) {
		//servono davvero controlli?
		if (row < MAX_ROW && coloumn < MAX_COLOUMN) 
			return tower[row][coloumn];
		else return null;
	}
	
}

class Cell extends Space{
	
	private DevelopmentCard card;
	
	public Cell(DevelopmentCard card, int cost, Resource resource){
		super(cost, resource);
		this.card = card;
	}
	
	@Override //del metodo sotto
	public void setFamiliar(FamilyMember member) throws GameException{
		//prima di piazzare devo vedere se posso pagare, e pago
		Resource cost = card.getCost();
		member.getOwner().controlPay(cost);
		//ora piazzo il familiare
		super.setFamiliar(member);
	}
	
	public DevelopmentCard getCard(){
		return card;
	}

}

class Space{
	
	private int requiredDiceValue;
	private Resource instantBonus;
	
	public Resource getInstantBonus() {
		return instantBonus;
	}

	private ArrayList<FamilyMember> familiar;
	
	public Space(int cost, Resource resource) {
		requiredDiceValue = cost;
		instantBonus = resource;
	}
	
	public void setFamiliar(FamilyMember member) throws GameException{
		if (member.getValue() >= requiredDiceValue)
			if (familiar.isEmpty()){
				member.getOwner().controlGain(instantBonus);
				familiar.add(member);
			}
			else throw new GameException();	
	}
}