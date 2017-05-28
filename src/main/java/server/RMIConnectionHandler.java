package server;

import java.rmi.RemoteException;
import java.time.Instant;
import java.util.Date;
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
	public void activateLeaderCard() throws RemoteException {
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
	
	private Date _lastPing;
	private Logger _logger = Logger.getLogger(RMIConnectionHandler.class.getName());
	private Thread _timeout;
}
