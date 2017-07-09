package model.effects;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Effect;
import model.GC;
import model.IEffectBehavior;
import model.Player;
import model.Resource;
import model.exceptions.GameException;

public class EffectConvertResource implements IEffectBehavior{
	
	private transient Logger _log = Logger.getLogger(EffectConvertResource.class.getName());
	
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

	private Effect effect;
	
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
			_log.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	/**
	 * Initializes the behavior of the effect
	 * @param ref Effect that possesses this behavior
	 */
	private void initializes(Effect ref){
		effect = ref;
		player = ref.getPlayer();
	}

	private void convert() throws RemoteException {
		//chiede cose al client: tipo, vuoi convertire?
		if (realPayOptions.isEmpty())
			return;
		boolean wantConvert = player.getClient().getConnectionHandler().askBoolean(MESSAGE);
		if (wantConvert){
			Resource bonus = new Resource();
			int index = player.getClient().getConnectionHandler().chooseConvert(realPayOptions, realGainOptions);
			try {
				player.pay(realPayOptions.get(index));
				bonus.add(realGainOptions.get(index));
				if(bonus.get(GC.RES_COUNCIL) > 0)
					bonus = effect.getBar().handleCouncil(bonus);
				player.gain(bonus);
			} catch (GameException e) {
				_log.log(Level.OFF, e.getMessage(), e);
				return;
			}
		}
	}
	
	private void tryConvert() {
		for (int i = 0; i < payOptions.size(); i++){
			try {
				player.pay(payOptions.get(i));
				player.gain(payOptions.get(i));
				realPayOptions.add(payOptions.get(i));
				realGainOptions.add(gainOptions.get(i));
			} catch (GameException e) {
				// TODO il giocatore non puo' converire perche' non ha le risorse necessarie
				_log.log(Level.INFO, e.getMessage(), e);
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
