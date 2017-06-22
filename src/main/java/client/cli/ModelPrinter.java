package client.cli;

import java.util.Arrays;
import java.util.List;

import game.FamilyMember;
import game.GC;
import game.GameBoard;
import game.Resource;
import game.Space;
import game.development.DevelopmentCard;
import util.IOHandler;

public abstract class ModelPrinter {
	
	private final static IOHandler _ioHandler = new IOHandler();
	
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
	
	public static void printSpace(Space space){
		_ioHandler.writeNext("Dice Required: " + space.getRequiredDiceValue() + " ");
		_ioHandler.writeNext("Effect: " + space.getInstantEffect() + " ");
		printListFamiliar(space.getFamiliar());
	}
	
	public static void printListFamiliar(List<FamilyMember> familiars){
		if (familiars.isEmpty())
			_ioHandler.write("No familiars here");
		else
			familiars.forEach(fam -> _ioHandler
				.write("A " + fam.getColor() + " " + fam.getOwner().getName() + "'s familiar is here "));
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
				printSpace(board.getCell(row, column));
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
		
		//dices
		_ioHandler.write("\n*****Dices: ");
		List<Integer> dices = Arrays.asList(board.getDices());
		
		for (int index=0; index < GameBoard.MAX_DICES; index++)
			_ioHandler.write("Dice " + GC.FM_TYPE.get(index) + ": "+ dices.get(index));
		
	}
}
