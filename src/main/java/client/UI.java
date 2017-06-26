package client;

import java.util.List;
import java.util.Map;

import game.*;
import game.development.*;

public interface UI extends Runnable{
	
	public String askForConfigFile();
	
	public void showInfo(String str);
		
	public void setConnection(String connectionType, String host, int port);

	public List<String> dropLeaderCard();

	public int showInitialLeaderList(List<String> leadersList) throws Exception;
	
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

	/**
	 * Show a message and allows the player to choose a familiar from a list
	 * @param familiars List of family members
	 * @param message Message to show
	 * @return Selection, index of the list
	 */
	public int chooseFamiliar(List<FamilyMember> familiars, String message);

	/**
	 * Allows the player to answer true or false to a message
	 * @param message Question
	 * @return Answer of the player: yes or no (true or false)
	 */
	public boolean answerToAQuestion(String message);

	/**
	 * Allows the player to choose what exchange he prefer, for example for building cards
	 * @param realPayOptions Options to pay
	 * @param realGainOptions Options to gain
	 * @return selection, index of the lists
	 */
	public int chooseConvert(List<Resource> realPayOptions, List<Resource> realGainOptions);

	public int chooseLeader(List<LeaderCard> tempList);

	public int chooseDashboardBonus(Map<String, List<Resource>> bonus);

	public void startTurn(GameBoard board, Player me);
	
	public boolean endTurn();
	
}