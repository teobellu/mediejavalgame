package game;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import game.development.DevelopmentCard;
import game.state.State;
import game.state.StateStartingTurn;
import server.Client;
import server.Room;
import util.CommandStrings;
import util.Constants;

/**
 * @author Jacopo
 *
 */
public class Game implements Runnable {
	
	private ListenAction _listener;
	
	private List<Player> _players = new ArrayList<>();
	private List<Player> _afkPlayers = new ArrayList<>();

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
		/*
		_state = new StateStartingTurn(this);
		
		while(!isGameOver()){
			for(int i = 0;i<Constants.NUMBER_OF_FAMILIARS; i++){
				cycleState();
				_hasPlacedFamiliar = false;
				_state = new StateStartingTurn(this);
			}
			_turn++;
		}*/
		
//		List<Player> winners = gameInformation.endOfTheGameFindWinners();
//		winners.forEach(player -> player.getClient().getConnectionHandler().sendToClient("GG U WIN"));
	}

	public boolean isOver() {
		return _isOver;
	}
	
	public Player getCurrentPlayer(){
		return _state.getCurrenPlayer();
		//return _players.get(_phase);
	}
	
	public GameBoard getGameBoard() {
		return _board;
	}
	
	private void setupGame() throws RemoteException{
		Collections.shuffle(_players);
		
		setupLeaderCards();
		
		for (DevelopmentCard c : gameInformation.getDevelopmentDeck()){
			System.out.print(c.getName() +  " " + c.getAge() + " dice: " + c.getDice() + " ");
			c.getCost().forEach(e -> System.out.print("cost " + e.toString()));
			c.getRequirement().forEach(e -> System.out.print("req " + e.toString()));
			c.getImmediateEffect().forEach(e -> System.out.print("imm " + e.getIEffectBehavior().toString()));
			c.getPermanentEffect().forEach(e -> System.out.print("perm " + e.getIEffectBehavior().toString()));
			System.out.println("");
		}
		
		int x = 45;
		for (Player player : _players){
			player.addDevelopmentCard(gameInformation.getDevelopmentDeck().get(x));
			x++;
		}
		
		Collections.shuffle(gameInformation.getDevelopmentDeck());
		
		gameInformation.getExcommunicationDeck().get(12);
		
		ExcommunicationTile[] delay = new ExcommunicationTile[3];
		delay[0] = gameInformation.getExcommunicationDeck().get(12);
		delay[1] = gameInformation.getExcommunicationDeck().get(12);
		delay[2] = gameInformation.getExcommunicationDeck().get(12);
		
		//gameInformation.setExcommunicationTitlesOnBoard();
		
		
		_board.setExCard(delay);
		
		int n = 5;
		for (Player p : _players){
			p.gain(new Resource(GC.RES_WOOD, 20));
			p.gain(new Resource(GC.RES_STONES, 20));
			p.gain(new Resource(GC.RES_SERVANTS, 30));
			p.gain(new Resource(GC.RES_COINS, n+20));
			n++;
		}
		
		gameInformation.newPhase(1);
		
		setupDashboardBonus();
		
		_state = new StateStartingTurn(this);
		_state.setupState();
		_listener = new ListenAction(this);
	}
	
	public void setNewCards(){
		
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
	 
	public void manipulateInitialLeaderList(Client cli, String leader){
		try {
			for(int i = 0;i<_players.size();i++){
				if(_players.get(i).getClient().equals(cli)){
					Player player = _players.get(i);
					for(LeaderCard lc : _leaders){
						if(lc.getName().equals(leader)){
							player.addLeaderCard(lc);
							System.out.println("Added leader card "+lc.getName()+" to player "+ player.getName());
							if(_tempLeaderCardForEachPlayer.get(i).remove(lc.getName())){
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
	
	/**
	 * Non memorizza le liste...!
	 * @throws RemoteException
	 
	@Deprecated
	private void setupLeaderCards_SENZA_MEMORIA() throws RemoteException{
		System.out.println("Setup Leader cards");
		
        List<LeaderCard> tempList = new ArrayList<>();
        _leaders = gameInformation.getLeaderDeck().subList(0, _players.size() * Constants.LEADER_CARDS_PER_PLAYER);
        
		Collections.shuffle(_leaders);
		
		for(int j = 0;j<Constants.LEADER_CARDS_PER_PLAYER;j++){
			  for (int k = 0; k < _players.size(); k++){
				  for(int i = 0;i<Constants.LEADER_CARDS_PER_PLAYER - j;i++){
					  LeaderCard lc = _leaders.remove(0);
					  System.out.println("\nAggiunto "+lc.getName());
					  tempList.add(lc);
					  _leaders.add(lc);
				  }
				  
				  System.out.println("\nMando la lista al player "+k+"-esimo, ovvero "+_players.get(k).getName());
				  int selection = _players.get(k).getClient().getConnectionHandler().chooseLeader(tempList);
				  _players.get(k).addLeaderCard(tempList.get(selection));
				  if(_leaders.remove(tempList.get(selection))){
					  System.out.println("Rimossa la carta numero "+selection+" ovvero "+ tempList.get(selection));
				  }
				  
				  tempList.clear();
			  }
			  _players.add(_players.remove(0));
		}
	}
	*/
	private void setupLeaderCards() throws RemoteException{
		
        _leaders = gameInformation.getLeaderDeck();
        
		Collections.shuffle(_leaders);
		
		List<List<LeaderCard>> playerLists = new ArrayList<>();
		
		//estrae n mazzi da 4 carte, dove n = numero dei giocatori, e li salva
		for (@SuppressWarnings("unused") Player player : _players){
			List<LeaderCard> miniDeck = new ArrayList<>();
			for (int i = 0; i < Constants.LEADER_CARDS_PER_PLAYER; i++)
				miniDeck.add(_leaders.remove(0));
			playerLists.add(miniDeck);
		}
		
		//riceve gli input
		for (int i = 0; i < Constants.LEADER_CARDS_PER_PLAYER; i++){
			int x = 0;
			for (Player player : _players){
				List<LeaderCard> miniDeck = playerLists.get(x);
				int selection = player.getClient().getConnectionHandler().chooseLeader(CommandStrings.INITIAL_LEADER, miniDeck);
				player.addLeaderCard(miniDeck.get(selection));
				miniDeck.remove(selection);
				x++;
			}
			playerLists.add(playerLists.remove(0));
		}
	}
	
	private void setupDashboardBonus() throws RemoteException{
		System.out.println("Setup Personal Bonus");
		
		//ottengo i bonus della plancia giocatore
        Map<String, List<Resource>> bonus = gameInformation.getBonusPlayerDashBoard();
		
        //creo una lista di player invertita (vedi regolamento)
		List<Player> reversePlayers = new ArrayList<>(_players);
		Collections.reverse(reversePlayers);
        
		//chiedo ai giocatori di scegliere il loro bonus
		for (Player p : reversePlayers){
			int selection = 0;
			selection = p.getClient().getConnectionHandler().chooseDashboardBonus(bonus);
			p.setHarvestBonus(bonus.get(GC.HARVEST).get(selection));
			p.setProductionBonus(bonus.get(GC.PRODUCTION).get(selection));
			bonus.get(GC.HARVEST).remove(selection);
			bonus.get(GC.PRODUCTION).remove(selection);
		}
	}
	
	public synchronized void otherPlayersInfo(String message, Player excluded){
		_players.stream()
			.filter(player -> player != excluded)
			.forEach(player -> {
				try {
					player.getClient().getConnectionHandler().sendInfo(message);
				} catch (RemoteException e) {
					setAFK(player);
				}
			});
	}
	
	public synchronized void broadcastInfo(String message){
		_players.forEach(player -> {
			try {
				player.getClient().getConnectionHandler().sendInfo(message);
			} catch (RemoteException e) {
				setAFK(player);
			}
		});
	}
	
	
	/**
	 * Remove a player from the official players list, and saves him in a separate list 
	 * @param player the player removed
	 */
	private synchronized void setAFK(Player player) {
		for(Player p : _players){
			_afkPlayers.add(p);
			_players.remove(p);
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
	
	public void setBoard(GameBoard board) {
		this._board = board;
	}

	public ListenAction getListener() {
		return _listener;
	}

	public void setListener(ListenAction listener) {
		this._listener = listener;
	}

	protected void setPlayers(List<Player> nextPlayersTurn) {
		_players = nextPlayersTurn;
	}
}
