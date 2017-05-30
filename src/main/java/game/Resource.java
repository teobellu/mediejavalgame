package game;

import java.util.*;

public class Resource {
	
	private HashMap <String, Integer> minidb;
	
	public Resource(String type, int amount) {
		minidb = new HashMap <String, Integer>();
		add(type, amount);
	}
	
	public Resource() {
		minidb = new HashMap <String, Integer>();
	}

	public int get(String type){
		if (minidb.containsKey(type))
			return minidb.get(type);
		return 0;
	}
	
	public void add(String type, int amount){
		minidb.putIfAbsent(type, 0);
		int currentValue = minidb.get(type);
		minidb.replace(type, currentValue + amount);		
	}
	
	public void add(Resource res){
		if(res == null) return;
		GameConstants.RES_TYPES.stream()
			.filter(type -> res.get(type) > 0)
			.forEach(type -> add(type, res.get(type)));
	}
	
	public void sub(Resource res) throws GameException{
		if(res == null) return;
		//controllo se posso pagare
		for (String i : GameConstants.RES_TYPES)
			if (res.get(i) > 0){
				if (minidb.containsKey(i) == false || minidb.get(i) < res.get(i))
					throw new GameException();
			}
		//pago
		GameConstants.RES_TYPES.stream()
			.filter(type -> res.get(type) > 0)
			.forEach(type -> add(type, -res.get(type)));
	}
}