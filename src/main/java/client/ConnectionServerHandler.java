package client;

public abstract class ConnectionServerHandler extends Thread {

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
}
