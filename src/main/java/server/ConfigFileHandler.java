package server;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import game.ExcommunicationTile;
import game.GC;
import game.ICard;
import game.Resource;
import game.development.Building;
import game.development.Character;
import game.development.Territory;
import game.development.Venture;
import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.effect.behaviors.EffectConvertResource;
import game.effect.behaviors.EffectDiscountResource;
import game.effect.behaviors.EffectDoNothing;
import game.effect.behaviors.EffectDontGetVictoryFor;
import game.effect.behaviors.EffectGetACard;
import game.effect.behaviors.EffectGetResource;
import game.effect.behaviors.EffectIncreaseActionPower;
import game.effect.behaviors.EffectIncreaseFamiliarStartPower;
import game.effect.behaviors.EffectLostVictoryDepicted;
import game.effect.behaviors.EffectLostVictoryForEach;
import game.effect.behaviors.EffectOverruleObject;
import game.effect.behaviors.EffectPayMoreForIncreaseWorker;
import game.effect.behaviors.EffectRecieveRewardForEach;
import game.effect.behaviors.EffectWork;

public class ConfigFileHandler {
	
	public static final Map<String, List<Effect>> SPACE_BONUS = new HashMap<>();
	public static final Map<String, List<Resource>> BONUS_PLAYER_DASHBOARD = new HashMap<>();
	public static final List<Integer> BONUS_FAITH = new ArrayList<>();
	public static final List<ICard> DEVELOPMENT_DECK = new ArrayList<>();
	public static final List<ICard> EXCOMMUNICATION_DECK = new ArrayList<>();
	public static int TIMEOUT_START;
	public static int TIMEOUT_TURN;

	public static void main(String[] args) {
		try{
			validate();
		} catch (Exception e) {
			System.err.println("file is not good"); //TODO e se scelgo GUI?
			/**TODO le carte caricate prima dell'errore rimangono!
			 * così su 2 piedi mi vengono in mente 2 opzioni, ce ne saranno di migliori:
			 * 1- svuotare tutto, space_bonus ecc. ma perderebbo "final"
			 * 2- chiudere il gioco
			 */
		}
	}
	
