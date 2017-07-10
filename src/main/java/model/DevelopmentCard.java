package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is designed to contain all information about a single development card
 * 
 * @author M
 *
 */
public abstract class DevelopmentCard implements ICard, Serializable{
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Id of the card, used for example by graphical user interface
	 */
	private int id;
	
	/**
	 * Name of the card
	 */
	protected String name;
	
	/**
	 * Age of the card
	 */
	protected int age;
	
	/**
	 * Immediate effects of the card
	 */
	private List<Effect> immediateEffect;
	
	/**
	 * Permanent effects of the card
	 */
	private List<Effect> permanentEffect;
	
	private List<Resource> cost;
	private List<Resource> requirement;
	
	protected int dice;
	
	public DevelopmentCard (){
		immediateEffect = new ArrayList<>();
		permanentEffect = new ArrayList<>();
		cost = new ArrayList<>();
		requirement = new ArrayList<>();
		cost.add(new Resource());
		requirement.add(new Resource());
	}
	
	public abstract void accept(DevelopmentCardVisitor visitor);
	public abstract String toString();
	
	protected void configureEffect(Effect effect){
		if (effect != null)
			effect.setSource(toString());
	}
	
	protected void configureEffect(List<Effect> effects){
		if (effects != null)
			effects.forEach(effect -> configureEffect(effect));
	}

	public int getDice() {
		return dice;
	}

	public void setDice(int dice) {
		this.dice = dice;
	}
	
	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}
	
	protected void addImmediateEffect(Effect effect) {
		immediateEffect.add(effect);
	}
	
	protected void addPermanentEffect(Effect effect) {
		permanentEffect.add(effect);
	}
	
	public Resource getCost(int index) {
		return cost.get(index);
	}

	public List<Resource> getCosts() {
		return cost;
	}
	
	protected void setCost(Resource res) {
		cost.set(0, res);
	}
	
	protected void setRequirement(Resource res) {
		requirement.set(0, res);
	}
	
	public Resource getRequirement(int index) {
		return requirement.get(index);
	}

	public List<Resource> getRequirement() {
		return requirement;
	}
	
	public List<Effect> getImmediateEffect() {
		configureEffect(immediateEffect);
		return immediateEffect;
	}
	
	public List<Effect> getPermanentEffect() {
		configureEffect(permanentEffect);
		return permanentEffect;
	}
	
	protected void addImmediateEffect(List<Effect> immediate) {
		if (immediate != null)
			immediateEffect.addAll(immediate);
	}
	
	protected void addPermanentEffect(List<Effect> permanent) {
		if (permanent != null)
			permanentEffect.addAll(permanent);
	}
	
	protected void setCost(List<Resource> cost) {
		this.cost.clear();
		if (cost != null)
			this.cost.addAll(cost);
	}
	
	protected void setRequirement(List<Resource> requires) {
		requirement.clear();
		if (requires != null)
			requirement.addAll(requires);
	}

	/**
	 * Getter: Get the id of the card, used for example by GUI
	 * @return Id of the card
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter: Set the id of the card, used for example by GUI
	 * @param Id of the card
	 */
	public void setId(int id) {
		this.id = id;
	}
	
}