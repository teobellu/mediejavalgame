package util;

import java.io.Serializable;

public abstract class Packet implements Serializable {

	public Packet(String command) {
		_command = command;
	}
	
	public String getCommand(){
		return _command;
	}
	
	private final String _command;
}
