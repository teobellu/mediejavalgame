package game;

import java.rmi.RemoteException;
import java.util.List;

import exceptions.GameException;

public class ListenAction{
	
	protected final Game _theGame;
	protected final Player _player;
	
	public ListenAction(Game game){
		_theGame = game;
		_player = _theGame.getCurrentPlayer();
	}

	public GameBoard getGameBoard() {
		return _theGame.getBoard();
	}
	
	public Player getMe() {
		return _player;
	}
	
	public void dropLeaderCard(LeaderCard card) throws GameException{
		List<LeaderCard> playerLeaders = _player.getLeaderCards();
		if (!playerLeaders.contains(card))
			throw new GameException("You can't discard this card!");
		try {
			_theGame.getDynamicBar().discardLeaderCard(card);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
		}
		//TODO avviso il player che è tutto ok
		//TODO avviso gli altri player
		//TODO salvo qui dentro che ho già fatto quest'azione
	}
	
	public void activateLeaderCard(LeaderCard card) throws GameException {
		List<LeaderCard> activableLeaders = _player.getActivableLeaderCards();
		if (!activableLeaders.contains(card))
			throw new GameException("You can't activate this card!");
		_theGame.getDynamicBar().activateLeaderCard(card);
		//TODO avviso il player che è tutto ok
		//TODO avviso gli altri player
		//TODO salvo qui dentro che ho già fatto quest'azione
	}
	
	public void placeFamiliar(FamilyMember familiar, Position position) throws GameException {
		List<FamilyMember> freeMembers = _player.getFreeMember();
		FamilyMember selection = null;
		for (FamilyMember f : freeMembers){
			System.out.println("color free = " + f.getColor());
			if (f.getColor().equals(familiar.getColor())){
				selection = f;
			}
		}
		System.out.println("size of free = " + freeMembers.size());
		System.out.println("color of me = " + familiar.getColor());
		if (selection.getColor() == "aa"){
			throw new GameException("You can't place this familiar!");
		}
		else{
			familiar = selection;
		}
		/*
		if (!freeMembers.contains(familiar))
			throw new GameException("You can't place this familiar!");
			*/
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
		System.out.println("player " + _player.getName() + " has coins = " + _player.getResource(GC.RES_COINS));
		//TODO avviso il player che è tutto ok
		//TODO avviso gli altri player
		//TODO salvo qui dentro che ho già fatto quest'azione
	}
	
	public void endTurn() throws GameException{
		_theGame.getState().nextState();
		//TODO avviso il player che è tutto ok
		//TODO avviso gli altri player
		//TODO svuoto la lista delle azioni
		//TODO cambio stato
	}
	
		
}
