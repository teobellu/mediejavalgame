package client;

/**
 * Main of the player's client
 * @author Jacopo
 * @author Matteo
 */
public class MainClient {

	/**
	 * Start a new client
	 * @param args Default main arguments
	 */
	public static void main(String[] args) {
		Client client = new Client();
		client.start();
	}
	
	private MainClient() {
	}
}
