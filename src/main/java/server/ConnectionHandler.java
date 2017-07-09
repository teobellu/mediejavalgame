package server;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import model.FamilyMember;
import model.GameBoard;
import model.LeaderCard;
import model.Player;
import model.Resource;
import server.game.Game;

/**
 * Abstract connection handler
 * @author Jacopo
 * @author Matteo
 *
 */
public abstract class ConnectionHandler implements Runnable {
	/**
	 * Set the client associated
	 * @param client the client
	 */
	public void setClient(Client client){
		_client = client;
	}
	
	/**
	 * Am I running?
	 * @return
	 */
	public boolean isRunning(){
		return _isRunning;
	}
	
	/**
	 * Close this handler
	 */
	public void shutdown(){
		_isRunning = false;
	}
	
	/**
	 * Get the associated config file
	 * @return the file, or an empty string
	 */
	public String getConfigFile(){
		if(_configFile==null){
			return "";
		} else {
			return _configFile;
		}
	}
	
	/**
	 * Set the associated game
	 */
	public void setGame(){
		if(_client!=null){
			_theGame = _client.getRoom().getGame();
		}
	}
	
	/**
	 * Start the turn, and update player and board
	 * @param board the board
	 * @param currentPlayer the player
	 * @throws RemoteException
	 */
	public abstract void startTurn(GameBoard board, Player currentPlayer) throws RemoteException;
	
	/**
	 * Send an uuid
	 * @param uuid the uuid
	 * @throws RemoteException
	 */
	public abstract void sendUUID(String uuid) throws RemoteException;
	
	/**
	 * Spend a council reward
	 * @param councilRewards how to spend it
	 * @return the conversion chosen
	 * @throws RemoteException
	 */
	public abstract int spendCouncil(List<Resource> councilRewards) throws RemoteException;

	/**
	 * Choose a familiar
	 * @param familiars the familiars
	 * @param message the question
	 * @return the chosen one
	 * @throws RemoteException
	 */
	public abstract int chooseFamiliar(List<FamilyMember> familiars, String message) throws RemoteException;

	/**
	 * Ask a yes/no question
	 * @param message the question
	 * @return yes or no
	 * @throws RemoteException
	 */
	public abstract boolean askBoolean(String message) throws RemoteException;

	/**
	 * Choose how to convert resources
	 * @param realPayOptions what to pay
	 * @param realGainOptions what to gain
	 * @return the conversion chosen
	 * @throws RemoteException
	 */
	public abstract int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions) throws RemoteException;

	/**
	 * Choose leader from a list
	 * @param context initial leader or choose leader
	 * @param tempList the list
	 * @return the chosen one
	 * @throws RemoteException
	 */
	public abstract int chooseLeader(String context, List<LeaderCard> tempList)  throws RemoteException;

	/**
	 * Choose initial bonus
	 * @param bonus list of bonuses
	 * @return the chosen one
	 * @throws RemoteException
	 */
	public abstract int chooseDashboardBonus(Map<String, List<Resource>> bonus) throws RemoteException;
	
	/**
	 * Ask an int between a max and a minimum
	 * @param message the question
	 * @param min the minimum
	 * @param max the maximum
	 * @return the int chosen
	 * @throws RemoteException
	 */
	public abstract int askInt(String message, int min, int max) throws RemoteException;
	
	/**
	 * Send an info
	 * @param infoMessage the info
	 * @throws RemoteException
	 */
	public abstract void sendInfo(String infoMessage) throws RemoteException;
	
	/**
	 * Send an info and update the board
	 * @param infoMessage the info
	 * @param board the board
	 * @throws RemoteException
	 */
	public abstract void sendInfo(String infoMessage, GameBoard board) throws RemoteException;
	
	/**
	 * Send an info and update the player
	 * @param message the info
	 * @param me the player
	 * @throws RemoteException
	 */
	public abstract void sendInfo(String message, Player me) throws RemoteException;
	
	/**
	 * Send an info and update the board and the player
	 * @param message the info
	 * @param board the board
	 * @param me the player
	 * @throws RemoteException
	 */
	public abstract void sendInfo(String message, GameBoard board, Player me) throws RemoteException;
	
	/**
	 * the client associated
	 */
	protected Client _client;
	/**
	 * Am i running?
	 */
	protected volatile boolean _isRunning;
	/**
	 * the configfile from the client
	 */
	protected String _configFile = null;
	/**
	 * the associated game
	 */
	protected Game _theGame = null;
}
