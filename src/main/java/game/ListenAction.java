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
	
	private void checkOut(String nickname, boolean removeAfk) throws GameException{
		if (!_player.getName().equals(nickname)){
			Player caller = null;
			for (Player p : _theGame.getPlayers()){
				if (p.getName().equals(nickname))
					caller = p;
			}
			if (caller == null)
				throw new GameException("You can't reconnect this time, sorry!");
			if (removeAfk)
				if (caller.isAfk()){
					caller.setAfk(false);
					throw new GameException("Successful reconnection!");
				}
			throw new GameException("It's not your turn! Please click \"End turn\" for reconnect !");
		}
		
	}

//	public GameBoard getGameBoard() {
//		return _theGame.getBoard();
//	}
//	
//	public Player getMe(String nickname) {
//		return _player;
//	}
	
	public void dropLeaderCard(String nickname, String leaderName) throws GameException{
		checkOut(nickname, false);
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
			_player.getClient().getConnectionHandler().sendInfo("Leader card dropped!", _player);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		_theGame.otherPlayersInfo("Player "+_player.getName()+" has dropped the Leader card "+leaderName, _player);
		
		actionsAlreadyDone.add(GC.DROP_LEADER);
	}
	
	public void activateLeaderCard(String nickname, String leaderName) throws GameException {
		checkOut(nickname, false);
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
		
		_theGame.otherPlayersInfo("Player "+_player.getName()+" has activated the Leader card " + leaderName, _player);
		
		actionsAlreadyDone.add(GC.ACTIVATE_LEADER);
	}
	
	public void placeFamiliar(String nickname, String familiarColour, Position position) throws GameException {
		checkOut(nickname, false);
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
			_player.getClient().getConnectionHandler().sendInfo("Familiar placed!", _theGame.getBoard(), _player);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		_theGame.otherPlayersInfo("Player "+_player.getName()+" has placed a familiar " + position.toString(), _player);
		
		actionsAlreadyDone.add(GC.PLACE_FAMILIAR);
	}
	
	public void endTurn(String nickname) throws GameException{
		checkOut(nickname, true);
		if (actionsAlreadyDone.contains(GC.END_TURN))
			throw new GameException("You have already ended turn");
		
		_player.getEffects().removeIf(eff -> eff.getSource().equals(GC.ACTION_SPACE));
		
		try {
			_player.getClient().getConnectionHandler().sendInfo("Ended turn.\n");
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
