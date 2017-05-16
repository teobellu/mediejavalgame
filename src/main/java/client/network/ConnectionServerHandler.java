package client.network;

import client.Client;
import packets.Packet;

public abstract class ConnectionServerHandler extends Thread {

	public ConnectionServerHandler(String host, int port) {
		_host = host;
		_port = port;
	}
	
	public boolean isRunning(){
		return _isRunning;
	}
	
	public void shutdown(){
		_isRunning = false;
	}
	
	public abstract void sendToServer(Packet packet);
	
	public abstract Packet readFromServer() throws Exception;
	
	public void setClient(Client client){
		_client = client;
	}
	
	protected boolean _isRunning = false;
	protected final String _host;
	protected final int _port;
	protected Client _client;
}
