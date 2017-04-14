package client;

import util.Constants;

public class ConnectionServerHandlerFactory {

	public static ConnectionServerHandler getConnectionServerHandler(int connectionType){
		String str = Constants.CONNECTION_TYPES[connectionType];
		if (str == Constants.RMI) {
			return new RMIConnectionServerHandler();
		} else if(str == Constants.SOCKET){
			return new SocketConnectionServerHandler();
		}
		
		return null;
	}
}