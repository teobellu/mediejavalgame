package game.state;

import game.Game;

public class StateStartingTurn extends State{

	public StateStartingTurn(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public State doState() {
		try{
			String action = theGame.getCurrentClient().getConnectionHandler().startTurn();
			
			return processAction(action);
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private State processAction(String action){
		if(){
			
		} else if(){
			
		} else if(){
			
		} else if(){
			
		} else if(){
			
		} else {
			
		}
	}

}
