package client.userinterface;

import client.network.ConnectionServerHandler;

public interface UI {
	
	public ConnectionServerHandler getConnection();
	
	public String getStringValue(String request);
}
