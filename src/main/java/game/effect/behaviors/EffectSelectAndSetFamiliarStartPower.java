package game.effect.behaviors;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import game.FamilyMember;
import game.GC;
import game.Player;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectSelectAndSetFamiliarStartPower implements IEffectBehavior{
	
	private static final String MESSAGE = "Set familiar to value ";
	private String message;
	private Player player;
	private String typeOfFamiliar;		//parametri
	private Integer valueToSet;
	
	private List<FamilyMember> familiars;
	private FamilyMember familiarToModify;
	
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
	
	private void initializes(Effect ref) {
		player = ref.getPlayer();
		//familiars = new ArrayList<>();
		//familiars.addAll(ref.getPlayer().getFreeMember());
		
		familiars = player.getFreeMember();
		message = MESSAGE + valueToSet;
	}
	
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
		//TODO select familiarToModify dentro la lista filteredFamiliars
		if (filteredFamiliars.isEmpty())
			return;
		int index = player.getClient().getConnectionHandler().chooseFamiliar(filteredFamiliars, message);
		familiarToModify = filteredFamiliars.get(index);
	}
	
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
