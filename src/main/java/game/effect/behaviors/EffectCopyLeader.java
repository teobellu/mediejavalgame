package game.effect.behaviors;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.LeaderCard;
import game.Player;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectCopyLeader implements IEffectBehavior{
	
	private transient Logger _log = Logger.getLogger(EffectCopyLeader.class.getName());
	
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
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		activateLeaderCard();
	}
	
	/**
	 * Initializes the behavior of the effect
	 * @param ref Effect that possesses this behavior
	 */
	private void initializes(Effect ref) {
		effect = ref;
		player = ref.getPlayer();
	}
	
	private void selectLeaderCard() throws RemoteException {
		//TODO selectedCard = ...
		Map<LeaderCard, Player> discardedLeader = effect.getBar().getDiscardedLeaderCards();
		
		List<LeaderCard> options = new ArrayList<>();
		
		//TODO se size == 0 ?
		
		discardedLeader.forEach((card, owner) -> {if (!owner.getName().equals(player.getName())) options.add(card);});
		
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
