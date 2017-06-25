package game.state;

import java.rmi.RemoteException;
import java.util.List;

import exceptions.GameException;
import game.FamilyMember;
import game.Game;
import game.ListenAction;
import game.Player;
import server.Client;

public abstract class State {
	
	protected boolean vatican = false; //TODO, per ora ignorarare
	protected int age;
	protected int phase;
	protected final Game _theGame; //TODO
	protected Player _player = null;
	protected Client _client;
	private List<Player> _players;
	
	public State(Game game){
		_theGame = game;
		//_player = _theGame.getCurrentPlayer();
		_players = _theGame.getPlayers();
	}
	
	public void setupState() {
		_player = _players.get(0);
		age = 1;
		phase = 1;
		vatican = false;
		_theGame.getDynamicBar().setPlayer(_players.get(0));
		_theGame.setListener(new ListenAction(_theGame));
		try {
			_player.getClient().getConnectionHandler().startTurn();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doState(){
		
	}
	
	public void nextState(){
		Player nextPlayer;
		int currentPlayerIndex = _players.indexOf(_player);
		if (currentPlayerIndex == _players.size()){
			nextPlayer = _players.get(0);
		}
		else {
			nextPlayer = _players.get(currentPlayerIndex + 1);
		}
		_theGame.getDynamicBar().setPlayer(nextPlayer);
		_player = nextPlayer;
		try {
			_player.getClient().getConnectionHandler().startTurn();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public abstract List<String> dropLeaderCard() throws GameException;
	
	public abstract void dropWhichLeaderCard(String leader) throws GameException;
	
	public abstract boolean endTurn() throws GameException;
	
	public abstract List<String> activateLeaderCard() throws GameException;

	//TODO alcune carte richiedono interazione
	public abstract void activateWhichLeaderCard(String leader) throws GameException;
	
	public abstract List<FamilyMember> placeFamiliar() throws GameException;
	
	public abstract List<String> placeWhichFamiliar(String familiar) throws GameException;
	
	//TODO string?
	public abstract void placeWhereFamiliar(String position) throws GameException;

	public Player getCurrenPlayer() {
		return _player;
	}

	
}