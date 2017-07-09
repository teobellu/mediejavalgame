package misc;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import model.FamilyMember;
import model.GameBoard;
import model.LeaderCard;
import model.Player;
import model.Resource;

/**
 * @author Jacopo
 *
 */
public interface ClientRemote extends Remote, Serializable {

	/**
	 * Convert a council privilege
	 * @param councilRewards how to convert
	 * @return conversion selection
	 * @throws RemoteException
	 */
	public int spendCouncil(List<Resource> councilRewards) throws RemoteException;

	/**
	 * Choose a familiar
	 * @param familiars the familiars
	 * @param message the question
	 * @return familiar chosen
	 * @throws RemoteException
	 */
	public int chooseFamiliar(List<FamilyMember> familiars, String message) throws RemoteException;

	/**
	 * Ask a yes/no question
	 * @param message the question
	 * @return yes or no
	 * @throws RemoteException
	 */
	public boolean ask(String message) throws RemoteException;

	/**
	 * Choose how to convert
	 * @param realPayOptions what you lose
	 * @param realGainOptions what you gain
	 * @return conversion chosen
	 * @throws RemoteException
	 */
	public int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions) throws RemoteException;

	/**
	 * Choose a leader
	 * @param context initial leader's list or in-game leader's list
	 * @param tempList leader's list
	 * @return leader chosen
	 * @throws RemoteException
	 */
	public int chooseLeader(String context, List<LeaderCard> tempList) throws RemoteException;

	/**
	 * Choose initial personal bonus
	 * @param bonus list of bonuses
	 * @return bonus chosen
	 * @throws RemoteException
	 */
	public int chooseDashboardBonus(Map<String, List<Resource>> bonus) throws RemoteException;

	/**
	 * Start turn, and update board and player
	 * @param board the board
 	 * @param currentPlayer the player
	 * @throws RemoteException
	 */
	public void startTurn(GameBoard board, Player currentPlayer) throws RemoteException;

	/**
	 * Ask a int between two numbers
	 * @param message the question
	 * @param min the minimum value
	 * @param max the maximum value
	 * @return the int chosen
	 * @throws RemoteException
	 */
	public int askInt(String message, int min, int max) throws RemoteException;
	
	/**
	 * Send an info
	 * @param infoMessage the info
	 * @throws RemoteException
	 */
	public void sendInfo(String infoMessage) throws RemoteException;
	
	/**
	 * Send an info, and update the board
	 * @param infoMessage the info
	 * @param board
	 * @throws RemoteException
	 */
	public void sendInfo(String infoMessage, GameBoard board) throws RemoteException;
	
	/**
	 * Send an info, and update the player
	 * @param message the info
	 * @param me
	 * @throws RemoteException
	 */
	public void sendInfo(String message, Player me) throws RemoteException;
	
	/**
	 * Send an info, and update the board and the player
	 * @param message the info
	 * @param board
	 * @param me
	 * @throws RemoteException
	 */
	public void sendInfo(String message, GameBoard board, Player me) throws RemoteException;
	
	/**
	 * Send the uuid to the client
	 * @param uuid the uuid
	 * @throws RemoteException
	 */
	public void sendUUID(String uuid) throws RemoteException;
}
