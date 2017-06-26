package game;

import java.io.Serializable;

public class Position implements Serializable{

	/**
	 * A word that identifies the position in the game board
	 */
	private String where;
	
	private int row;
	
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
	
	public Position(String where, int index){
		this(where);
		row = index;
	}
	
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

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
	
}



