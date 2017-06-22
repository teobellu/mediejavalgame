package game;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.state.State;
import game.state.StateStartingTurn;
import server.Client;
import server.Room;
import util.Constants;

public class Game implements Runnable {

	List<List<String>> _tempLeaderCardForEachPlayer = new ArrayList<>();
	
	private List<Player> _players = new ArrayList<>();

	private GameBoard _board;

	private State _state;
	private int _turn;
	private int _phase;
	private boolean _isOver = false;
	private final Room _theRoom;
	
	private List<LeaderCard> _leaders = new ArrayList<>();
	
	private final DynamicAction _dynamicAction;
	
	private int cardLeft = 4;
	
	private boolean _hasPlacedFamiliar = false;
	
	private GameInformation gameInformation;
	
	public GameInformation getGameInformation() {
		return gameInformation;
	}

	public Game(Room room) {
		_theRoom = room;
		_turn = 0;
		_phase = 0;
		for(Client cli : _theRoom.getPlayers()){
			_players.add(new Player(cli));//TODO
		}
		_dynamicAction = new DynamicAction(this);
		gameInformation = new GameInformation(this);
	}
	
	@Override
	public void run() {
		//TODO
		
		try {
			setupGame();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		_state = new StateStartingTurn(this);
		
		while(!isGameOver()){
			for(int i = 0;i<Constants.NUMBER_OF_FAMILIARS; i++){
				cycleState();
				_hasPlacedFamiliar = false;
				_state = new StateStartingTurn(this);
			}
			_turn++;
		}
		
//		List<Player> winners = gameInformation.endOfTheGameFindWinners();
//		winners.forEach(player -> player.getClient().getConnectionHandler().sendToClient("GG U WIN"));
	}
	
	//x sonar
	private void cycleState(){
		for(_phase = 0;_phase<_players.size();_phase++)
			while(_state!=null)
				_state = _state.doState();
	}

	public boolean isOver() {
		return _isOver;
	}
	
	private boolean isGameOver(){
		if(_turn < Constants.MAX_TURN && _players.size()>1){
			return false;
		}
		return true;
	}
	
	public Player getCurrentPlayer(){
		return _players.get(_phase);
	}
	
	public GameBoard getGameBoard() {
		return _board;
	}
	
	private void setupGame() throws RemoteException{
		//TODO
		
		List<LeaderCard> tempList = new ArrayList<>();
        _leaders = gameInformation.getLeaderDeck().subList(0, _players.size() * Constants.LEADER_CARDS_PER_PLAYER);
        
		Collections.shuffle(_leaders);
		
		for(int j = 0;j<Constants.LEADER_CARDS_PER_PLAYER;j++){
			  for (int k = 0; k < _players.size(); k++){
				  for(int i = 0;i<Constants.LEADER_CARDS_PER_PLAYER - j;i++){
					  LeaderCard lc = _leaders.remove(0);
					  System.out.println("Aggiunto "+lc.getName());
					  tempList.add(lc);
					  _leaders.add(lc);
				  }
				  
				  System.out.println("Mando la lista al player "+k+"-esimo, ovvero "+_players.get(k).getName());
				  int selection = _players.get(k).getClient().getConnectionHandler().chooseLeader(tempList);
				  _players.get(k).addLeaderCard(tempList.get(selection));
				  tempList.clear();
			  }
			  _players.add(_players.remove(0));
		}
		
//        //cycle n times, n = number of players
//        for (int k = 0; k < _players.size(); k++){
//            //draw 4 cards
//            for (int i = 0; i < Constants.LEADER_CARDS_PER_PLAYER; i++){
//                tempList.add(_leaders.remove(0));
//            }
//            //ask players what card they want
//            for (Player p : _players){
//                int selection = 0;
//				selection = p.getClient().getConnectionHandler().chooseLeader(tempList);
//				p.addLeaderCard(tempList.get(selection));
//				tempList.remove(selection);
//            }
//            tempList.clear();
//            Player queuedPlayer = _players.get(0);
//            _players.remove(0);
//            _players.add(queuedPlayer);
//        }
		
		
		
//		try{
//			_leaders = gameInformation.getLeaderDeck().subList(0, _players.size() * Constants.LEADER_CARDS_PER_PLAYER);
//			Collections.shuffle(_leaders);
//			
//			for(int i = 0;i<_players.size();i++){
//				List<String> leadersnames = new ArrayList<>();
//				for(int j = i*Constants.LEADER_CARDS_PER_PLAYER;j<(i+1)*Constants.LEADER_CARDS_PER_PLAYER;j++){
//					System.out.println(_leaders.get(j).getName());
//					leadersnames.add(_leaders.get(j).getName());
//				}
//				
//				_players.get(i).getClient().getConnectionHandler().sendInitialLeaderList(leadersnames);
//				_tempLeaderCardForEachPlayer.add(i, leadersnames);
//			}
//			
//			do {
//				Thread tr = new Thread(new Runnable() {
//					@Override
//					public void run() {
//						boolean stop = false;
//						do {
//							for(List<String> list : _tempLeaderCardForEachPlayer){
//								for(String s : list){
//									System.out.println("Questa lista contiene "+s);
//								}
//								System.out.println("\n");
//								if(list.size()!=getLeft()){
//									System.out.println("Lista non valida");
//									stop = true;
//									break;
//								}
//							}
//							if(!stop){
//								System.out.println("Lista valida");
//								List<String> tmp = _tempLeaderCardForEachPlayer.get(_tempLeaderCardForEachPlayer.size()-1);
//								_tempLeaderCardForEachPlayer.add(0, tmp);
//								_tempLeaderCardForEachPlayer.remove(_tempLeaderCardForEachPlayer.size()-1);
//								return;
//							} else {
//								try {
//									Thread.sleep(1000);
//								} catch (InterruptedException e) {
//								}
//							}
//						} while (true);
//					}
//				});
//				tr.start();
//				
//				while(tr.isAlive()){
//					Thread.sleep(1000);
//				}
//				
//				for(int j = 0; j<_players.size(); j++){
//					System.out.println("Mando carte al client");
//					_players.get(j).getClient().getConnectionHandler().sendInitialLeaderList(_tempLeaderCardForEachPlayer.get(j));
//				}
//				
//				cardLeft--;
//				
//			} while (cardLeft>0);
//		} catch(Exception e){
//			_log.log(Level.SEVERE, e.getMessage(), e);
//		}
	}
	
	public int getLeft(){
		return cardLeft;
	}
	
	public boolean hasPlacedFamiliarYet(){
		return _hasPlacedFamiliar;
	}
	
	/**
	 * Add the chosen leader card to the right player, and removes it from the temporary list
	 * @param cli the client of the player who's choosing
	 * @param leader the name of the card chosen
	 */
	public void manipulateInitialLeaderList(Client cli, String leader){
		try {
			for(int i = 0;i<_players.size();i++){
				if(_players.get(i).getClient().equals(cli)){
					Player player = _players.get(i);
					for(LeaderCard lc : _leaders){
						if(lc.getName().equals(leader)){
							player.addLeaderCard(lc);
							System.out.println("Added leader card "+lc.getName()+" to player "+ player.getName());
							if(_tempLeaderCardForEachPlayer.get(i).remove(lc)){
								System.out.println("Rimossa carta con successo");
							} else {
								System.out.println("Errore nel rimuovere carta");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	public State getState(){
		return _state;
	}
	
	public void setState(State state){
		_state = state;
	}
	
	public DynamicAction getDynamicBar(){
		return _dynamicAction;
	}
	
	public List<Player> getPlayers() {
		return _players;
	}
	
	public GameBoard getBoard() {
		return _board;
	}
	
	private Logger _log = Logger.getLogger(Game.class.getName());
}
