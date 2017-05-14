package client.network;

import client.Client;
import util.Packet;

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
	
	public abstract void write(Packet packet);
	
	public abstract Packet read() throws Exception;
	
	public void setClient(Client client){
		_client = client;
	}
	
	protected boolean _isRunning = false;
	protected final String _host;
	protected final int _port;
	protected Client _client;
}