	public static void validate() throws Exception{
		File fileXML = new File("default_settings.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();			
		Document doc = db.parse(fileXML);
		
		Node root = doc.getDocumentElement();
			
		root.normalize();
			
		List<Node> listNode = getChildNodesFromNode(root);
			
		GC.SPACE_TYPE.forEach(type -> SPACE_BONUS.putIfAbsent(type, new ArrayList<>()));
		BONUS_PLAYER_DASHBOARD.putIfAbsent(GC.HARVEST, new ArrayList<>());
		BONUS_PLAYER_DASHBOARD.putIfAbsent(GC.PRODUCTION, new ArrayList<>());
			
		uploadSpaceBonus(listNode.get(0));
		uploadPlayerDashboardBonus(listNode.get(1));
		uploadFaithTracing(listNode.get(2));
		uploadTerritories(getChildNodesFromNode(listNode.get(3)).get(0));
		uploadBuildings(getChildNodesFromNode(listNode.get(3)).get(1));
		uploadCharacters(getChildNodesFromNode(listNode.get(3)).get(2));
		uploadVentures(getChildNodesFromNode(listNode.get(3)).get(3));
		uploadExcommunicationTiles(listNode.get(4));
		uploadTimerStart(listNode.get(5));
		uploadTimerTurn(listNode.get(6));
	}
	
	
	public void read(File xml){
		// metodo jacopo
	}
	
	public static void uploadSpaceBonus(Node root){ 
		List<Node> typeOfSpaces = getChildNodesFromNode(root);
		for (Node node : typeOfSpaces){
			String name = node.getNodeName();
			List<Node> spaces = getChildNodesFromNode(node);
			if(!GC.SPACE_TYPE.contains(node.getNodeName()))
				;//lancia eccezione
			spaces.forEach(space -> SPACE_BONUS.get(name)
				.add(getEffectFromContainerNode(space)));
		}
	}
	
	public static Effect getEffectFromContainerNode(Node container){
		List<Node> effects = getChildNodesFromNode(container);
		Effect effect = GC.NIX;
		if (!effects.isEmpty())
			effect = factoryEffectBehavior(effects.get(0));
		return effect;
	}
	
	public static List<Effect> getEffectListFromContainerNode(Node container){
		List<Effect> effectList = new ArrayList<>();
		List<Node> effects = getChildNodesFromNode(container);
		if (!effects.isEmpty())
			for (Node node : effects)
				effectList.add(factoryEffectBehavior(node));
		else
			effectList.add(GC.NIX);
		return effectList;
	}
	
	public static int getIntegerFromNode(Node node){
		String text = node.getTextContent();
		return Integer.parseInt(text.trim());
	}
	
	public static String getStringFromNode(Node node){
		String text = node.getTextContent();
		return text.trim();
	}
	
	public static Resource getResourceFromNode(Node node){
		List<Node> list = getChildNodesFromNode(node);
		Resource resource = new Resource();
		list.forEach(type -> 
			resource.add(type.getNodeName(), Integer.parseInt(type.getTextContent())));
		return resource;
	}
	
	public static void uploadPlayerDashboardBonus(Node root){
		List<Node> options = getChildNodesFromNode(root);
		for (Node option : options){
			List<Node> work = getChildNodesFromNode(option);
			
			for(Node workNode : work){
				Resource bonus = getResourceFromNode(workNode);
				BONUS_PLAYER_DASHBOARD.get(workNode.getNodeName()).add(bonus);
			}
		}
	}
	
	public static void uploadFaithTracing(Node node){
		List<Integer> values = new ArrayList<>();
		List<Node> listNode = getChildNodesFromNode(node);
		listNode.forEach(lnode -> values.add(Integer.parseInt(lnode.getTextContent())));
		values.forEach(value -> BONUS_FAITH.add(value));
	}
	
	public static void uploadTerritories(Node root){/*territories*/
		List<Node> territoryCards = getChildNodesFromNode(root);
		for(Node territory : territoryCards){
			List<Node> parameters = getChildNodesFromNode(territory);
			int age = getIntegerFromNode(parameters.get(0));
			String name = getStringFromNode(parameters.get(1));
			Effect immediate = getEffectFromContainerNode(parameters.get(2));
			Effect permanent = getEffectFromContainerNode(parameters.get(3));
			int dice = getIntegerFromNode(parameters.get(4));
			DEVELOPMENT_DECK.add(new Territory(name, age, immediate, permanent, dice));
			System.out.println("add territory card, name = " + name + ", age =" + age + " , immediate = " + immediate.getWhenActivate() + ", permanent = " + permanent.getWhenActivate() + ", dice = " + dice);
		}
	}
	
	public static void uploadBuildings(Node root){/*buildings*/
		List<Node> buildingCards = getChildNodesFromNode(root);
		for(Node building : buildingCards){
			List<Node> parameters = getChildNodesFromNode(building);
			int age = getIntegerFromNode(parameters.get(0));
			String name = getStringFromNode(parameters.get(1));
			Resource cost = getResourceFromNode(parameters.get(2));
			Effect immediate = getEffectFromContainerNode(parameters.get(3));
			Effect permanent = getEffectFromContainerNode(parameters.get(4));
			int dice = getIntegerFromNode(parameters.get(5));
			DEVELOPMENT_DECK.add(new Building(age, name, cost, immediate, permanent, dice));
			System.out.println("add building card, name = " + name + ", age =" + age + " , immediate = " + immediate.getWhenActivate() + ", permanent = " + permanent.getWhenActivate() + ", dice = " + dice);
		}
	}
	
	public static void uploadCharacters(Node root){/*characters*/
		List<Node> characterCards = getChildNodesFromNode(root);
		for(Node character : characterCards){
			List<Node> parameters = getChildNodesFromNode(character);
			int age = getIntegerFromNode(parameters.get(0));
			String name = getStringFromNode(parameters.get(1));
			Resource cost = getResourceFromNode(parameters.get(2));
			List<Effect> immediate = getEffectListFromContainerNode(parameters.get(3));
			List<Effect> permanent = getEffectListFromContainerNode(parameters.get(4));
			DEVELOPMENT_DECK.add(new Character(age, name, cost, immediate, permanent));
			System.out.println("add character card, name = " + name + ", age = " + age + ", immediate = " + immediate.size() + ", permanent = " + permanent.size());
		}
	}
	
	public static void uploadVentures(Node root){/*ventures*/
		List<Node> ventureCards = getChildNodesFromNode(root);
		for(Node venture : ventureCards){
			List<Node> parameters = getChildNodesFromNode(venture);
			int age = getIntegerFromNode(parameters.get(0));
			String name = getStringFromNode(parameters.get(1));
			List<Resource> requires = lectureVentureRequires(parameters.get(2));
			List<Resource> cost = lectureVentureCost(parameters.get(2));
			List<Effect> immediate = getEffectListFromContainerNode(parameters.get(3));
			int reward = getIntegerFromNode(parameters.get(4));
			DEVELOPMENT_DECK.add(new Venture(age, name, requires, cost, immediate, reward));
			System.out.println("add venture card, name = " + name + ", age = " + age + ", immediate = " + immediate.size() + ", cost = " + cost.size());
		}
	}
	
	private static List<Resource> lectureVentureRequires(Node costRoot){
		List<Node> options = getChildNodesFromNode(costRoot);
		List<Resource> requires = new ArrayList<>();
		for (Node option : options){
			List<Node> singleResource = getChildNodesFromNode(option);
			Node firstNode = singleResource.get(0);
			requires.add(new Resource(firstNode.getNodeName(), getIntegerFromNode(firstNode)));
		}
		return requires;
	}
	
	private static List<Resource> lectureVentureCost(Node costRoot){
		List<Node> options = getChildNodesFromNode(costRoot);
		List<Resource> cost = new ArrayList<>();
		for (Node option : options){
			List<Node> singleResource = getChildNodesFromNode(option);
			singleResource.remove(0);
			Resource singleCost = new Resource();
			singleResource.forEach(node -> 
				singleCost.add(node.getNodeName(), getIntegerFromNode(node)));
			cost.add(singleCost);
		}
		return cost;
	}
	
	public static void uploadExcommunicationTiles(Node root){ /*ban_cards*/
		List<Node> banCards = getChildNodesFromNode(root);
		for(Node banCard : banCards){
			List<Node> parameters = getChildNodesFromNode(banCard);
			int age = Integer.parseInt(parameters.get(0).getTextContent());
			Effect effect = factoryEffectBehavior(parameters.get(1));
			EXCOMMUNICATION_DECK.add(new ExcommunicationTile(age, effect));
			System.out.println("add ban card, age = " + age + ", effect = " + effect.getWhenActivate());
		}
	}
	
	public static void uploadTimerStart(Node root){
		TIMEOUT_START = getIntegerFromNode(root);
	}
	
	public static void uploadTimerTurn(Node root){
		TIMEOUT_TURN = getIntegerFromNode(root);
	}
	
	public static Resource getResourceFromNodeList(List<Node> list){
		Resource resource = new Resource();
		list.forEach(type -> 
			resource.add(type.getNodeName(), Integer.parseInt(type.getTextContent())));
		return resource;
	}
	
	/**
	 * This method reads a node, which represents an effect, and converts it into a effect
	 * @param node The node which represents an effect
	 * @return Effect with his behavior
	 */
	public static Effect factoryEffectBehavior(Node node){
		IEffectBehavior behavior;
		List<Node> parameters = getChildNodesFromNode(node);
		switch (node.getNodeName()){
		
			case "get_resources": {
				Resource resource = getResourceFromNodeList(parameters);
				behavior = new EffectGetResource(resource);
				return new Effect(GC.IMMEDIATE, behavior);
			}
			case "gain_for_every":{
				Node resNode = parameters.get(0);
				Resource resource = new Resource(
						resNode.getNodeName(), Integer.parseInt(resNode.getTextContent()));
				String typeOfCard = parameters.get(1).getTextContent();
				behavior = new EffectRecieveRewardForEach(resource, typeOfCard);
				return new Effect(GC.IMMEDIATE, behavior);
			}
			case "gain_for_each":{
				List<Node> gain = getChildNodesFromNode(parameters.get(0));
				List<Node> each = getChildNodesFromNode(parameters.get(1));
				Resource gainResource = getResourceFromNodeList(gain);
				Resource eachResource = getResourceFromNodeList(each);
				//TODO
				return GC.NIX;
			}
			case "get_discount":{
				String action = parameters.get(0).getTextContent();
				parameters.remove(0);
				Resource resource = getResourceFromNodeList(parameters);
				behavior = new EffectDiscountResource(action, resource);
				return new Effect(GC.WHEN_FIND_COST_CARD, behavior);
			}
			case "block_floor":{
				behavior = new EffectOverruleObject();
				return new Effect(GC.WHEN_GET_TOWER_BONUS, behavior);
			}
			case "get_card":{
				switch(parameters.size()){
					case 1: behavior = new EffectGetACard(Integer.parseInt(parameters.get(0).getTextContent()));
						break;
					case 2: behavior = new EffectGetACard(parameters.get(0).getTextContent(), Integer.parseInt(parameters.get(1).getTextContent()));
						break;
					default : ;//TODO generate exception
				}
				return new Effect(GC.IMMEDIATE, new EffectDoNothing());
			}
			case "work":{
				String action = parameters.get(0).getNodeName();
				int value = Integer.parseInt(parameters.get(0).getTextContent().trim());
				behavior = new EffectWork(action, value);
				return new Effect(GC.IMMEDIATE, behavior);
			}
			case "trade":{
				List<Resource> payOptions = new ArrayList<>();
				List<Resource> gainOptions = new ArrayList<>();
				for (Node trade : parameters){
					List<Node> option = getChildNodesFromNode(trade);
					List<Node> pay = getChildNodesFromNode(option.get(0));
					List<Node> gain = getChildNodesFromNode(option.get(1));
					payOptions.add(getResourceFromNodeList(pay));
					gainOptions.add(getResourceFromNodeList(gain));
				}
				behavior = new EffectConvertResource(payOptions, gainOptions);
				return new Effect(GC.IMMEDIATE, behavior);
			}
			
			/*Excommunication tiles*/
			
			/**
			 * First Age
			 */
			case "get_less_resources": {
				Resource resource = getResourceFromNodeList(parameters);
				behavior = new EffectDiscountResource(resource);
				return new Effect(GC.WHEN_GAIN, behavior);
			}
			case "less_value_familiars": {
				int value = Integer.parseInt(node.getTextContent());
				behavior = new EffectIncreaseFamiliarStartPower(GC.FM_COLOR, value);
				return new Effect(GC.WHEN_FIND_VALUE_ACTION, behavior);
			}
			
			/**
			 * Second Age
			 */
			case "increase_value_action": {
				String action = parameters.get(0).getTextContent();
				int value = Integer.parseInt(parameters.get(1).getTextContent());
				behavior = new EffectIncreaseActionPower(action, value);
				return new Effect(GC.WHEN_FIND_VALUE_ACTION, behavior);
			}
			case "no_market": {
				behavior = new EffectOverruleObject();
				return new Effect(GC.WHEN_PLACE_FAMILIAR_MARKET, behavior);
			}
			case "pay_more_increase_worker": {
				int malus = Integer.parseInt(node.getTextContent());
				behavior = new EffectPayMoreForIncreaseWorker(malus);
				return new Effect(GC.WHEN_INCREASE_WORKER, behavior);
			}
			case "delay_first_action": {
				//TODO
				return new Effect("TODO", null);
			}
			
			/**
			 * Third Age
			 */
			case "no_victory_points": {
				String typeOfCard = parameters.get(0).getTextContent();
				behavior = new EffectDontGetVictoryFor(typeOfCard);
				return new Effect(GC.WHEN_END, behavior);
			}
			case "lose_victory_points_depicted_resource": {
				String typeOfCard = parameters.get(0).getTextContent();
				parameters.remove(0);
				Resource resource = getResourceFromNodeList(parameters);
				behavior = new EffectLostVictoryDepicted(typeOfCard, resource);
				return new Effect(GC.WHEN_END, behavior);
			}
			case "lose_victory_points": {
				Resource resource = getResourceFromNodeList(parameters);
				behavior = new EffectLostVictoryForEach(resource);
				return new Effect(GC.WHEN_END, behavior);
			}
			default : {
				System.err.println("Can't load this effect");
				return GC.NIX;
			}
		}
		
	}
	
	public static List<Node> getChildNodesFromNode(Node father){
		NodeList nodeList = father.getChildNodes(); 
		List<Node> listNode = new ArrayList<>();
		for (int i = 0; i < nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE)
				listNode.add(node);
		}
		return listNode;
	}
	
}
