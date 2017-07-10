package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Optional;

import model.exceptions.GameException;

/**
 * Class used to manage resources (as hoard or game loot), like coins or wood, 
 * and perform math operations on related information
 * @author Jacopo
 * @author Matteo
 */
public class Resource implements Serializable{
	
	/**
	 * A default serial version ID to the selected type
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Object that contains information about the resource package;
	 * To the left the type of resource, to the right its quantity
	 */
	private HashMap <String, Integer> hoard;
	
	/**
	 * Creates a resource pack and fills it with a single type of resource and his amount
	 * @param type Type of resource to add
	 * @param amount Quantity of resource type to add
	 */
	public Resource(String type, int amount) {
		this();
		add(type, amount);
	}
	
	/**
	 * Creates an empty resource package.
	 */
	public Resource() {
		hoard = new HashMap <>();
	}

	/**
	 * Creates a resource pack and fills it with a single type of resource and his amount
	 * and another pack of resource
	 * @param resource The another pack of resource to add
	 * @param type Type of single resource to add
	 * @param amount Quantity of single resource type to add
	 */
	public Resource(Resource resource, String type, int amount) {
		this(type, amount);
		add(resource);
	}

	/**
	 * Receives a single resource type and returns its current quantity;
	 * The method returns 0 if the resource type does not exist in the hashmap, 
	 * as it is logical that both
	 * @param type Type of resource
	 * @return Quantity of resource type
	 */
	public int get(String type){
		if (hoard.containsKey(type))
			return hoard.get(type);
		return 0;
	}
	
	/**
	 * Adds a single type of resource to current resources
	 * @param type Type of resource to add
	 * @param amount Quantity of resource type to add
	 */
	public void add(String type, int amount){
		hoard.putIfAbsent(type, 0);
		int currentValue = hoard.get(type);
		hoard.replace(type, currentValue + amount);		
	}
	
	/**
	 * Adds additional resources to current resources
	 * @param addition Resource to add
	 */
	public void add(Resource addition){
		if(addition == null) 
			return;
		GC.RES_TYPES.stream()
			.filter(type -> addition.get(type) > 0)
			.forEach(type -> add(type, addition.get(type)));
	}
	
	/**
	 * Subtracts resources from current resources using class java.util.Optional, 
	 * only if for each resource type the quantity remains greater or equal to zero.
	 * @param price Resource to subtract
	 * @throws GameException Some types of resources have a negative amount
	 */
	public void sub(Resource price) throws GameException{
		if(price == null) 
			return;
		Optional<String> check = GC.RES_TYPES.stream()
			.filter(type -> price.get(type) > 0)
			.filter(type -> !hoard.containsKey(type) || hoard.get(type) < price.get(type))
			.findFirst();
		if (check.isPresent()) 
			throw new GameException("You can't pay! You don't have enough resources!");
		GC.RES_TYPES.stream()
			.filter(type -> price.get(type) > 0)
			.forEach(type -> add(type, -price.get(type)));
	}
	
	@Override
	public String toString(){
		StringBuilder description = new StringBuilder();
		for (String type : GC.RES_TYPES){
			if (get(type) > 0){
				description.append(get(type)+" "+type.replaceAll("_", " ")+", ");
			}
		}
		
		if(description.length()>0)
			return description.substring(0, description.length()-2);
		return "";
	}
}