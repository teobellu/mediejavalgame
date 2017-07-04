package game;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import exceptions.GameException;

public class ListenAction{
	
	protected final Game _theGame;
	protected Player _player;
	protected List<String> actionsAlreadyDone;
	
	public ListenAction(Game game){
		_theGame = game;
		_player = _theGame.getPlayers().get(0);
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
		
		try {
			_player.getClient().getConnectionHandler().sendInfo("Leader card dropped!", _player);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//TODO avviso gli altri player
		actionsAlreadyDone.add(GC.DROP_LEADER);
	}
	
	public void activateLeaderCard(String leaderName) throws GameException {
		LeaderCard selection = null;
		List<LeaderCard> activableLeaders = _player.getActivableLeaderCards();
		List<String> playerLeadersNames = new ArrayList<>();
		activableLeaders.forEach(leader -> playerLeadersNames.add(leader.getName()));
		for (LeaderCard leader : activableLeaders){
			if (leader.getName().equals(leaderName))
				selection = leader;
		}
		if (selection == null)
			throw new GameException("You can't activate this card!");
		_theGame.getDynamicBar().activateLeaderCard(selection);

		try {
			//TODO mando solo il player? O ci possono essere update della mappa anche?
			_player.getClient().getConnectionHandler().sendInfo("Leader card activated!", _player);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//TODO avviso gli altri player
		actionsAlreadyDone.add(GC.ACTIVATE_LEADER);
	}
	
	public void placeFamiliar(String familiarColour, Position position) throws GameException {
		if (actionsAlreadyDone.contains(GC.PLACE_FAMILIAR))
			throw new GameException("You have already placed a familiar");
		List<FamilyMember> freeMembers = _player.getFreeMembers();
		FamilyMember selection = null;
		for (FamilyMember f : freeMembers){
			if (f.getColor().equals(familiarColour)){
				selection = f;
			}
		}
		if (selection == null){
			throw new GameException("You can't place this familiar!");
		}
		
		switch(position.getWhere()){
			case GC.TOWER :
				_theGame.getDynamicBar().placeInTower(selection, position.getRow(), position.getColumn());
				break;
			case GC.COUNCIL_PALACE :
				_theGame.getDynamicBar().placeCouncilPalace(selection);
				break;
			case GC.HARVEST :
				_theGame.getDynamicBar().placeWork(selection, GC.HARVEST);
				break;
			case GC.PRODUCTION :
				_theGame.getDynamicBar().placeWork(selection, GC.PRODUCTION);
				break;
			case GC.MARKET :
				_theGame.getDynamicBar().placeMarket(selection, position.getRow());
				break;
			default : throw new GameException("Invalid position");
		}
		
		try {
			_player.getClient().getConnectionHandler().sendInfo("Familiar placed!", _theGame.getBoard());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		actionsAlreadyDone.add(GC.PLACE_FAMILIAR);
	}
	
	public void endTurn() throws GameException{
		if (actionsAlreadyDone.contains(GC.END_TURN))
			throw new GameException("You have already ended turn");
		
		_player.getEffects().removeIf(eff -> eff.getSource().equals(GC.ACTION_SPACE));
		
		try {
			_player.getClient().getConnectionHandler().sendInfo("Ended turn.");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		_theGame.otherPlayersInfo("Player "+_player.getName()+" has ended his turn", _player);
		
		actionsAlreadyDone.clear();
		_theGame.getState().nextState();
		//TODO cambio stato
	}

	
	
		
}
