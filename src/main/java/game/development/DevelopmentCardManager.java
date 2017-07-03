package game.development;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import game.GC;

/**
 * Using visitor design pattern, this class allows to add a development card to a specific list,
 * based on his dynamic type
 * @Visitor_design_patter
 * 
 * @author Matteo
 *
 */
public class DevelopmentCardManager implements Serializable{
	
	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * List of Territories
	 */
	private List<DevelopmentCard> tList = new ArrayList<>();
    
	/**
	 * List of Characters
	 */
	private List<DevelopmentCard> cList = new ArrayList<>();
    
	/**
	 * List of Building
	 */
	private List<DevelopmentCard> bList = new ArrayList<>();
    
	/**
	 * List of ventures
	 */
	private List<DevelopmentCard> vList = new ArrayList<>();
    
	/**
	 * @Visitor_design_pattern
	 * The object allows, through visitor patter, to add the card to the corresponding list
	 */
    private AppendsToListVisitor addVisitor = new AppendsToListVisitor(this);

    /**
     * @Visitor_design_patter
     * Add a development cart to a specific list, nased on its dynamic type
     * @param d Development card to add
     */
    public void add(DevelopmentCard d) {
        d.accept(addVisitor);
    }

    /**
     * Add territory to his list
     * @param t Territory card
     */
    public void add(Territory t) {
        tList.add(t);
    }
    
    /**
     * Add character to his list
     * @param c Character card
     */
    public void add(Character c) {
        cList.add(c);
    }
    
    /**
     * Add building to his list
     * @param b Building card
     */
    public void add(Building b) {
        bList.add(b);
    }
    
    /**
     * Add venture to his list
     * @param v Venture card
     */
    public void add(Venture v) {
        vList.add(v);
    }
    
    /**
     * Getter: Get a list of development cards of a certain type
     * @param type A certain type
     * @return List of development cards
     */
    public List<DevelopmentCard> getList(String type) {
    	switch(type){
    		case GC.DEV_TERRITORY: return tList;
    		case GC.DEV_CHARACTER : return cList;
    		case GC.DEV_BUILDING : return bList;
    		case GC.DEV_VENTURE : return vList;
    		default : return new ArrayList<>();
    	}
    }

    /**
     * Empties the list of cards of a certain type
     * @param type A certain type
     */
	public void freeList(String type) {
		switch(type){
			case GC.DEV_TERRITORY : tList.clear(); 
				break;
			case GC.DEV_CHARACTER : cList.clear(); 
				break;
			case GC.DEV_BUILDING : bList.clear(); 
				break;
			case GC.DEV_VENTURE : vList.clear(); 
				break;
			default : return;
		}
	}
}
