package game.development;

public interface DevelopmentCardVisitor {
	void visit(Territory t);
	void visit(Character c);
	void visit(Building b);
	void visit(Venture v);
}
