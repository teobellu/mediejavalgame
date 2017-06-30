package game;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import exceptions.GameException;

public class ListenAction{
	
	protected final Game _theGame;
	protected Player _player;
	protected List<String> actionsAlreadyDone;
	
	private Logger _log = Logger.getLogger(ListenAction.class.getName());
	
	public ListenAction(Game game){
		_theGame = game;
		_player = _theGame.getCurrentPlayer();
		actionsAlreadyDone = new ArrayList<>();
	}
	
	public void setPlayer(Player nextPlayer) {
		_player = nextPlayer;
		actionsAlreadyDone.clear();
	}

	public GameBoard getGameBoard() {
		return _theGame.getBoard();
	}
	
	public Player getMe() {
		return _player;
	}
	
	public void dropLeaderCard(String leaderName) throws GameException{
		LeaderCard selection = null;
		List<LeaderCard> playerLeaders = _player.getLeaderCards();
		List<String> playerLeadersNames = new ArrayList<>();
		playerLeaders.forEach(leader -> playerLeadersNames.add(leader.getName()));
		for (LeaderCard leader : playerLeaders){
			if (leader.getName().equals(leaderName))
				selection = leader;
		}
		if (selection == null)
			throw new GameException("You can't discard this card!");
		try {
			_theGame.getDynamicBar().discardLeaderCard(selection);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
		}
		//TODO avviso il player che è tutto ok
		//TODO avviso gli altri player
		actionsAlreadyDone.add(GC.DROP_LEADER);
	}
	
	public void activateLeaderCard(LeaderCard card) throws GameException {
		LeaderCard selection = null;
		List<LeaderCard> activableLeaders = _player.getActivableLeaderCards();
		List<String> playerLeadersNames = new ArrayList<>();
		activableLeaders.forEach(leader -> playerLeadersNames.add(leader.getName()));
		for (LeaderCard leader : activableLeaders){
			if (leader.getName().equals(card.getName()))
				selection = leader;
		}
		if (selection == null)
			throw new GameException("You can't activate this card!");
		_theGame.getDynamicBar().activateLeaderCard(selection);
		//TODO avviso il player che è tutto ok
		//TODO avviso gli altri player
		actionsAlreadyDone.add(GC.ACTIVATE_LEADER);
	}
	
	public void placeFamiliar(FamilyMember familiar, Position position) throws GameException {
		if (actionsAlreadyDone.contains(GC.PLACE_FAMILIAR))
			throw new GameException("You have already placed a familiar");
		List<FamilyMember> freeMembers = _player.getFreeMember();
		FamilyMember selection = null;
		for (FamilyMember f : freeMembers){
			if (f.getColor().equals(familiar.getColor())){
				selection = f;
			}
		}
		if (selection == null){
			throw new GameException("You can't place this familiar!");
		}
		familiar = selection;
		
		switch(position.getWhere()){
			case GC.TOWER :
				_theGame.getDynamicBar().placeInTower(familiar, position.getRow(), position.getColumn());
				break;
			case GC.COUNCIL_PALACE :
				_theGame.getDynamicBar().placeCouncilPalace(familiar);
				break;
			case GC.HARVEST :
				_theGame.getDynamicBar().placeWork(familiar, GC.HARVEST);
				break;
			case GC.PRODUCTION :
				_theGame.getDynamicBar().placeWork(familiar, GC.PRODUCTION);
				break;
			case GC.MARKET :
				_theGame.getDynamicBar().placeMarket(familiar, position.getRow());
				break;
			default : throw new GameException("Invalid position");
		}
		//TODO avviso il player che è tutto ok
		//TODO avviso gli altri player
		actionsAlreadyDone.add(GC.PLACE_FAMILIAR);
	}
	
	public void endTurn() throws GameException{
		if (actionsAlreadyDone.contains(GC.END_TURN))
			throw new GameException("You have already ended turn");
		_theGame.getState().nextState();
		_player.getEffects().removeIf(eff -> eff.getSource().equals(GC.ACTION_SPACE));
		//TODO avviso il player che è tutto ok
		//TODO avviso gli altri player
		actionsAlreadyDone.clear();
		//TODO cambio stato
	}

	
	
		
}
