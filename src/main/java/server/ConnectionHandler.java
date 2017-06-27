package server;

import java.rmi.RemoteException;
import java.util.ArrayList;
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
	
	public List<String> getTempLeaders(){
		return _temporaryLeaders;
	}
	
	private List<String> _temporaryLeaders = new ArrayList<>();
	
	public abstract void startTurn(GameBoard board, Player currentPlayer) throws RemoteException;
	
	//TODO GAME METHOD
	public abstract int spendCouncil(List<Resource> councilRewards) throws RemoteException;

	//TODO GAME METHOD
	public abstract int chooseFamiliar(List<FamilyMember> familiars, String message) throws RemoteException;

	//TODO GAME METHOD manda un messaggio al quale il giocatore deve rispondere si o no (true or false)
	public abstract boolean ask(String message) throws RemoteException;

	//TODO GAME METHOD chiedi al giocatore un indice tra 0 e realPayOption.size() - 1
	//Ho già chiesto al giocatore se vuole convertire o no, bisogna solo chiedergli un indice
	public abstract int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions) throws RemoteException;

	public abstract int chooseLeader(String context, List<LeaderCard> tempList)  throws RemoteException;

	//TODO GAME METHOD, SELEZIONE TESSERA BONUS
	public abstract int chooseDashboardBonus(Map<String, List<Resource>> bonus) throws RemoteException;
	
	public abstract int askInt(String message, int min, int max) throws RemoteException;
	
	protected Client _client;
	protected volatile boolean _isRunning;
	protected String _configFile = null;
	protected transient Game _theGame = null;

	
}
