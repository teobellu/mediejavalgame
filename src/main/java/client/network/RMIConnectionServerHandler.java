package client.network;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import misc.ClientRemote;
import misc.ConnectionHandlerRemote;
import misc.ServerRemote;
import model.FamilyMember;
import model.GameBoard;
import model.LeaderCard;
import model.Player;
import model.Position;
import model.Resource;
import util.Constants;

/**
 * RMI Connection handler
 * @author Jacopo
 *
 */
public class RMIConnectionServerHandler extends ConnectionServerHandler implements ClientRemote {
	
	//TODO da rimuovere
	private boolean primo = true;
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 3638341358577466527L;

	private ConnectionHandlerRemote _connectionHandler;
	private transient Logger _log = Logger.getLogger(RMIConnectionServerHandler.class.getName());

	public RMIConnectionServerHandler(String host, int port) {
		super(host, port);
		
		if(port==0){
			_port = Registry.REGISTRY_PORT;
		}
		
		try {
			Registry _registry = LocateRegistry.getRegistry(_host, _port);
			
			for(int i = 1; i <= 1000; i++){
				try{
					UnicastRemoteObject.exportObject((ClientRemote)this, Constants.DEFAULT_SOCKET_PORT + i);//TODO da rivedere
					break;
				} catch(ExportException e){
					if(i == 1000){
						_log.log(Level.SEVERE, e.getMessage(), e);
					}
					continue;
				}
			}
			
			ServerRemote _serverRMI = (ServerRemote) _registry.lookup(Constants.RMI);
			
			_connectionHandler = (ConnectionHandlerRemote) _serverRMI.onConnect();
			
			_connectionHandler.setClientRemote(this);
			
			_log.info("RMIConnection is up");
			
			_isRunning = true;
			
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	/* (non-Javadoc)
	 * @see client.network.ConnectionServerHandler#attemptReconnection(java.lang.String)
	 */
	@Override
	public void attemptReconnection(String uuid) throws RemoteException {
		try {
			Registry registry = LocateRegistry.getRegistry(_host, _port);
			ServerRemote serverRMI = (ServerRemote) registry.lookup(Constants.RMI);
			
			//TODO non so se sia necessario
//			for(int i = 1; i <= 1000; i++){
//				try{
//					UnicastRemoteObject.exportObject((ClientRemote)this, Constants.DEFAULT_SOCKET_PORT + i);
//					break;
//				} catch(ExportException e){
//					if(i == 1000){
//						_log.log(Level.SEVERE, e.getMessage(), e);
//					}
//					continue;
//				}
//			}
			
			_connectionHandler = (ConnectionHandlerRemote) serverRMI.onReconnect(uuid);
			_connectionHandler.setClientRemote(this);
			
			_log.info("Reconnected succesfully");
			_ui.showInfo("Reconnected succesfully");
//			return true;
		} catch (NotBoundException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
//		return false;
	}
	
	@Override
	public void run() {
		//empty method
	}

	/* (non-Javadoc)
	 * @see client.network.ConnectionServerHandler#addMeToGame(java.lang.String)
	 */
	@Override
	public boolean addMeToGame(String name) throws RemoteException {
		return _connectionHandler.addMeToGame(name);
	}
	
	/* (non-Javadoc)
	 * @see client.network.ConnectionServerHandler#sendConfigFile(java.lang.String)
	 */
	@Override
	public void sendConfigFile(String file) throws RemoteException {
		_connectionHandler.sendConfigFile(file);
	}
	
	/* (non-Javadoc)
	 * @see misc.ClientRemote#spendCouncil(java.util.List)
	 */
	@Override
	public int spendCouncil(List<Resource> councilRewards) throws RemoteException {
		return _ui.spendCouncil(councilRewards);
	}
	
	/* (non-Javadoc)
	 * @see misc.ClientRemote#chooseFamiliar(java.util.List, java.lang.String)
	 */
	@Override
	public int chooseFamiliar(List<FamilyMember> familiars, String message) throws RemoteException {
		return _ui.chooseFamiliar(familiars, message);
	}

	/* (non-Javadoc)
	 * @see misc.ClientRemote#ask(java.lang.String)
	 */
	@Override
	public boolean ask(String message) throws RemoteException {
		return _ui.askBoolean(message);
	}

	/* (non-Javadoc)
	 * @see misc.ClientRemote#chooseConvert(java.util.List, java.util.List)
	 */
	@Override
	public int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions) throws RemoteException {
		return _ui.chooseConvert(realPayOptions, realGainOptions);
	}

	/* (non-Javadoc)
	 * @see misc.ClientRemote#chooseLeader(java.lang.String, java.util.List)
	 */
	@Override
	public int chooseLeader(String context, List<LeaderCard> tempList) throws RemoteException {
		return _ui.chooseLeader(context, tempList);
	}

	/* (non-Javadoc)
	 * @see misc.ClientRemote#chooseDashboardBonus(java.util.Map)
	 */
	@Override
	public int chooseDashboardBonus(Map<String, List<Resource>> bonus) throws RemoteException {
		return _ui.chooseDashboardBonus(bonus);
	}

	/* (non-Javadoc)
	 * @see client.network.ConnectionServerHandler#dropLeaderCard(java.lang.String)
	 */
	@Override
	public void dropLeaderCard(String leaderName) throws RemoteException {
		if(primo){
			primo = false;
			throw new RemoteException();
		}
		_connectionHandler.dropLeaderCard(leaderName);
	}

	/* (non-Javadoc)
	 * @see client.network.ConnectionServerHandler#activateLeaderCard(java.lang.String)
	 */
	@Override
	public void activateLeaderCard(String leaderName) throws RemoteException {
		_connectionHandler.activateLeaderCard(leaderName);
	}

	/* (non-Javadoc)
	 * @see client.network.ConnectionServerHandler#placeFamiliar(java.lang.String, game.Position)
	 */
	@Override
	public void placeFamiliar(String familiarColour, Position position) throws RemoteException {
		_connectionHandler.placeFamiliar(familiarColour, position);
	}

	/* (non-Javadoc)
	 * @see client.network.ConnectionServerHandler#endTurn()
	 */
	@Override
	public void endTurn() throws RemoteException {
		_connectionHandler.endTurn();
	}

	/* (non-Javadoc)
	 * @see misc.ClientRemote#startTurn(game.GameBoard, game.Player)
	 */
	@Override
	public void startTurn(GameBoard board, Player currentPlayer) throws RemoteException {
		_ui.startTurn(board, currentPlayer);		
	}

	/* (non-Javadoc)
	 * @see misc.ClientRemote#askInt(java.lang.String, int, int)
	 */
	@Override
	public int askInt(String message, int min, int max) throws RemoteException {
		return _ui.askInt(message, min, max);
	}

	/* (non-Javadoc)
	 * @see misc.ClientRemote#sendInfo(java.lang.String)
	 */
	@Override
	public void sendInfo(String infoMessage) throws RemoteException {
		_ui.showInfo(infoMessage);
	}
	
	/* (non-Javadoc)
	 * @see misc.ClientRemote#sendInfo(java.lang.String, game.GameBoard)
	 */
	@Override
	public void sendInfo(String infoMessage, GameBoard board) throws RemoteException {
		_ui.showInfo(infoMessage, board);
	}

	/* (non-Javadoc)
	 * @see misc.ClientRemote#sendInfo(java.lang.String, game.Player)
	 */
	@Override
	public void sendInfo(String message, Player me) throws RemoteException {
		_ui.showInfo(message, me);
	}

	/* (non-Javadoc)
	 * @see misc.ClientRemote#sendInfo(java.lang.String, game.GameBoard, game.Player)
	 */
	@Override
	public void sendInfo(String message, GameBoard board, Player me) throws RemoteException {
		_ui.showInfo(message, board, me);
	}

	/* (non-Javadoc)
	 * @see misc.ClientRemote#sendUUID(java.lang.String)
	 */
	@Override
	public void sendUUID(String uuid) throws RemoteException {
		_ui.setUUID(uuid);
	}

	@Override
	public void showVaticanSupport() throws RemoteException {
		_connectionHandler.showVaticanSupport();
	}

	@Override
	public void activateOPTLeaders() throws RemoteException {
		_connectionHandler.activateOPTLeaders();
	}
}
