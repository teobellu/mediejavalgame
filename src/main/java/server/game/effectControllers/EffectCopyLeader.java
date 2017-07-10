package server.game.effectControllers;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Effect;
import model.LeaderCard;
import model.Player;
import util.CommandStrings;

public class EffectCopyLeader implements IEffectBehavior{
	
	private transient Logger _log = Logger.getLogger(EffectCopyLeader.class.getName());
	
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

		Map<LeaderCard, Player> discardedLeader = effect.getBar().getDiscardedLeaderCards();
		
		List<LeaderCard> options = new ArrayList<>();
		
		discardedLeader.forEach((card, owner) -> {if (!owner.getName().equals(player.getName())) options.add(card);});
		
		if (!options.isEmpty()){
			int index = player.getClient().getConnectionHandler().chooseLeader(CommandStrings.CHOOSE_LEADER, options);
			LeaderCard selection = options.get(index);
			player.addEffect(selection.getEffect());
		}else
			player.getClient().getConnectionHandler().sendInfo("You did not copy any effects because there were no effects available");

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
