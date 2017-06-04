package client.cli;

public interface UI {
		
	public String getStringValue(boolean isEmptyAllowed);
	
	public void printString(String string);
	
	public String askForConfigFile();
	
	public void write(String str);
		
	public void start();

	public void setConnection(String connectionType, String host, int port);
}