package client.network;

public abstract class ConnectionServerHandler extends Thread {

	public ConnectionServerHandler(String host, int port) {
		_host = host;
		_port = port;
	}
	
	public boolean isRunning(){
		return _IS_RUNNING;
	}
	
	public void shutdown(){
		_IS_RUNNING = false;
	}
	
	public String prova(){
		return "";
	}
	
	protected boolean _IS_RUNNING = false;
	protected final String _host;
	protected final int _port;
}
