package game.effect.behaviors;

import game.GC;
import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;

public class EffectSantaRita implements IEffectBehavior{
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Resources you need to receive to apply the bonus
	 */
	private Resource resource;
	
	/**
	 * Effect with this behavior
	 */
	private Effect ref;
	
	/**
	 * What I have to gain without the effect
	 */
	private Resource normalGain;
	
	/**
	 * What I have to gain
	 */
	private Resource newGain;
	
	/**
	 * Source of the gain
	 */
	private String source;
	
	/**
	 * Constructor of EffectSantaRita
	 * @param resource Resources you need to receive to apply the bonus
	 */
	public EffectSantaRita(Resource resource) {
		this.resource = resource;
	}
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		if(resource == null) 
			return;
		if(!GC.DEV_TYPES.contains(source)) 
			return;
		findNextGain();
		applyBenefit();
	}
	
	/**
	 * Initializes the behavior of the effect
	 * @param ref Effect that possesses this behavior
	 */
	private void initializes(Effect ref){
		this.ref = ref;
		newGain = new Resource();
		normalGain = (Resource) ref.getToAnalyze();
		source = ref.getToScan();
	}
	
	/**
	 * Find the bonus to apply
	 */
	public void findNextGain(){
		if(GC.DEV_TYPES.contains(source))
			GC.RES_TYPES.stream()
				.filter(type -> resource.get(type) > 0 && normalGain.get(type) > 0)
				.forEach(type -> newGain.add(type, normalGain.get(type)));
	}
	
	/**
	 * Apply bonus to the player
	 */
	private void applyBenefit (){
		ref.getPlayer().gain(newGain);
	}
	
	/**
	 * Describes the behavior
	 */
	@Override
	public String toString(){
		String text = "Each time you recieve " + resource.toString();
		text += "from instant effect of a development card, you recieve the bonus twice";
		return text;
	}
}