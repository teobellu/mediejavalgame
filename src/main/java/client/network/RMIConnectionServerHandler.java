package client.network;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.FamilyMember;
import game.GameBoard;
import game.LeaderCard;
import game.Player;
import game.Position;
import game.Resource;
import misc.ClientRemote;
import misc.ConnectionHandlerRemote;
import misc.ServerRemote;
import util.Constants;

public class RMIConnectionServerHandler extends ConnectionServerHandler implements ClientRemote {
	
	private ConnectionHandlerRemote _connectionHandler;
	private final Logger _log = Logger.getLogger(RMIConnectionServerHandler.class.getName());

	public RMIConnectionServerHandler(String host, int port) {
		super(host, port);
		
		if(port==0){
			_port = Registry.REGISTRY_PORT;
		}
		
		try {
			Registry _registry = LocateRegistry.getRegistry(_host, _port);
			
			for(int i = 1; i <= Math.pow(10, 3); i++){
				try{
					UnicastRemoteObject.exportObject((ClientRemote)this, Constants.DEFAULT_SOCKET_PORT + i);//TODO da rivedere
					break;
				} catch(ExportException e){
					if(i == Math.pow(10, 3)){
						e.printStackTrace();
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
	
	@Override
	public void run() {
		
	}

	@Override
	public void ping() throws RemoteException {
		_connectionHandler.ping();		
	}
	
	@Override
	public boolean addMeToGame(String name) throws RemoteException {
		return _connectionHandler.addMeToGame(name);
	}
	
	@Override
	public void sendConfigFile(String file) throws RemoteException {
		_connectionHandler.sendConfigFile(file);
	}
	
	@Override
	public int spendCouncil(List<Resource> councilRewards) throws RemoteException {
		System.out.println("\nChiamato spendCouncil su RMI\n");
		return _ui.spendCouncil(councilRewards);
	}
	
	@Override
	public int chooseFamiliar(List<FamilyMember> familiars, String message) throws RemoteException {
		return _ui.chooseFamiliar(familiars, message);
	}

	@Override
	public boolean ask(String message) throws RemoteException {
		return _ui.askBoolean(message);
	}

	@Override
	public int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions) throws RemoteException {
		return _ui.chooseConvert(realPayOptions, realGainOptions);
	}

	@Override
	public int chooseLeader(String context, List<LeaderCard> tempList) throws RemoteException {
		return _ui.chooseLeader(context, tempList);
	}

	@Override
	public int chooseDashboardBonus(Map<String, List<Resource>> bonus) throws RemoteException {
		return _ui.chooseDashboardBonus(bonus);
	}

	@Override
	public GameBoard getBoard() throws RemoteException {
		return _connectionHandler.getBoard();
	}

	@Override
	public Player getMe() throws RemoteException {
		return _connectionHandler.getMe();
	}

	@Override
	public void dropLeaderCard(String leaderName) throws RemoteException {
		_connectionHandler.dropLeaderCard(leaderName);
	}

	@Override
	public void activateLeaderCard(String leaderName) throws RemoteException {
		_connectionHandler.activateLeaderCard(leaderName);
	}

	@Override
	public void placeFamiliar(String familiarColour, Position position) throws RemoteException {
		_connectionHandler.placeFamiliar(familiarColour, position);
	}

	@Override
	public void endTurn() throws RemoteException {
		_connectionHandler.endTurn();
	}

	@Override
	public void startTurn(GameBoard board, Player currentPlayer) throws RemoteException {
		_ui.startTurn(board, currentPlayer);		
	}

	@Override
	public int askInt(String message, int min, int max) throws RemoteException {
		return _ui.askInt(message, min, max);
	}

	@Override
	public void sendInfo(String infoMessage) throws RemoteException {
		_ui.showInfo(infoMessage);
	}
}
