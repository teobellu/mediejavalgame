package controller;

import server.Client;
import server.ConnectionHandler;
import server.RMIConnectionHandler;

/**
 * A fake client used to tests other classes
 *
 */
public class FakeClient extends Client{
	
	/**
	 * A random connection handler, we use RMI for tests
	 */
	private static final ConnectionHandler CONNECTION_HANDLER = new RMIConnectionHandler();
	
	/**
	 * Base constructor of a fake client @Override of client base constructor
	 * @param handler A random connection handler, we use RMI for tests
	 * @param uuid Unused param
	 * @param name Unused param
	 */
	public FakeClient(ConnectionHandler handler, String uuid, String name) {
		super(CONNECTION_HANDLER, "128 BIT UUID", "Nickname");
	}

}
