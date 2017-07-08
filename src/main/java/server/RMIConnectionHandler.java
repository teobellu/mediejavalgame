package server;

import java.io.Serializable;
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
import misc.ClientRemote;
import misc.ConnectionHandlerRemote;

public class RMIConnectionHandler extends ConnectionHandler implements ConnectionHandlerRemote, Serializable {

	private ClientRemote _clientConnectionHandler;
	
	@Override
	public void run() {
		
	}
	
	@Override
	public void setClientRemote(ClientRemote rmiConnectionServerHandler) throws RemoteException {
		_clientConnectionHandler = rmiConnectionServerHandler;
	}

	@Override
	public boolean addMeToGame(String name) throws RemoteException {
		boolean returned = Server.getInstance().addMeToGame(this, name);
		if(returned){
			sendUUID(_client.getUUID());
		}
		return returned;
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
	public boolean askBoolean(String message) throws RemoteException {
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

	@Override
	public GameBoard getBoard() throws RemoteException {
		return _theGame.getListener().getGameBoard();
	}

	@Override
	public Player getMe() throws RemoteException {
		return _theGame.getListener().getMe(_client.getName());
	}

	@Override
	public void dropLeaderCard(String leaderName) throws RemoteException {
		try {
			_theGame.getListener().dropLeaderCard(_client.getName(), leaderName);
		} catch (GameException e) {
			sendInfo(e.getMessage());
		}
	}

	@Override
	public void activateLeaderCard(String leaderName) throws RemoteException {
		try {
			_theGame.getListener().activateLeaderCard(_client.getName(), leaderName);
		} catch (GameException e) {
			sendInfo(e.getMessage());
		}
	}

	@Override
	public void placeFamiliar(String familiarColour, Position position) throws RemoteException {
		try {
			_theGame.getListener().placeFamiliar(_client.getName(), familiarColour, position);
		} catch (GameException e) {
			sendInfo(e.getMessage());
		}
	}

	@Override
	public void endTurn() throws RemoteException {
		try {
			_theGame.getListener().endTurn(_client.getName());
		} catch (GameException e) {
			sendInfo(e.getMessage());
		}
	}

	@Override
	public void startTurn(GameBoard board, Player currentPlayer) throws RemoteException {
		_clientConnectionHandler.startTurn(board, currentPlayer);
	}

	@Override
	public int askInt(String message, int min, int max) throws RemoteException {
		return _clientConnectionHandler.askInt(message, min, max);
	}

	@Override
	public void sendInfo(String infoMessage) throws RemoteException {
		_clientConnectionHandler.sendInfo(infoMessage);
	}
	
	@Override
	public void sendInfo(String infoMessage, GameBoard board) throws RemoteException {
		_clientConnectionHandler.sendInfo(infoMessage, board);
	}

	@Override
	public void sendInfo(String message, Player me) throws RemoteException {
		_clientConnectionHandler.sendInfo(message, me);
	}

	@Override
	public void sendInfo(String message, GameBoard board, Player me) throws RemoteException {
		_clientConnectionHandler.sendInfo(message, board, me);
	}

	@Override
	public void sendUUID(String uuid) throws RemoteException {
		_clientConnectionHandler.sendUUID(uuid);
	}
}
