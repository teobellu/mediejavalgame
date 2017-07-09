package client.network;

import util.Constants;

/**
 * Factory to create a connection handler
 * @author Jacopo
 * 
 */
public class ConnectionServerHandlerFactory {

	/**
	 * Create the appropriate handler
	 * @param str RMI or Socket
	 * @param host server's address
	 * @param port server's port
	 * @return the appropriate handler
	 */
	public static ConnectionServerHandler getConnectionServerHandler(String str, String host, int port){
		if (str == Constants.RMI) {
			return new RMIConnectionServerHandler(host, port);
		} else if(str == Constants.SOCKET){
			return new SocketConnectionServerHandler(host, port);
		}
		
		return null;
	}
}