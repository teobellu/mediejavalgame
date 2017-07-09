package server;

/**
 * Main of the Server
 * @author Jacopo
 * @author Matteo
 */
public class MainServer {

	/**
	 * Start the server
	 * @param args Default main arguments
	 */
	public static void main(String[] args) {
		Server server = Server.getInstance();
		server.start();
	}
}