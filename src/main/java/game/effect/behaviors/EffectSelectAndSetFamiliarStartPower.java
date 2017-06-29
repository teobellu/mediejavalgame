package game.effect.behaviors;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import game.FamilyMember;
import game.GC;
import game.Messages;
import game.Player;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectSelectAndSetFamiliarStartPower implements IEffectBehavior{
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Message to send to the player
	 */
	private String message;
	
	/**
	 * Owner of the effect
	 */
	private Player player;
	
	/**
	 * Type of familiar to set
	 */
	private String typeOfFamiliar;
	
	/**
	 * Power of familiar to set
	 */
	private Integer valueToSet;
	
	/**
	 * Set of familiars that can be modify
	 */
	private List<FamilyMember> familiars;
	
	/**
	 * Familiar selected by the player
	 */
	private FamilyMember familiarToModify;
	
	/**
	 * Base constructor of EffectSelectAndSetFamiliarStartPower effect behavior
	 * @param typeOfFamiliar Type of familiar to modify
	 * @param valueToSet Value to set
	 */
	public EffectSelectAndSetFamiliarStartPower(String typeOfFamiliar, Integer valueToSet) {
		this.typeOfFamiliar = typeOfFamiliar;
		this.valueToSet = valueToSet;
		
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		try {
			selectFamiliarsToModify();
		} catch (RemoteException e) {
			Logger.getLogger(EffectSelectAndSetFamiliarStartPower.class.getName()).log(Level.WARNING, e.getMessage(), e);
		}
		setFamiliarsToModify();
	}
	
	/**
	 * Initializes the behavior of the effect
	 * @param ref Effect that possesses this behavior
	 */
	private void initializes(Effect ref) {
		player = ref.getPlayer();
		familiars = player.getFreeMember();
		message = Messages.MESS_SELECT_AND_SET_FAMILIAR + valueToSet;
	}
	
	/**
	 * Select a familiar from a set of familiars
	 * @throws RemoteException Connection error problem
	 */
	private void selectFamiliarsToModify() throws RemoteException {
		List<FamilyMember> filteredFamiliars = new ArrayList<>();
		if (typeOfFamiliar == GC.FM_COLOR)
			filteredFamiliars.addAll(familiars.stream()
				.filter(fam -> fam.getColor() != GC.FM_TRANSPARENT)
				.collect(Collectors.toList()));
		if (typeOfFamiliar == GC.FM_TRANSPARENT)
			filteredFamiliars.addAll(familiars.stream()
				.filter(fam -> fam.getColor() == GC.FM_TRANSPARENT)
				.collect(Collectors.toList()));
		if (filteredFamiliars.isEmpty())
			return;
		int index = player.getClient().getConnectionHandler().chooseFamiliar(filteredFamiliars, message);
		familiarToModify = filteredFamiliars.get(index);
	}
	
	/**
	 * Modify the value of the familiar
	 */
	private void setFamiliarsToModify() {
		if (familiarToModify == null)
			return;
		familiarToModify.setValue(valueToSet);
	}
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		String text = "Select a familiar with type: " + typeOfFamiliar;
		text += ", and set his power to " + valueToSet;
		return text;
	}
}
