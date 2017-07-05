package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import game.development.DevelopmentCard;
import game.effect.Effect;

/**
 * This class is designed for the game board of Lorenzo il Magnifico
 * @author M
 *
 */
public class GameBoard implements Serializable{
	
	/**
	 * A default serial version ID to the selected type
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Number of rows in the towers
	 */
	public static final int MAX_ROW = 4;
	
	/**
	 * Number of columns in the towers
	 */
	public static final int MAX_COLUMN = 4;
	
	/**
	 * Number of excommunication tiles in the game board
	 */
	public static final int MAX_EXCOMUNNICATION_CARD = 3;
	
	/**
	 * Number of dices
	 */
	public static final int MAX_DICES = 3;
	
	/**
	 * Number of market spaces
	 */
	public static final int MAX_MARKET_SPACE = 4;
	
	/**
	 * @Polymorphism
	 * Cells of the towers, like a pair <DevelopmentCard, Space>
	 */
	private Space[][] tower = new Cell[MAX_ROW][MAX_COLUMN];

	/**
	 * Excommunication tiles on the gameboard
	 */
	private ExcommunicationTile[] exCard = new ExcommunicationTile[MAX_EXCOMUNNICATION_CARD];
	
	/**
	 * Space of the council building
	 */
	private Space councilPalaceSpace;
	
	/**
	 * Little space of harvest
	 */
	private Space harvestPos;
	
	/**
	 * Little space of production
	 */
	private Space productionPos;
	
	/**
	 * Big space of harvest
	 */
	private Space harvestLongPos;
	
	/**
	 * Big space of production
	 */
	private Space productionLongPos;
	
	/**
	 * Market spaces
	 */
	private Space[] market = new Space[MAX_MARKET_SPACE];
	
	/**
	 * Dices on the board
	 */
	private Integer[] dices = new Integer[MAX_DICES];
	
	/**
	 * Constructor of the game board
	 * @param spaceBonus this param is readen from the xml file. 
	 * Is a table that contains simply to the left the types of action spaces as Strings 
	 * and to the right the corresponding effects of the single space
	 */
	public GameBoard(Map<String, List<Effect>> spaceBonus) {
		councilPalaceSpace = new Space(1, spaceBonus.get(GC.COUNCIL_PALACE).get(0), false);
		harvestPos = new Space(1, spaceBonus.get(GC.HARVEST).get(0), true);
		harvestLongPos = new Space(1, spaceBonus.get(GC.HARVEST).get(1), false);
		productionPos = new Space(1, spaceBonus.get(GC.PRODUCTION).get(0), true);
		productionLongPos = new Space(1, spaceBonus.get(GC.PRODUCTION).get(1), false);
		for (int index = 0; index < MAX_MARKET_SPACE; index++)
			market[index] = new Space(1, spaceBonus.get(GC.MARKET).get(index), true);
		int effectIndex = 0;
		for (int column = 0; column < MAX_COLUMN; column++)
			for(int row = MAX_ROW - 1; row >= 0; row--){
				int cost = 2 * row + 1;
				Effect effect = spaceBonus.get(GC.TOWER).get(effectIndex);
				tower[row][column] = new Cell(cost, effect);
				effectIndex++;
			}		
	}
	
	/**
	 * Generate the cards in the towers using a deck of development cards
	 * @param cards Deck of development cards
	 * @param age Current age
	 */
	public void generateDevelopmentCards(List<DevelopmentCard> cards, int age){
		for (int column = 0; column < MAX_COLUMN; column++)
			for(int row = 0; row < MAX_ROW; row++)
				putCard(cards, age, row, column);
	}
	
	/**
	 * Private method because is not never outside
	 * Take a card from a deck and put it in the specific cell selected
	 * @param cards Deck of development cards
	 * @param age Current age
	 * @param row Row selected
	 * @param column Column select
	 */
	private void putCard(List<DevelopmentCard> cards, int age, int row, int column){
		for(DevelopmentCard card : cards)
			if (card.toString() == GC.DEV_TYPES.get(column) && card.getAge() == age){
				tower[row][column].setCard(card);
				cards.removeIf(elem -> elem == card);
				return;
			}
	}
	
	/**
	 * Clear all spaces, deleting cards and emptying all lists of familiars
	 */
	public void clearPos(){
		councilPalaceSpace.getFamiliars().clear();
		harvestPos.getFamiliars().clear();
		harvestLongPos.getFamiliars().clear();
		productionPos.getFamiliars().clear();
		productionLongPos.getFamiliars().clear();
		for (int index = 0; index < MAX_MARKET_SPACE; index++)
			market[index].getFamiliars().clear();
		for (int column = 0; column < MAX_COLUMN; column++)
			for(int row = 0; row < MAX_ROW; row++)
				tower[row][column].getFamiliars().clear();
	}
	
	/**
	 * Getter: get a card from a specific space in the tower
	 * @param row Row selected
	 * @param column Column selected
	 * @return the space selected
	 */
	public DevelopmentCard getCard(int row, int column){
		return getFromTowers(row,column).getCard();
	}

	/**
	 * @Polymorphism space -> cell
	 * Getter: get the space of a specific position in the towers
	 * @param row Row selected
	 * @param column Column selected
	 * @return the space selected
	 */
	public Space getFromTowers(int row, int column){
		return tower[row][column];
	}
	
	/**
	 * Getter: get the council building big space
	 * @return council building big space
	 */
	public Space getCouncilPalaceSpace(){
		return councilPalaceSpace;
	}
	
	/**
	 * Getter: get a specific market space
	 * @param whichSpace selection
	 * @return space selected
	 */
	public Space getMarketSpace(int whichSpace){
		return market[whichSpace];
	}
	
	/**
	 * Getter: get dices on the board	
	 * @return dices on the board
	 */
	public Integer[] getDices() {
		return dices;
	}

	/**
	 * Setter: set dices on the board
	 * @param dices dices on the board
	 */
	public void setDices(Integer[] dices) {
		this.dices = dices;
	}
	
	/**
	 * Getter: get excommunication tiles on the board
	 * @return exCard excommunication tiles
	 */
	public ExcommunicationTile[] getExCard() {
		return exCard;
	}
	
	/**
	 * Setter: set excommunication tiles on the board
	 * @param exCard excommunication tiles
	 */
	public void setExCard(ExcommunicationTile[] exCard) {
		this.exCard = exCard;
	}
	
	/**
	 * Get a little work space depending on the action
	 * @param action generally harvest or production
	 * @return little space selected
	 */
	public Space getWorkSpace(String action){
		switch(action){
			case GC.HARVEST : return harvestPos;
			case GC.PRODUCTION : return productionPos;
			default : return null;
		}
	}
	
	/**
	 * Get a big work space depending on the action
	 * @param action generally harvest or production
	 * @return big space selected
	 */
	public Space getWorkLongSpace(String action){
		switch(action){
			case GC.HARVEST : return harvestLongPos;
			case GC.PRODUCTION : return productionLongPos;
			default : return null;
		}
	}
	
	/**
	 * Get a list of familiars in a specific column of the tower
	 * @param column Column of the tower
	 * @return List of familiars in that column of the tower
	 */
	public List<FamilyMember> getFamiliarInSameColumn(int column){
		List<FamilyMember> familiarInSameColumn = new ArrayList<>();
		for (int row = 0; row < GameBoard.MAX_ROW; row++)
			familiarInSameColumn.addAll(getFromTowers(row, column).getFamiliars());
		return familiarInSameColumn;
	}

}