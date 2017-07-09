package server;

import java.io.Serializable;
import java.rmi.RemoteException;
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

/**
 * RMI Connection Handler
 * @author Jacopo
 * @author Matteo
 */
public class RMIConnectionHandler extends ConnectionHandler implements ConnectionHandlerRemote, Serializable {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 992769740779261223L;

	/**
	 * The logger
	 */
	private transient Logger _log = Logger.getLogger(RMIConnectionHandler.class.getName());
	
	/**
	 * The client.Client remote interface
	 */
	private ClientRemote _clientConnectionHandler;
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
	}
	
	/* (non-Javadoc)
	 * @see misc.ConnectionHandlerRemote#setClientRemote(misc.ClientRemote)
	 */
	@Override
	public void setClientRemote(ClientRemote rmiConnectionServerHandler) throws RemoteException {
		_clientConnectionHandler = rmiConnectionServerHandler;
	}

	/* (non-Javadoc)
	 * @see misc.ConnectionHandlerRemote#addMeToGame(java.lang.String)
	 */
	@Override
	public boolean addMeToGame(String name) throws RemoteException {
		boolean returned = Server.getInstance().addMeToGame(this, name);
		if(returned){
			sendUUID(_client.getUUID());
		}
		return returned;
	}
	
	/* (non-Javadoc)
	 * @see misc.ConnectionHandlerRemote#sendConfigFile(java.lang.String)
	 */
	@Override
	public void sendConfigFile(String file) throws RemoteException {
		_configFile = file;
	}
	
	/* (non-Javadoc)
	 * @see server.ConnectionHandler#spendCouncil(java.util.List)
	 */
	@Override
	public int spendCouncil(List<Resource> councilRewards) throws RemoteException {
		return _clientConnectionHandler.spendCouncil(councilRewards);
	}
	
	/* (non-Javadoc)
	 * @see server.ConnectionHandler#chooseFamiliar(java.util.List, java.lang.String)
	 */
	@Override
	public int chooseFamiliar(List<FamilyMember> familiars, String message) throws RemoteException {
		return _clientConnectionHandler.chooseFamiliar(familiars, message);
	}

	/* (non-Javadoc)
	 * @see server.ConnectionHandler#askBoolean(java.lang.String)
	 */
	@Override
	public boolean askBoolean(String message) throws RemoteException {
		return _clientConnectionHandler.ask(message);
	}

	/* (non-Javadoc)
	 * @see server.ConnectionHandler#chooseConvert(java.util.List, java.util.List)
	 */
	@Override
	public int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions) throws RemoteException {
		return _clientConnectionHandler.chooseConvert(realPayOptions, realGainOptions);
	}
	
	/* (non-Javadoc)
	 * @see server.ConnectionHandler#chooseLeader(java.lang.String, java.util.List)
	 */
	@Override
	public int chooseLeader(String context, List<LeaderCard> tempList) throws RemoteException{
		return _clientConnectionHandler.chooseLeader(context, tempList);
	}
	
	/* (non-Javadoc)
	 * @see server.ConnectionHandler#chooseDashboardBonus(java.util.Map)
	 */
	@Override
	public int chooseDashboardBonus(Map<String, List<Resource>> bonus) throws RemoteException {
		return _clientConnectionHandler.chooseDashboardBonus(bonus);
	}

	/* (non-Javadoc)
	 * @see misc.ConnectionHandlerRemote#dropLeaderCard(java.lang.String)
	 */
	@Override
	public void dropLeaderCard(String leaderName) throws RemoteException {
		try {
			_theGame.getListener().dropLeaderCard(_client.getName(), leaderName);
		} catch (GameException e) {
			_log.log(Level.OFF, e.getMessage(), e);
			sendInfo(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see misc.ConnectionHandlerRemote#activateLeaderCard(java.lang.String)
	 */
	@Override
	public void activateLeaderCard(String leaderName) throws RemoteException {
		try {
			_theGame.getListener().activateLeaderCard(_client.getName(), leaderName);
		} catch (GameException e) {
			_log.log(Level.OFF, e.getMessage(), e);
			sendInfo(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see misc.ConnectionHandlerRemote#placeFamiliar(java.lang.String, game.Position)
	 */
	@Override
	public void placeFamiliar(String familiarColour, Position position) throws RemoteException {
		try {
			_theGame.getListener().placeFamiliar(_client.getName(), familiarColour, position);
		} catch (GameException e) {
			_log.log(Level.OFF, e.getMessage(), e);
			sendInfo(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see misc.ConnectionHandlerRemote#endTurn()
	 */
	@Override
	public void endTurn() throws RemoteException {
		try {
			_theGame.getListener().endTurn(_client.getName());
		} catch (GameException e) {
			_log.log(Level.OFF, e.getMessage(), e);
			sendInfo(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see server.ConnectionHandler#startTurn(game.GameBoard, game.Player)
	 */
	@Override
	public void startTurn(GameBoard board, Player currentPlayer) throws RemoteException {
		_clientConnectionHandler.startTurn(board, currentPlayer);
	}

	/* (non-Javadoc)
	 * @see server.ConnectionHandler#askInt(java.lang.String, int, int)
	 */
	@Override
	public int askInt(String message, int min, int max) throws RemoteException {
		return _clientConnectionHandler.askInt(message, min, max);
	}

	/* (non-Javadoc)
	 * @see server.ConnectionHandler#sendInfo(java.lang.String)
	 */
	@Override
	public void sendInfo(String infoMessage) throws RemoteException {
		_clientConnectionHandler.sendInfo(infoMessage);
	}
	
	/* (non-Javadoc)
	 * @see server.ConnectionHandler#sendInfo(java.lang.String, game.GameBoard)
	 */
	@Override
	public void sendInfo(String infoMessage, GameBoard board) throws RemoteException {
		_clientConnectionHandler.sendInfo(infoMessage, board);
	}

	/* (non-Javadoc)
	 * @see server.ConnectionHandler#sendInfo(java.lang.String, game.Player)
	 */
	@Override
	public void sendInfo(String message, Player me) throws RemoteException {
		_clientConnectionHandler.sendInfo(message, me);
	}

	/* (non-Javadoc)
	 * @see server.ConnectionHandler#sendInfo(java.lang.String, game.GameBoard, game.Player)
	 */
	@Override
	public void sendInfo(String message, GameBoard board, Player me) throws RemoteException {
		_clientConnectionHandler.sendInfo(message, board, me);
	}

	/* (non-Javadoc)
	 * @see server.ConnectionHandler#sendUUID(java.lang.String)
	 */
	@Override
	public void sendUUID(String uuid) throws RemoteException {
		_clientConnectionHandler.sendUUID(uuid);
	}

	/* (non-Javadoc)
	 * @see misc.ConnectionHandlerRemote#showVaticanSupport()
	 */
	@Override
	public void showVaticanSupport() throws RemoteException {
		try {
			_theGame.getListener().showVaticanSupport(_client.getName());
		} catch (GameException e) {
			_log.log(Level.OFF, e.getMessage(), e);
			sendInfo(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see misc.ConnectionHandlerRemote#activateOPTLeaders()
	 */
	@Override
	public void activateOPTLeaders() throws RemoteException {
		try {
			_theGame.getListener().playOPTLeaderCards(_client.getName());
		} catch (GameException e) {
			_log.log(Level.OFF, e.getMessage(), e);
			sendInfo(e.getMessage());
		}
	}
}
