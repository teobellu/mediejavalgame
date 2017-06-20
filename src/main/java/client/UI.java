package client;

import java.util.List;

import game.*;
import game.development.*;

public interface UI extends Runnable{
	
	public String askForConfigFile();
	
	public void showInfo(String str);
		
	public void setConnection(String connectionType, String host, int port);

	public List<String> dropLeaderCard();

	public void showInitialLeaderList(List<String> leadersList);
	
	public void showBoard(GameBoard board);
	
	public void showWhatIHave(Player me);
	
	public void notifyPutFamiliar(FamilyMember familiar);
	
	public void notifyDiscardLeaderCard(String playerName, String card);
	
	/**
	 * Allows the player to select an initial leader card
	 * @param leaders List options
	 * @return Player's selection
	 */
	public int selectInitialLeaders(List<LeaderCard> leaders);
	
	/**
	 * Allows the player to spend his council's privilege
	 * @param options List of options
	 * @return Player's selection
	 */
	public int spendCouncil(List<Resource> options);
	
	//se la carta ha più costi, segli quale costo
	public int chooseCardCost(DevelopmentCard card);

	//scegli se attivare una carta
	public boolean activateLeaderCard(LeaderCard card);
	
	
}