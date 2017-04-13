package util;

public class Packet {

	public Packet(String command, String commandType) {
		_command = command;
		_commandType = commandType;
	}
	
	public String getCommand(){
		return _command;
	}
	
	public String getCommandType(){
		return _commandType;
	}
	
	private final String _command;
	private final String _commandType;
}
