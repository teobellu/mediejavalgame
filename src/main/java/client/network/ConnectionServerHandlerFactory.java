package client.network;

import util.Constants;

public class ConnectionServerHandlerFactory {

	public static ConnectionServerHandler getConnectionServerHandler(int connectionType, String host, int port){
		String str = Constants.CONNECTION_TYPES[connectionType];
		if (str == Constants.RMI) {
			return new RMIConnectionServerHandler(host, port);
		} else if(str == Constants.SOCKET){
			return new SocketConnectionServerHandler(host, port);
		}
		
		return null;
	}
}