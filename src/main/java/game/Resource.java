package game;

import java.util.*;

public class Resource {
	
	public static final String COINS = "coins";
	public static final String WOOD = "wood";
	public static final String STONES = "stones";
	public static final String SERVANTS = "servants";
	public static final String COUNCIL = "council";
	public static final String VICTORYPOINTS = "victorypoints";
	public static final String MILITARYPOINTS = "militarypoints";
	public static final String FAITHPOINTS = "faithpoints";
	
	public static final List<String> RESOURCE_TYPES = Collections.unmodifiableList(
			Arrays.asList(COINS, WOOD, STONES, SERVANTS, COUNCIL, VICTORYPOINTS, MILITARYPOINTS, FAITHPOINTS));
	
	private HashMap <String, Integer> minidb = new HashMap <String, Integer>();
	
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
		RESOURCE_TYPES.stream()
			.filter(type -> res.get(type) > 0)
			.forEach(type -> add(type, res.get(type)));
	}
	
	public void sub(Resource res) throws GameException{
		if(res == null) return;
		//controllo se posso pagare
		for (String i : RESOURCE_TYPES)
			if (res.get(i) > 0){
				if (minidb.containsKey(i) == false || minidb.get(i) < res.get(i))
					throw new GameException();
			}
		//pago
		RESOURCE_TYPES.stream()
			.filter(type -> res.get(type) > 0)
			.forEach(type -> add(type, -res.get(type)));
	}
}