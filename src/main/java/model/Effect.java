package model;

public abstract class Effect{
	protected Player player;
	
	public abstract void effect();
	
	public void setPlayer(Player player){
		this.player = player;
	}
}