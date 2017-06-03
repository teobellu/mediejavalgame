package game;

public class Territory extends DevelopmentCard{
	
	@Override
	public DevelopmentCard getTerritory(){
		return this;
	}

	@Override
	public void accept(DevelopmentCardVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		return GC.DEV_TERRITORY;
	}
	
	/*
	@Override
	public void activateImmediateEffect(Player player) {
		for (Effect x : immediateEffect){
			x.setPlayer(player);
			x.effect();
		}
	}
	
	@Override
	public void activatePermanentEffect(Player player) {
		for (Effect x : permanentEffect){
			x.setPlayer(player);
			x.effect();
		}
	}
	 */
}
