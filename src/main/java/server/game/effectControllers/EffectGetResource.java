package server.game.effectControllers;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Effect;
import model.GC;
import model.Resource;

/**
 * Bonus get resource
 * @author Matteo
 * @author Jacopo
 *
 */
public class EffectGetResource implements IEffectBehavior{
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;

	private Effect ref;
	private Resource bonus;

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

	/**
	 * Initializes the behavior of the effect
	 * @param ref Effect that possesses this behavior
	 */
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
