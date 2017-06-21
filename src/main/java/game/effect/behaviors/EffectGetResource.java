package game.effect.behaviors;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.GC;
import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectGetResource implements IEffectBehavior{

	private Effect ref;			//mi serve per aggiornare toAnalyze (risorse in Effetto)
	private Resource bonus; 	//cio' che guadagnerei normalmente

	public EffectGetResource(Resource bonus) {
		this.bonus = bonus;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		try {
			addResource();
		} catch (RemoteException e) {
			Logger.getLogger(EffectGetResource.class.getName()).log(Level.WARNING, e.getMessage(), e);
		}
	}

	private void initializes(Effect ref){
		this.ref = ref;
	}

	private void addResource() throws RemoteException {
		ref.getBar().gain(ref,bonus);
	}
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		String text = "Get resource: ";
		for (String type : GC.RES_TYPES){
			if (bonus.get(type) > 0)
				text += bonus.get(type) + " " + type + " ";
		}
		if(text == "Get resource: ")
			return "Nothing";
		return text;
	}
}
