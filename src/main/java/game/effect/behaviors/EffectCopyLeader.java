package game.effect.behaviors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import game.FamilyMember;
import game.GC;
import game.LeaderCard;
import game.Player;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectCopyLeader implements IEffectBehavior, Serializable{
	
	private Player player;
	private LeaderCard selectedCard;
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		selectLeaderCard();
		activateLeaderCard();
	}
	
	private void initializes(Effect ref) {
		player = ref.getPlayer();
	}
	
	private void selectLeaderCard() {
		//TODO selectedCard = ...
	}
	
	private void activateLeaderCard() {
		player.addEffect(selectedCard.getEffect());
	}
}
