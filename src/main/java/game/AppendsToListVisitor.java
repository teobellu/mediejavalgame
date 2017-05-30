package game;

public class AppendsToListVisitor implements DevelopmentCardVisitor{

	private DevelopmentCardManager manager;
	
	public AppendsToListVisitor(DevelopmentCardManager manager) {
        this.manager = manager;
    }
	
	@Override
	public void visit(Territory t) {
		manager.getList(GameConstants.DEV_TERRITORY).add(t);
	}

	@Override
	public void visit(Character c) {
		manager.getList(GameConstants.DEV_CHARACTER).add(c);
	}

	@Override
	public void visit(Building b) {
		manager.getList(GameConstants.DEV_BUILDING).add(b);
	}

	@Override
	public void visit(Venture v) {
		manager.getList(GameConstants.DEV_VENTURE).add(v);
	}
	
}
