package model;


public class LeaderCard implements Card{
	private String name;
	private Resource requirement;
	private int reqTerritory;
	private int reqBuilding;
	private int reqCharacter;
	private int reqVenture;
	private Effect effect;
	private boolean isPermanent;
	
	public LeaderCard(UserConfig userconfig){
		//TODO
	}
	
	public void setRequirement(Resource requirement){
		this.requirement = requirement;
	}
	
	public void setCardRequirementTerritory(int requirement){
		reqTerritory = requirement;
	}
	
	public void setCardRequirementBuilding(int requirement){
		reqBuilding = requirement;
	}
	
	public void setCardRequirementCharacter(int requirement){
		reqCharacter = requirement;
	}
	
	public void setCardRequirementVenture(int requirement){
		reqVenture = requirement;
	}
	
	public void setEffect(Effect effect){
		this.effect = effect;
	}
	
	public void setIsPermanent(boolean isPermanent){
		this.isPermanent = isPermanent;
	}
	
	
}
