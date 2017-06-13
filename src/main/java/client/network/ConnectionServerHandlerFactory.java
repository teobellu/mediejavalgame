package client.network;

import util.Constants;

public class ConnectionServerHandlerFactory {

	public static ConnectionServerHandler getConnectionServerHandler(String str, String host, int port){
		if (str == Constants.RMI) {
			return new RMIConnectionServerHandler(host, port);
		} else if(str == Constants.SOCKET){
			return new SocketConnectionServerHandler(host, port);//TODO fix port
		}
		
		return null;
	}
}