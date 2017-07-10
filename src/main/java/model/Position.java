package model;

import java.io.Serializable;

/**
 * This class identifies a specific position within the board, using a sort of coordinates for the purpose, like row or column
 * @author Matteo
 * @author Jacopo
 *
 */
public class Position implements Serializable{

	/**
	 * A default serial version ID to the selected type
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A word that identifies the position in the game board
	 */
	private String where;
	
	/**
	 * A index that identifies the row of the position in the game board
	 * It could be a generic index (e.g.) for market.
	 */
	private int row;
	
	/**
	 * A index that identifies the column of the position in the game board
	 */
	private int column;
	
	/**
	 * Constructor for positions that do not require other information, such as integers
	 * @param where A word that identifies the position in the game board
	 */
	public Position(String where){
		this.where = where;
		row = 0;
		column = 0;
	}
	
	/**
	 * Constructor for positions that require an index as information
	 * @param where A word that identifies the position in the game board
	 * @param index A index that identifies the position in the game board
	 */
	public Position(String where, int index){
		this(where);
		row = index;
	}
	
	/**
	 * Constructor for positions that require two integers as information
	 * @param where A word that identifies the position in the game board
	 * @param row A index that identifies the row of the position in the game board
	 * @param column A index that identifies the column of the position in the game board
	 */
	public Position(String where, int row, int column){
		this(where, row);
		this.column = column;
	}

	/**
	 * Getter: get the word that identifies the position in the game board
	 * @return Position in the game board
	 */
	public String getWhere() {
		return where;
	}

	/**
	 * Getter: get the row or the index that identifies the position in the game board
	 * @return The row or the index of the position
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Getter: get the column that identifies the position in the game board
	 * @return The column of the position
	 */
	public int getColumn() {
		return column;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		if(where.equals(GC.TOWER)){
			String[] towers = new String[]{
					"first","second","third","fourth"
			};
			
			String[] floors = new String[]{
					"ground","first","second","third"
			};
			
			sb.append("in the "+towers[column]+" tower at the "+floors[row]+" floor.");
		} else if(where.equals(GC.MARKET)){
			String[] spaces = new String[]{
					"first","second","third","fourth"
			};
			sb.append("at the market, in the "+ spaces[row]+"space.");
		} else if(where.equals(GC.COUNCIL_PALACE)) {
			sb.append("in the Council Palace.");
		} else {
			sb.append("in the "+where+" space.");
		}
		return sb.toString();
	}
	
}