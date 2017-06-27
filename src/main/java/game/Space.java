package game;

import java.io.Serializable;
import java.util.ArrayList;

import game.development.DevelopmentCard;
import game.effect.Effect;

public class Space implements Serializable{
	
	private int requiredDiceValue;
	private Effect instantEffect;
	private boolean singleObject;
	private ArrayList<FamilyMember> familiar;
	
	public Space(int cost, Effect instantEffect, boolean singleObject) {
		requiredDiceValue = cost;
		if (instantEffect != null)
			instantEffect.setSource(GC.ACTION_SPACE);
		this.instantEffect = instantEffect;
		this.singleObject = singleObject;
		familiar = new ArrayList<>();
	}
	
	public void setFamiliar(FamilyMember member){
		familiar.add(member);
	}

	public int getRequiredDiceValue() {
		return requiredDiceValue;
	}

	public ArrayList<FamilyMember> getFamiliars() {
		return familiar;
	}

	public boolean isSingleObject() {
		return singleObject;
	}

	public void setSingleObject(boolean singleObject) {
		this.singleObject = singleObject;
	}
	
	public void setCard(DevelopmentCard card) {
		return;
	}

	public DevelopmentCard getCard() {
		return null;
	}

	public Effect getInstantEffect() {
		return instantEffect;
	}
}

class Cell extends Space{
	
	private DevelopmentCard card;
	
	public Cell(int cost, Effect instantEffect){
		super(cost, instantEffect, true);
	}
	
	@Override
	public void setCard(DevelopmentCard card) {
		this.card = card;
	}

	@Override
	public DevelopmentCard getCard(){
		return card;
	}

}