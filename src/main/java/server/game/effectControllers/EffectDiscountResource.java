package server.game.effectControllers;

import java.util.ArrayList;
import java.util.List;

import model.Effect;
import model.GC;
import model.Resource;

/**
 * Effect get a discount of resource, could be a malus or a bonus
 * @author Matteo
 * @author Jacopo
 *
 */
public class EffectDiscountResource implements IEffectBehavior{
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;
	
	private Resource discountResource;
	private List<String> actions;
	
	private Effect ref;	
	private Resource normalGain; 
	private Resource newGain;
	private String thisAction;
	
	public EffectDiscountResource(Resource discountResource) {
		this.discountResource = discountResource;
	}
	
	public EffectDiscountResource(String specificAction, Resource discountResource) {
		this(discountResource);
		if (specificAction == null) 
			return;
		actions = new ArrayList<>();
		actions.add(specificAction);
	}
	
	public EffectDiscountResource(List<String> multipleAction, Resource discountResource) {
		this(discountResource);
		if (multipleAction == null) 
			return;
		actions = new ArrayList<>();
		actions.addAll(multipleAction);
	}

	@Override
	public void effect(Effect ref) {
		initializes(ref);
		if(discountResource == null) 
			return;
		if(actions != null && !actions.contains(thisAction)) 
			return;
		sufferMalus();
		skipMalus();
		applyTax();
	}
	
	/**
	 * Initializes the behavior of the effect
	 * @param ref Effect that possesses this behavior
	 */
	private void initializes(Effect ref){
		this.ref = ref;
		newGain = new Resource();
		normalGain = (Resource) ref.getToAnalyze();
		thisAction = ref.getToScan();
	}
	
	private void sufferMalus() {
		GC.RES_TYPES.stream()
			.filter(type -> normalGain.get(type)>discountResource.get(type) && discountResource.get(type)>0)
			.forEach(type -> newGain.add(type, normalGain.get(type) - discountResource.get(type)));
	}
	
	private void skipMalus() {
		GC.RES_TYPES.stream()
			.filter(type -> normalGain.get(type)==0 || discountResource.get(type)==0)
			.forEach(type -> newGain.add(type, normalGain.get(type)));
	}
	
	private void applyTax (){
		ref.setToAnalyze(newGain);
	}
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		if (discountResource == null)
			return "Nothing";
		String text = "Get a discout of ";
		text += discountResource.toString();
		if (actions == null)
			return text;
		text += "when you perform action: ";
		for (String action : actions)
			text += action + " ";
		return text;
	}
	
}