package model;

import java.util.*;

public class Player {
	
	private int id;
	private int turn;
	private String name;
	private Resource resource;
	private ArrayList<DevelopmentCard> developmentCard;
	private ArrayList<LeaderCard> leaderCard;
	
	protected Player (){
		name = new String();
		resource = new Resource();
		developmentCard = new ArrayList<>();
		leaderCard = new ArrayList<>();
	}
	
	public void Gain (Resource res){
		resource.add(res);
	}
	
	public void Pay (Resource cost) throws GameException{
		resource.sub(cost);
	}
	
	/*
	public void buyDevelopmentCard (DevelopmentCard newCard) throws GameException{
		this.Pay(newCard.getCost());
		this.developmentCard.add(newCard);
	}
	*/
	
	
}
