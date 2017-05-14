package model;

import java.util.*;

public class Resource {
	
	public enum type { COINS, WOOD, STONES, SERVANTS, VICTORYPOINTS, MILITARYPOINTS, FAITHPOINTS }
	
	private HashMap <type, Integer> minidb = new HashMap <type, Integer>();
	
	public int get(type type){
		if (minidb.containsKey(type))
			return minidb.get(type);
		return 0;
	}
	
	public void add(type type, int amount){
		minidb.putIfAbsent(type, 0);
		int currentValue = minidb.get(type);
		minidb.replace(type, currentValue + amount);		
	}
	
	public void add(Resource res){
		for (type i : type.values())
			if (res.get(i) > 0)
				this.add(i, res.get(i));
	}
	
	public void sub(Resource res) throws GameException{
		//controllo se posso pagare
		for (type i : type.values())
			if (res.get(i) > 0){
				if (minidb.containsKey(i) == false || minidb.get(i) < res.get(i))
					throw new GameException();
			}
		//pago
		for (type i : type.values())
			if (res.get(i) > 0){
				this.add(i, -res.get(i));
			}
	}
}