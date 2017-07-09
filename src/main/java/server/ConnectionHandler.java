package server;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import game.FamilyMember;
import game.Game;
import game.GameBoard;
import game.LeaderCard;
import game.Player;
import game.Resource;

public abstract class ConnectionHandler implements Runnable {
	public void setClient(Client client){
		_client = client;
	}
	
	public boolean isRunning(){
		return _isRunning;
	}
	
	public void shutdown(){
		_isRunning = false;
	}
	
	public String getConfigFile(){
		if(_configFile==null){
			return "";
		} else {
			return _configFile;
		}
	}
	
	public void setGame(){
		if(_client!=null){
			_theGame = _client.getRoom().getGame();
		}
	}
	
	public abstract void startTurn(GameBoard board, Player currentPlayer) throws RemoteException;
	
	public abstract void sendUUID(String uuid) throws RemoteException;
	
	public abstract int spendCouncil(List<Resource> councilRewards) throws RemoteException;

	public abstract int chooseFamiliar(List<FamilyMember> familiars, String message) throws RemoteException;

	public abstract boolean askBoolean(String message) throws RemoteException;

	public abstract int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions) throws RemoteException;

	public abstract int chooseLeader(String context, List<LeaderCard> tempList)  throws RemoteException;

	public abstract int chooseDashboardBonus(Map<String, List<Resource>> bonus) throws RemoteException;
	
	public abstract int askInt(String message, int min, int max) throws RemoteException;
	
	public abstract void sendInfo(String infoMessage) throws RemoteException;
	
	public abstract void sendInfo(String infoMessage, GameBoard board) throws RemoteException;
	
	public abstract void sendInfo(String message, Player me) throws RemoteException;
	
	public abstract void sendInfo(String message, GameBoard board, Player me) throws RemoteException;
	
	protected Client _client;
	protected volatile boolean _isRunning;
	protected String _configFile = null;
	protected Game _theGame = null;
}
