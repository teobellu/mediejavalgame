package game;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.development.DevelopmentCard;
import game.state.State;
import game.state.StateStartingTurn;
import server.Client;
import server.Room;
import util.Constants;

public class Game implements Runnable {
	
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
		System.out.println("\n\n\n\n\n\n\nFINE SETUP");
		
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
		//for(_phase = 0;_phase<_players.size();_phase++)
			//while(_state!=null)
				//_state = _state.doState();
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
		//setupDashboardBonus();
		setupLeaderCards();
		List<DevelopmentCard> devDeck = gameInformation.getDevelopmentDeck();
		
		Collections.shuffle(devDeck);
		
		
		_board.generateDevelopmentCards(devDeck, 1);
		
		System.out.println(_board.getCard(1, 1).getName());
		
		setupDashboardBonus();
		 //facciamo finta che tocca al giocatore 1
		List<FamilyMember> testFM = new ArrayList<>();
		FamilyMember FM1 = new FamilyMember(GC.FM_BLACK);
		FamilyMember FM2 = new FamilyMember(GC.FM_ORANGE);
		testFM.add(FM1);
		testFM.add(FM2);
		_state = new StateStartingTurn(this);
		_players.get(0).setFreeMember(testFM);
		_players.get(0).getClient().getConnectionHandler().notifyTurn();
		
		
		//_players.forEach(player -> player.getClient().getConnectionHandler().sendBoard(_board));
		
		
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
				int selection = player.getClient().getConnectionHandler().chooseLeader(miniDeck);
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
		List<Player> reversePlayers = _players;
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

	
	private Logger _log = Logger.getLogger(Game.class.getName());
}
