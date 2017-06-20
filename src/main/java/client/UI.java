package client;

import java.util.List;

public interface UI extends Runnable{
	
	public String askForConfigFile();
	
	public void showInfo(String str);
		
	public void setConnection(String connectionType, String host, int port);

	public List<String> dropLeaderCard();

	public void showInitialLeaderList(List<String> leadersList);
	
	public void showBoard(Gameboard board);
	
	public void showWhatIHave(Player me);
	
	public void notifyPutFamiliar(Familiar familiar);
	
	public void notifyActivateLeaderCard(Player player, LeaderCard card);
}