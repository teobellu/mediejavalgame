package game;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
	
	private Player _currentPlayer;

	private GameBoard _board;

	private long turnTimeout;
	private State _state;
	
	protected int age; //1,2,3
	protected int phase; //
	protected int countTurn;
	
	private boolean _isOver = false;
	private final Room _theRoom;
	
	ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	
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
		age = 1;
		phase = 1;
		countTurn = 1;
		
		List<String> playerColours = new ArrayList<>();
		for(String color : GC.PLAYER_COLOURS){
			playerColours.add(color);
		}
		Collections.shuffle(playerColours);
		
		for(Client cli : _theRoom.getPlayers()){
			_players.add(new Player(cli, playerColours.remove(0)));
		}
		
		_currentPlayer = _players.get(0);
		
		_dynamicAction = new DynamicAction(this);
		gameInformation = new GameInformation(this);
	}
	
	@Override
	public void run() {
		try {
			setupGame();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i = 0;i<3;i++){//fasi
			for(int j = 0;j<2;j++){//turni per fase
				for(int k=0;k<Constants.NUMBER_OF_FAMILIARS;k++){//familiari per player
					for(int l=0;l<_players.size();l++){//numero di players
						_state = new State(this);
						
						Thread tr = new Thread(_state);
						synchronized (this) {
							tr.start();
							executor.schedule(new Runnable() {
								
								@Override
								public void run() {
									if(!tr.isInterrupted()){
										setAFK(_currentPlayer);
									}
								}
							}, 60*3, TimeUnit.SECONDS);
						}
						
						
					}
				}
			}
		}
		
		_state.startTurn(_currentPlayer);
		
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
	
	public GameBoard getGameBoard() {
		return _board;
	}
	
	private void setupGame() throws RemoteException{
		Collections.shuffle(_players);
		
		setupLeaderCards();
		
		for (DevelopmentCard c : gameInformation.getDevelopmentDeck()){
			System.out.print(c.getName() +  " " + c.getAge() + " dice: " + c.getDice() + " ");
			c.getCosts().forEach(e -> System.out.print("cost " + e.toString()));
			c.getRequirement().forEach(e -> System.out.print("req " + e.toString()));
			c.getImmediateEffect().forEach(e -> System.out.print("imm " + e.getIEffectBehavior().toString()));
			c.getPermanentEffect().forEach(e -> System.out.print("perm " + e.getIEffectBehavior().toString()));
			System.out.println("");
		}
		
		Collections.shuffle(gameInformation.getDevelopmentDeck());
		/*
		 TODO non cancellare per il momento
		gameInformation.getExcommunicationDeck().get(12);
		
		ExcommunicationTile[] delay = new ExcommunicationTile[3];
		delay[0] = gameInformation.getExcommunicationDeck().get(12);
		delay[1] = gameInformation.getExcommunicationDeck().get(12);
		delay[2] = gameInformation.getExcommunicationDeck().get(12);
		
		_board.setExCard(delay);
		*/
		
		gameInformation.setExcommunicationTitlesOnBoard();
		
		
		
		
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
	}
	
	public int getLeft(){
		return cardLeft;
	}
	
	public boolean hasPlacedFamiliarYet(){
		return _hasPlacedFamiliar;
	}
	
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
	
	public void setMeAFK(String name){
		for(Player p : _players){
			if(p.getName().equals(name)){
				setAFK(p);
			}
		}
	}

	public void startState(){
		//TODO wip
		executor.schedule(new Runnable() {
			
			@Override
			public void run() {
				setAFK(getCurrentPlayer());
			}
		}, 60*3, TimeUnit.SECONDS);//3 minuti
		
		
	}
	
	public void nextState(){

		Player nextPlayer = getNextPlayer();
		
		if (countTurn % (_players.size() * 4) == 0){//TODO non e' 4.
			//TODO che è???
			for(Player p : getGameInformation().getLatePlayersTurn()){
				setupNewTurn(p);
				getGameInformation().getLatePlayersTurn().removeIf(item -> item ==p);
				getGameInformation().getTailPlayersTurn().add(p);
			}
			
			if (phase == 2){
				System.out.println("VATICAN PHASE");
				for (Player p : _players){
					getDynamicBar().setPlayer(p);
					getListener().setPlayer(p);
					getDynamicBar().showVaticanSupport(age);
				}
				phase = 0;
				age++;
			}
			System.out.println("NEXT PHASE");
			getGameInformation().newPhase(age);
			nextPlayer = getPlayers().get(0);
			phase++;
		}
		countTurn++;
		if (age == 4){
			age = 3;
			List<Player> players = getGameInformation().endOfTheGameFindWinners();
			players.forEach(player -> {
				try {
					player.getClient().getConnectionHandler().sendInfo("You Win!");
					//TODO esci dal gioco
				} catch (RemoteException e) {
					//Do nothing, it's the end of the game
				}
			});
			
		}
		getDynamicBar().setPlayer(nextPlayer);

		getListener().setPlayer(nextPlayer);

		_currentPlayer = nextPlayer;
		
		try {
			_currentPlayer.getClient().getConnectionHandler().startTurn(_board, _currentPlayer);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			setAFK(_currentPlayer);
			if(_players.size()>1){
				nextState();
			} else {
				//TODO rimane un solo player, vincitore
			}
		}
	}
	
	public void setupNewTurn(Player nextPlayer){
		getDynamicBar().setPlayer(nextPlayer);
		getListener().setPlayer(nextPlayer);
		getDynamicBar().startTurn();
	}
	
	private Player getNextPlayer(){
		int currentPlayerIndex = _players.indexOf(_currentPlayer);
		if (currentPlayerIndex == _players.size() - 1)
			return _players.get(0);
		Player next = _players.get(currentPlayerIndex + 1);
		if (getGameInformation().getTailPlayersTurn().contains(next)){
			getGameInformation().getTailPlayersTurn().removeIf(player -> player == next);
			getGameInformation().getLatePlayersTurn().add(next);
			_currentPlayer = next;
			countTurn++;
			return getNextPlayer();
		}
		return _players.get(currentPlayerIndex + 1);
	}
	
	public long getTurnTimeout() {
		return turnTimeout;
	}

	public void setTurnTimeout(long timeout) {
		this.turnTimeout = timeout;
	}
}
