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

public interface UI extends Runnable{
	
	public void showInfo(String infoMessage) throws RemoteException;
	
	public void showInfo(String infoMessage, GameBoard board) throws RemoteException;
	
	public void showInfo(String message, Player me) throws RemoteException;
	
	public void showInfo(String message, GameBoard board, Player me) throws RemoteException;
	
	public void setConnection(String connectionType, String host, int port);
	
	/**
	 * Allows the player to spend his council's privilege
	 * @param options List of options
	 * @return Player's selection
	 */
	public int spendCouncil(List<Resource> options);
	
	//se la carta ha più costi, segli quale costo
	public int chooseCardCost(DevelopmentCard card);

	//scegli se attivare una carta
	public void activateLeaderCard(String leaderName) throws RemoteException, GameException;
	
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

	public int chooseLeader(String context, List<LeaderCard> tempList);

	public int chooseDashboardBonus(Map<String, List<Resource>> bonus);

	public void startTurn(GameBoard board, Player me);

	public int askInt(String message, int min, int max);

	void addMeToGame(String username) throws GameException;
	
	public void dropLeaderCard(String leaderName) throws RemoteException, GameException;

	public void endTurn() throws RemoteException, GameException;
	
	public void placeFamiliar(String familiarColour, Position position) throws RemoteException, GameException;

	public void setUUID(String uuid);
	
}