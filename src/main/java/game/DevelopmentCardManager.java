package game;

import java.util.*;

public class DevelopmentCardManager {
	private List<DevelopmentCard> tList = new ArrayList<>();
    private List<DevelopmentCard> cList = new ArrayList<>();
    private List<DevelopmentCard> bList = new ArrayList<>();
    private List<DevelopmentCard> vList = new ArrayList<>();
    
    private AppendsToListVisitor addVisitor = new AppendsToListVisitor(this);

    public void add(DevelopmentCard d) {
        d.accept(addVisitor);
    }

    public void add(Territory t) {
        tList.add(t);
    }
    public void add(Character c) {
        cList.add(c);
    }
    public void add(Building b) {
        bList.add(b);
    }
    public void add(Venture v) {
        vList.add(v);
    }
    
    public List<DevelopmentCard> getList(String type) {
    	switch(type){
    		case GameConstants.DEV_TERRITORY: return tList;
    		case GameConstants.DEV_CHARACTER : return cList;
    		case GameConstants.DEV_BUILDING : return bList;
    		case GameConstants.DEV_VENTURE : return vList;
    		default : return null;
    	}
    }

	public void freeList(String type) {
		switch(type){
			case GameConstants.DEV_TERRITORY : tList.clear(); break;
			case GameConstants.DEV_CHARACTER : cList.clear(); break;
			case GameConstants.DEV_BUILDING : bList.clear(); break;
			case GameConstants.DEV_VENTURE : vList.clear(); break;
			default : return;
		}
	}
}
