package misc;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import game.FamilyMember;
import game.GameBoard;
import game.LeaderCard;
import game.Player;
import game.Resource;

public interface ClientRemote extends Remote {

	public int spendCouncil(List<Resource> councilRewards) throws RemoteException;

	public int chooseFamiliar(List<FamilyMember> familiars, String message) throws RemoteException;

	public boolean ask(String message) throws RemoteException;

	public int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions) throws RemoteException;

	public int chooseLeader(String context, List<LeaderCard> tempList) throws RemoteException;

	public int chooseDashboardBonus(Map<String, List<Resource>> bonus) throws RemoteException;

	public void startTurn(GameBoard board, Player currentPlayer) throws RemoteException;

	public int askInt(String message, int min, int max) throws RemoteException;
	
	public void sendInfo(String infoMessage) throws RemoteException;
	
	public void sendInfo(String infoMessage, GameBoard board) throws RemoteException;
	
	public void sendInfo(String message, Player me) throws RemoteException;
	
	public void sendInfo(String message, GameBoard board, Player me) throws RemoteException;
	
	public void sendUUID(String uuid) throws RemoteException;
}
