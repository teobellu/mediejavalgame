package game;

import java.io.Serializable;

public class Position implements Serializable{

	private String where;
	
	private int row;
	
	private int column;
	
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



