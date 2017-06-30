package client.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.FamilyMember;
import game.GC;
import game.GameBoard;
import game.LeaderCard;
import game.Player;
import game.Resource;
import game.Space;
import game.development.DevelopmentCard;
import util.IOHandler;

/**
 * This class is used to print information about model serialized objects
 *
 */
public abstract class ModelPrinter {
	
	/**
	 * I/O handler used for write operations and read operations
	 */
	private static final IOHandler _ioHandler = new IOHandler();
	
	/**
	 * Hide constructor
	 */
	private ModelPrinter(){
		//Created to please sonarLint
	}
	
	/**
	 * Print a resource, like a print toString()
	 * @param resource Resource to print
	 */
	public static void printResource(Resource resource){
		if (resource == null)
			return;
		GC.RES_TYPES.stream()
			.filter(type -> resource.get(type) > 0)
			.forEach(type -> _ioHandler.writeNext(resource.get(type) + " " + type + " "));
	}
	
	/**
	 * Print all information about a space
	 * @param space Space to print
	 */
	public static void printSpace(Space space){
		_ioHandler.writeNext("Dice Required: " + space.getRequiredDiceValue() + " ");
		_ioHandler.writeNext("Effect: " + space.getInstantEffect() + " ");
		List<FamilyMember> familiars = space.getFamiliars();
		if (familiars.isEmpty())
			_ioHandler.write("No familiars here");
		else
			familiars.forEach(fam -> _ioHandler
				.write(fam.getOwner().getName() + " " + fam.getColor() + "familiar "));
	}
	
	
	
	/**
	 * Print all information about a card
	 * @param card Card to print
	 */
	public static void printCard(DevelopmentCard card) {
		if (card == null)
			return;
		_ioHandler.writeNext("Card Name: " + card.getName());
		_ioHandler.writeNext(", Type: " + card.toString());
		_ioHandler.writeNext(", Age: " + card.getAge());
		if (card.getDice() > 0)
			_ioHandler.writeNext(", Dice: " + card.getDice());
		if (!card.getCost().isEmpty()){
			_ioHandler.writeNext(", Costs:");
			for(int i = 0; i < card.getCost().size(); i ++){
				_ioHandler.writeNext(" * ");
				printResource(card.getCost(i));
			}
		}
		if (!card.getRequirement().isEmpty()){
			_ioHandler.writeNext(", Requirements:");
			for(int i = 0; i < card.getRequirement().size(); i ++){
				_ioHandler.writeNext(" * ");
				printResource(card.getRequirement(i));
			}
		}
		if (!card.getImmediateEffect().isEmpty()){
			_ioHandler.writeNext(", Immediate effects: ");
			card.getImmediateEffect()
				.forEach(effect -> _ioHandler.writeNext("* " + effect.toString()));
		}
		if (!card.getPermanentEffect().isEmpty()){
			_ioHandler.writeNext(", Permanent effects: ");
			card.getPermanentEffect()
				.forEach(effect -> _ioHandler.writeNext("* " + effect.toString()));
		}
		_ioHandler.write("");
	}
	
