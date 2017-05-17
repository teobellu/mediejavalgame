package game;

import java.util.*;

import game.UserConfig;


public class GameBoard {
	
	private final UserConfig userConfig;
	
	private ArrayList <Cell> territoryCol = new ArrayList <Cell>();
	private ArrayList <Cell> characterCol = new ArrayList <Cell>();
	private ArrayList <Cell> buildingCol = new ArrayList <Cell>();
	private ArrayList <Cell> ventureCol = new ArrayList <Cell>();
	
	private ArrayList <Space> turnPos = new ArrayList <Space>();
	private ArrayList <Space> harvestPos = new ArrayList <Space>();
	private ArrayList <Space> productionPos = new ArrayList <Space>();
	private Space marketCoinsPos;
	private Space marketServantsPos;
	private Space marketMilitaryPos;
	private Space marketCouncilPos;
	//private int diceBlack;
	//private int diceOrange;
	//private int diceWhite;
	private int dices[] = new int[3];;
	
	public GameBoard(UserConfig userConfig){
		this.userConfig = userConfig;
	}
	
	public void inizialize(){
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
		marketCoinsPos = null;
		marketServantsPos = null;
		marketMilitaryPos = null;
		marketCouncilPos = null;
	}
	
}

class Cell{
	private DevelopmentCard card;
	private Space space;
}

class Space{
	
	private int dice;
	private Resource instantBonus;
	
	public Space(int cost, Resource resource) {
		dice = cost;
		instantBonus = resource;
	}
}