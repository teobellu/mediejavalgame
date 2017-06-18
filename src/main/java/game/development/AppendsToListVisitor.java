package game.development;

import java.io.Serializable;

import game.GC;

/**
 * The class deals with adding an element to a list, depending on its dynamic type
 * 
 * @author Matteo
 *
 */
public class AppendsToListVisitor implements DevelopmentCardVisitor, Serializable{

	/**
	 * Manager that contains all lists
	 */
	private DevelopmentCardManager manager;
	
	/**
	 * Constructor
	 * @param manager Manager that contains all lists
	 */
	public AppendsToListVisitor(DevelopmentCardManager manager) {
        this.manager = manager;
    }
	
	@Override
	public void visit(Territory t) {
		manager.getList(GC.DEV_TERRITORY).add(t);
	}

	@Override
	public void visit(Character c) {
		manager.getList(GC.DEV_CHARACTER).add(c);
	}

	@Override
	public void visit(Building b) {
		manager.getList(GC.DEV_BUILDING).add(b);
	}

	@Override
	public void visit(Venture v) {
		manager.getList(GC.DEV_VENTURE).add(v);
	}
	
}
