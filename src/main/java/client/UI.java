package client;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import exceptions.GameException;
import game.FamilyMember;
import game.GameBoard;
import game.LeaderCard;
import game.Player;
import game.Position;
import game.Resource;
import game.development.DevelopmentCard;

/**
 * Interface for the client
 * @author Jacopo
 * @author Matteo
 */
public interface UI extends Runnable{
	
	/**
	 * Display an info message
	 * @param infoMessage the info
	 */
	public void showInfo(String infoMessage);
	
	/**
	 * Display an info message, with a board update
	 * @param infoMessage the info
	 * @param board the board
	 */
	public void showInfo(String infoMessage, GameBoard board);
	
	/**
	 * Display an info message, with a player update
	 * @param message the info
	 * @param me the player
	 */
	public void showInfo(String message, Player me);
	
	/**
	 * Display an info message, with a board and player update
	 * @param message the info
	 * @param board the board
	 * @param me the player
	 */
	public void showInfo(String message, GameBoard board, Player me);
	
	/** Setup the connection
	 * @param connectionType socket or rmi
	 * @param host the server address
	 * @param port the server port
	 */
	public void setConnection(String connectionType, String host, int port);
	
	/**
	 * Allows the player to spend his council's privilege
	 * @param options List of options
	 * @return Player's selection
	 */
	public int spendCouncil(List<Resource> options);
	
	/**
	 * Choose how to pay a card
	 * @param card the card
	 * @return the cost choosen
	 */
	public int chooseCardCost(DevelopmentCard card);

	/**
	 * Activate the leader card
	 * @param leaderName the card
	 * @throws RemoteException connection error
	 * @throws GameException if you cannot activate this card
	 */
	public void activateLeaderCard(String leaderName) throws RemoteException;
	
	/**
	 * Show a message and allows the player to choose a familiar from a list
	 * @param familiars List of family members
	 * @param message Message to show
	 * @return Selection, index of the list
	 */
	public int chooseFamiliar(List<FamilyMember> familiars, String message);

	/**
	 * Allows the player to answer true or false to a message
	 * @param message Question
	 * @return Answer of the player: yes or no (true or false)
	 */
	public boolean askBoolean(String message);

	/**
	 * Allows the player to choose what exchange he prefer, for example for building cards
	 * @param realPayOptions Options to pay
	 * @param realGainOptions Options to gain
	 * @return selection, index of the lists
	 */
	public int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions);

	/**
	 * Choose a leader card
	 * @param context initial leader, or in-game action
	 * @param tempList the list
	 * @return the card chosen
	 */
	public int chooseLeader(String context, List<LeaderCard> tempList);

	/**
	 * Choose an initial personal bonus
	 * @param bonus list of bonus
	 * @return the bonus chosen
	 */
	public int chooseDashboardBonus(Map<String, List<Resource>> bonus);

	/**
	 * Start a turn and update board and player
	 * @param board the board
	 * @param me the player
	 */
	public void startTurn(GameBoard board, Player me);

	/**
	 * Ask an int between min and max
	 * @param message the question asked
	 * @param min the minimum
	 * @param max the maximum
	 * @return the int chosen
	 */
	public int askInt(String message, int min, int max);

	/**
	 * Add me to a game
	 * @param username player's name
	 * @throws GameException you cannot join a game
	 */
	public void addMeToGame(String username) throws GameException;
	
	/**
	 * Drop a leader card
	 * @param leaderName the card dropped
	 * @throws RemoteException connection error
	 * @throws GameException you cannot drop this card
	 */
	public void dropLeaderCard(String leaderName) throws RemoteException;

	/**
	 * End the turn
	 * @throws RemoteException connection error
	 * @throws GameException you cannot end the turn
	 */
	public void endTurn() throws RemoteException;
	
	/**
	 * Place a familiar on the board
	 * @param familiarColour the familiar
	 * @param position where to put it
	 * @throws RemoteException connection error
	 * @throws GameException you cannot put a familiar there
	 */
	public void placeFamiliar(String familiarColour, Position position) throws RemoteException;

	/**
	 * Set the uuid of this client
	 * @param uuid the uuid
	 */
	public void setUUID(String uuid);
	
	/**
	 * On reconnected
	 */
	public void reconnected();
	
	/**
	 * Show your support to the Vatican TODO
	 * @throws RemoteException
	 * @throws GameException
	 */
	public void showVaticanSupport() throws RemoteException;

	/**
	 * Activate OPT Leaders
	 * @throws RemoteException
	 */
	public void activateOPTLeaders() throws RemoteException;
	
}