package server;

import java.rmi.RemoteException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import misc.ConnectionHandlerRemote;
import util.Constants;

public class RMIConnectionHandler extends ConnectionHandler implements Runnable, ConnectionHandlerRemote {

	@Override
	public void run() {
		
	}
	
	@Override
	public void sendName(String name) throws RemoteException {
		_client.setName(name);
	}

	@Override
	public void ping() throws RemoteException {
		_lastPing = Date.from(Instant.now());	}

	@Override
	public List<String> activateLeaderCard() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendConfigFile() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void putFamiliar() throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	private boolean isTimeoutOver(){
		return Date.from(Instant.now()).getTime() > (_lastPing.getTime() + Constants.TIMEOUT_CONNESSION_MILLIS);
	}
	
	@Override
	public void onConnect() throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean addMeToGame() throws RemoteException {
		return Server.getInstance().addMeToGame(this);
	}
	
	@Override
	public void sendConfigFile(String file) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	private Date _lastPing;
	private Logger _logger = Logger.getLogger(RMIConnectionHandler.class.getName());
	private Thread _timeout;
}
