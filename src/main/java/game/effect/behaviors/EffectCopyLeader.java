package game.effect.behaviors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import game.FamilyMember;
import game.GC;
import game.LeaderCard;
import game.Player;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectCopyLeader implements IEffectBehavior{
	
	private Effect effect;
	private Player player;
	private LeaderCard selectedCard;
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		selectLeaderCard();
		activateLeaderCard();
	}
	
	private void initializes(Effect ref) {
		effect = ref;
		player = ref.getPlayer();
	}
	
	private void selectLeaderCard() {
		//TODO selectedCard = ...
		Map<LeaderCard, Player> options = effect.getBar().getDiscardedLeaderCards();
		
		
		LeaderCard selection = null;
		//TODO o meglio una copia dell'effetto?
		player.addEffect(selection.getEffect());
	}
	
	private void activateLeaderCard() {
		player.addEffect(selectedCard.getEffect());
	}
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		return "Copy a leader card";
	}
}
