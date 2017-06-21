package misc;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import game.FamilyMember;
import game.LeaderCard;
import game.Resource;

public interface ClientRemote extends Remote {
	
	public void startTurn() throws RemoteException;

	public int sendInitialLeaderList(List<String> leadersList) throws RemoteException;

	public int spendCouncil(List<Resource> councilRewards) throws RemoteException;

	public int chooseFamiliar(List<FamilyMember> familiars, String message) throws RemoteException;

	public boolean ask(String message) throws RemoteException;

	public int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions) throws RemoteException;

	public int chooseLeader(List<LeaderCard> tempList) throws RemoteException;
	
}
