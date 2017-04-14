package client;

import util.Constants;
import util.IOHandler;

public class Client extends Thread {

	public Client() {
		_ioHandler = new IOHandler();
	}
	
	@Override
	public void run(){
		_ioHandler.write("Come vorresti connetterti?");
		int i = 1;
		for(String ct : Constants.CONNECTION_TYPES){
			_ioHandler.write(i+") "+ct);
			i++;
		}
		_ioHandler.readNumber();
	}
	
	private final IOHandler _ioHandler;
	
}
