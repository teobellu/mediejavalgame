package game.effect.behaviors;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.GameException;
import game.Player;
import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectConvertResource implements IEffectBehavior{
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String MESSAGE = "Do you want to convert resources?"; 
	
	private Player player;
	
	private List<Resource> payOptions;
	private List<Resource> gainOptions;
	private List<Resource> realPayOptions;
	private List<Resource> realGainOptions;
	
	private EffectConvertResource(){
		payOptions = new ArrayList<>();
		gainOptions = new ArrayList<>();
		realPayOptions = new ArrayList<>();
		realGainOptions = new ArrayList<>();
	}
	
	public EffectConvertResource(Resource resourceToConvert, Resource resourceToGain) {
		this();
		payOptions.add(resourceToConvert);
		gainOptions.add(resourceToGain);
	}
	
	public EffectConvertResource(List<Resource> payOptions, List<Resource> gainOptions) {
		this();
		this.payOptions.addAll(payOptions);
		this.gainOptions.addAll(gainOptions);
	}

	@Override
	public void effect(Effect ref) {
		initializes(ref);
		tryConvert();
		try {
			convert();
		} catch (RemoteException e) {
			Logger.getLogger(EffectConvertResource.class.getName()).log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	/**
	 * Initializes the behavior of the effect
	 * @param ref Effect that possesses this behavior
	 */
	private void initializes(Effect ref){
		player = ref.getPlayer();
	}

	private void convert() throws RemoteException {
		//chiede cose al client: tipo, vuoi convertire?
		if (realPayOptions.isEmpty())
			return;
		boolean wantConvert = player.getClient().getConnectionHandler().askBoolean(MESSAGE);
		if (wantConvert){
			int index = player.getClient().getConnectionHandler().chooseConvert(realPayOptions, realGainOptions);
			try {
				player.pay(realPayOptions.get(index));
				player.gain(realGainOptions.get(index));
			} catch (GameException e) {
				return;
			}
		}
	}
	
	private void tryConvert() {
		for (int i = 0; i <= payOptions.size(); i++){
			try {
				player.pay(payOptions.get(i));
				player.gain(gainOptions.get(i));
				realPayOptions.add(payOptions.get(i));
				realGainOptions.add(gainOptions.get(i));
			} catch (GameException e) {
				// TODO il giocatore non puo' converire perche' non ha le risorse necessarie
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		String text = "Convert resource: ";
		int numberOfOptions = payOptions.size();
		for(int i = 0; i < numberOfOptions; i++){
			text += payOptions.get(i).toString();
			text += "into ";
			text += gainOptions.get(i).toString();
			if (i + 1 != numberOfOptions){
				text += "or ";
			}
		}
		return text;
	}
}