	/**
	 * Print all information about the game board
	 * @param board Game board to print
	 */
	public static void printBoard(GameBoard board){
		_ioHandler.write("(^_^) Here is the game board:");
		
		_ioHandler.write("*****Tower");
		for (int column = 0; column < GameBoard.MAX_COLUMN; column++){
			_ioHandler.write("\nColumn " + column + ":");
			for (int row = GameBoard.MAX_ROW - 1; row >= 0; row--){
				_ioHandler.writeNext("Row " + row + ": ");
				DevelopmentCard card = board.getCard(row, column);
				if (card == null)
					_ioHandler.writeNext("No card here! ");
				else{
					_ioHandler.writeNext("Card name: " + card.getName() + " ");
				}
				printSpace(board.getFromTowers(row, column));
			}
		}
		
		//council palace
		_ioHandler.write("\n*****Council Palace: ");
		printSpace(board.getCouncilPalaceSpace());
		
		//harvest
		_ioHandler.write("\n*****Harvest Space: ");
		_ioHandler.writeNext("Normal Space: ");
		printSpace(board.getWorkSpace(GC.HARVEST));
		_ioHandler.writeNext("Long Space: ");
		printSpace(board.getWorkLongSpace(GC.HARVEST));
		
		//production
		_ioHandler.write("\n*****Producion Space: ");
		_ioHandler.writeNext("Normal Space: ");
		printSpace(board.getWorkSpace(GC.PRODUCTION));
		_ioHandler.writeNext("Long Space: ");
		printSpace(board.getWorkLongSpace(GC.PRODUCTION));
		
		//market
		_ioHandler.write("\n*****Market: ");
		for (int index=0; index < GameBoard.MAX_MARKET_SPACE; index ++){
			_ioHandler.writeNext("Space " + index + ": ");
			printSpace(board.getMarketSpace(index));
		}
		
		//excommunication tiles
		
		_ioHandler.write("\n*****Excommunication tiles: ");
		for (int index=0; index < GameBoard.MAX_EXCOMUNNICATION_CARD; index++){
			String effectDescription = board.getExCard()[index].getEffect().toString();
			_ioHandler.write("Malus " + index + ": " + effectDescription);
		}
		
		_ioHandler.write("\n*****Dices: ");
		List<Integer> dices = Arrays.asList(board.getDices());
		
		for (int index=0; index < GameBoard.MAX_DICES; index++)
			_ioHandler.write("Dice " + GC.FM_TYPE.get(index) + ": "+ dices.get(index));
		
	}
	
	/**
	 * Print all information about player's loot, like cards and resources
	 * @param me Player
	 */
	public static void printMyLoot(Player me){
		//resources
		_ioHandler.write("\n*****Resources: ");
		printResource(me.getResource());
		
		//dev cards
		_ioHandler.write("\n*****Development cards: ");
		for(String type : GC.DEV_TYPES)
			if (!me.getDevelopmentCards(type).isEmpty()){
				_ioHandler.write("\n" + type);
				for(DevelopmentCard card : me.getDevelopmentCards(type)){
					_ioHandler.writeNext(card.getName() + " ");
				}
			}
		
		//leader cards
		_ioHandler.write("\n*****Leader cards: ");
		me.getLeaderCards()	
			.forEach(leader -> _ioHandler.writeNext(leader.getName() + " "));
		
		//leader cards that you can activate
		/*TODO non è serializzabile!!!
		_ioHandler.write("\n*****Leader cards that you can activate: ");
		me.getActivableLeaderCards()
			.forEach(leader -> _ioHandler.writeNext(leader.getName() + " "));
		*/
		//free familiars
		_ioHandler.write("\n*****Familiars: ");
		me.getFreeMember()
			.forEach(fam -> _ioHandler.writeNext(fam.getColor() + " with power " + fam.getValue()));
		
		//permanent effects
		_ioHandler.write("\n*****Effects: ");
		me.getEffects()
			.forEach(eff -> _ioHandler.write(eff.getIEffectBehavior().toString()));
		
	}
	
	/**
	 * Print all information about a list of familiars
	 * @param familiars List to print
	 */
	public static void printListFamiliar(List<FamilyMember> familiars){
		int index = 0;
		for(FamilyMember familiar : familiars){
			_ioHandler.writeNext(index + ") ");
			_ioHandler.write(familiar.getColor() + " familiar");
			index++;
		}
	}
	
	/**
	 * Print all information about a list of leader cards
	 * @param leaders
	 */
	public static void printLeaderCards(List<LeaderCard> leaders){
		int index = 0;
		for(LeaderCard leader : leaders){
			_ioHandler.writeNext(index + ") ");
			_ioHandler.write(leader.getName() + " : " + leader.getEffect());
			index++;
		}
	}
}
