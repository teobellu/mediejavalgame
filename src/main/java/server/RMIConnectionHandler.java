package server;

import java.rmi.RemoteException;
import java.util.concurrent.LinkedBlockingQueue;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public synchronized Packet getFromClient() throws InterruptedException {
		return _inputQueue.take();
	}

	@Override
	public synchronized Packet readFromServer() throws RemoteException {
		return _outputQueue.take();
	}

	@Override
	public synchronized void sendToServer(Packet command) throws RemoteException {
		try {
			_inputQueue.put(command);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private LinkedBlockingQueue<Packet> _inputQueue = new LinkedBlockingQueue<>();
	private LinkedBlockingQueue<Packet> _outputQueue = new LinkedBlockingQueue<>();
}
