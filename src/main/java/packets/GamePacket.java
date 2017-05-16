package packets;

import java.util.List;

public class GamePacket extends Packet {

	public GamePacket(String command) {
		super(command);
	}
	
	public GamePacket(String command, Object...objects) {
		super(command);
		for(Object obj : objects){
			_arguments.add(obj);
		}
	}
	
	private List<Object> _arguments;
}
