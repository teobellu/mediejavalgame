package model;

import java.io.Serializable;

/**
 * 
 * The class deals with adding an element to a list, depending on its dynamic type
 * @Visitor_Design_Pattern
 * 
 * @author Matteo
 * @author Jacopo
 *
 */
public class AppendsToListVisitor implements DevelopmentCardVisitor, Serializable{

	/**
	 * A default serial version ID to the selected type.
	 */
	private static final long serialVersionUID = 1L;
	
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
