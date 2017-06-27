package server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.GameException;
import game.FamilyMember;
import game.GameBoard;
import game.LeaderCard;
import game.Player;
import game.Position;
import game.Resource;
import misc.ClientRemote;
import misc.ConnectionHandlerRemote;

public class RMIConnectionHandler extends ConnectionHandler implements ConnectionHandlerRemote, Serializable {

	private ClientRemote _clientConnectionHandler;
	private Date _lastPing;
	private transient Logger _log = Logger.getLogger(RMIConnectionHandler.class.getName());
	
	@Override
	public void run() {
		
	}
	
	@Override
	public void setClientRemote(ClientRemote rmiConnectionServerHandler) throws RemoteException {
		_clientConnectionHandler = rmiConnectionServerHandler;
	}

	@Override
	public void ping() throws RemoteException {
		_lastPing = Date.from(Instant.now());	
	}
	
	@Override
	public void onConnect() throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean addMeToGame(String name) throws RemoteException {
		return Server.getInstance().addMeToGame(this, name);
	}
	
	@Override
	public void sendConfigFile(String file) throws RemoteException {
		_configFile = file;
	}
	
	public void setGame(){//TODO da chiamare questo metodo alla creazione del gioco(probabilmente nella room)
		if(_client!=null){
			_theGame = _client.getRoom().getGame();
		}
	}
	
	@Override
	public int spendCouncil(List<Resource> councilRewards) throws RemoteException {
		return _clientConnectionHandler.spendCouncil(councilRewards);
	}
	
	@Override
	public int chooseFamiliar(List<FamilyMember> familiars, String message) throws RemoteException {
		return _clientConnectionHandler.chooseFamiliar(familiars, message);
	}

	@Override
	public boolean ask(String message) throws RemoteException {
		return _clientConnectionHandler.ask(message);
	}

	@Override
	public int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions) throws RemoteException {
		return _clientConnectionHandler.chooseConvert(realPayOptions, realGainOptions);
	}
	
	@Override
	public int chooseLeader(String context, List<LeaderCard> tempList) throws RemoteException{
		return _clientConnectionHandler.chooseLeader(context, tempList);
	}
	
	@Override
	public int chooseDashboardBonus(Map<String, List<Resource>> bonus) throws RemoteException {
		return _clientConnectionHandler.chooseDashboardBonus(bonus);
	}

	//@Override TODO
	public void notifyTurn() throws RemoteException {
		//_clientConnectionHandler.notifyTurn(); TODO
	}

	@Override
	public GameBoard getBoard() throws RemoteException {
		return _theGame.getListener().getGameBoard();
	}

	@Override
	public Player getMe() throws RemoteException {
		return _theGame.getListener().getMe();
	}

	@Override
	public void dropLeaderCard(LeaderCard card) throws RemoteException, GameException {
		_theGame.getListener().dropLeaderCard(card);
	}

	@Override
	public void activateLeaderCard(LeaderCard card) throws RemoteException, GameException {
		_theGame.getListener().activateLeaderCard(card);
	}

	@Override
	public void placeFamiliar(FamilyMember familiar, Position position) throws RemoteException, GameException {
		_theGame.getListener().placeFamiliar(familiar, position);
	}

	@Override
	public void endTurn() throws RemoteException, GameException {
		_theGame.getListener().endTurn();
	}

	@Override
	public void startTurn(GameBoard board, Player currentPlayer) throws RemoteException {
		_clientConnectionHandler.startTurn(board, currentPlayer);
	}

	@Override
	public int askInt(String message, int min, int max) throws RemoteException {
		return _clientConnectionHandler.askInt(message, min, max);
	}
	
	
}
