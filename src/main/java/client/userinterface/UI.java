package client.userinterface;

import client.network.ConnectionServerHandler;

public interface UI {
	
	public ConnectionServerHandler getConnection();
	
	public String getStringValue(boolean isEmptyAllowed);
	
	public void printString(String string);
	
	public String askForConfigFile();
	
	public void write(String str);
}
