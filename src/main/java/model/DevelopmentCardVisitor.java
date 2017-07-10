package model;

/**
 * @Visitor_design_pattern
 * The interface simply follows the standard lines of visitor design pattern
 * 
 * @author Matteo
 *
 */
public interface DevelopmentCardVisitor {
	
	/**
	 * Visit a territory
	 * @param t Territory card
	 */
	void visit(Territory t);
	
	/**
	 * Visit a character
	 * @param c Character card
	 */
	void visit(Character c);
	
	/**
	 * Visit a building
	 * @param b Building card
	 */
	void visit(Building b);
	
	/**
	 * Visit a venture
	 * @param v Venture card
	 */
	void visit(Venture v);
}
