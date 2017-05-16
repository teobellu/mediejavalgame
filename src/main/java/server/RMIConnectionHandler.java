package server;

import java.rmi.RemoteException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import misc.ConnectionHandlerRemote;
import util.Packet;

public class RMIConnectionHandler extends ConnectionHandler implements Runnable, ConnectionHandlerRemote {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void sendToClient(Packet packet) {
		try {
			_outputQueue.put(packet);
		} catch (InterruptedException e) {
			_logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
	}

	@Override
	public synchronized Packet getFromClient() throws InterruptedException {
		return _inputQueue.take();
	}

	@Override
	public synchronized Packet readFromServer() throws RemoteException {
		Packet message = null;
		try {
			message = _outputQueue.take();
		} catch (InterruptedException e) {
			_logger.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			return message;
		}
	}

	@Override
	public synchronized boolean sendToServer(Packet command) throws RemoteException {
		try {
			_inputQueue.put(command);
			return true;
		} catch (InterruptedException e) {
			_logger.log(Level.SEVERE, e.getMessage(), e);
			return false;
		}
		
	}
	
	private LinkedBlockingQueue<Packet> _inputQueue = new LinkedBlockingQueue<>();
	private LinkedBlockingQueue<Packet> _outputQueue = new LinkedBlockingQueue<>();
	private Logger _logger = Logger.getLogger(RMIConnectionHandler.class.getName());
}
