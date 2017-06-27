package game.effect.behaviors;

import java.io.Serializable;
import java.rmi.RemoteException;
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
	
	private static final String MESSAGE = "Select a leader card to copy";
	
	private Effect effect;
	private Player player;
	private LeaderCard selectedCard;
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		try {
			selectLeaderCard();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		activateLeaderCard();
	}
	
	private void initializes(Effect ref) {
		effect = ref;
		player = ref.getPlayer();
	}
	
	private void selectLeaderCard() throws RemoteException {
		//TODO selectedCard = ...
		Map<LeaderCard, Player> discardedLeader = effect.getBar().getDiscardedLeaderCards();
		
		List<LeaderCard> options = new ArrayList<>();
		
		//TODO se size == 0 ?
		
		discardedLeader.forEach((card, owner) -> {if (owner.getName() != player.getName()) options.add(card);});
		
		int index = player.getClient().getConnectionHandler().chooseLeader(MESSAGE, options);;
		
		LeaderCard selection = options.get(index);
		
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
