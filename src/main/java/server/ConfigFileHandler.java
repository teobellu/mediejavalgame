package server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import exceptions.CustomConfigException;
import game.ExcommunicationTile;
import game.GC;
import game.Resource;
import game.development.Building;
import game.development.Character;
import game.development.DevelopmentCard;
import game.development.Territory;
import game.development.Venture;
import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.effect.behaviors.EffectConvertResource;
import game.effect.behaviors.EffectDelayFirstAction;
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

/**
 * This class reads and manages the xml file and creates 
 * @author Matteo
 *
 */
public class ConfigFileHandler {
	
	private transient Logger _log = Logger.getLogger(ConfigFileHandler.class.getName());
	
	/**
	 * Map with all information about spaces bonus
	 */
	private static final Map<String, List<Effect>> SPACE_BONUS = new HashMap<>();
	
	/**
	 * Map with all information about bonuses associated with player dashboard
	 */
	private static final Map<String, List<Resource>> BONUS_PLAYER_DASHBOARD = new HashMap<>();
	
	/**
	 * Faith bonus track information
	 */
	private static final List<Integer> BONUS_FAITH = new ArrayList<>();
	
	/**
	 * Deck of development cards
	 */
	private static final List<DevelopmentCard> DEVELOPMENT_DECK = new ArrayList<>();
	
	/**
	 * Deck of excommunication tiles
	 */
	private static final List<ExcommunicationTile> EXCOMMUNICATION_DECK = new ArrayList<>();
	
	/**
	 * Start timeout
	 */
	protected long TIMEOUT_START;
	
	/**
	 * Turn timeout
	 */
	protected long TIMEOUT_TURN;
	
	/**
	 * Map with effect and this function, used for create lambda expression
	 */
	private static final Map<String, Function<Node , Effect>> EFFECTS = new HashMap<>();
	
	public ConfigFileHandler() {
		
	}
	
	/**
	 * Reads a xml file
	 * @param xml Input xml file
	 */
	public void read(File xml){
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xml);
			validate(doc);
		} catch (Exception e) {
			_log.log(Level.INFO, e.getMessage(), e);
			/*TODO le carte caricate prima dell'errore rimangono!
			 * cosi' su 2 piedi mi vengono in mente 2 opzioni, ce ne saranno di migliori:
			 * 1- svuotare tutto, space_bonus ecc. ma perderebbo "final"
			 * 2- chiudere il gioco
			 * 3- fare un metodo clean() //possibile?
			 */
		}
	}
	
	/**
	 * Creates all objects from document
	 * @param doc document, from DocumentBuilder.parse(xml file)
	 * @throws Exception File is not valid
	 */
	public void validate(Document doc) throws CustomConfigException{
		Node root = doc.getDocumentElement();
			
		//an advice from stackoverflow
		root.normalize();
			
		List<Node> listNode = getChildNodesFromNode(root);
		
		//Initialize objects used for reading
		EFFECTS.clear();
		buildMap();
		
		SPACE_BONUS.clear();
		GC.SPACE_TYPE.forEach(type -> SPACE_BONUS.putIfAbsent(type, new ArrayList<>()));
		
		BONUS_PLAYER_DASHBOARD.clear();
		BONUS_PLAYER_DASHBOARD.putIfAbsent(GC.HARVEST, new ArrayList<>());
		BONUS_PLAYER_DASHBOARD.putIfAbsent(GC.PRODUCTION, new ArrayList<>());
		
		
		//TODO debug
		for(Resource resource :  BONUS_PLAYER_DASHBOARD.get(GC.HARVEST)){
			System.out.println("\n\nRisorse in configfilehandler: "+resource.toString());
		}
		
		
		
		//load from file
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
		setIdToCards();
	}
	
	//TODO description
	private void setIdToCards(){
		int countId = 0;
		for (DevelopmentCard card : DEVELOPMENT_DECK){
			countId++;
			card.setId(countId);
		}
		countId = 0;
		for (ExcommunicationTile tile : EXCOMMUNICATION_DECK){
			countId ++;
			tile.setId(countId);
			if (countId == 7)
				countId = 0;
		}
	}
	
	/**
	 * Read an integer from a node
	 * @param node Node to read
	 * @return value read
	 */
	private int getIntegerFromNode(Node node){
		String text = node.getTextContent();
		return Integer.parseInt(text.trim());
	}
	
	/**
	 * Read a string from a node
	 * @param node Node to read
	 * @return value read
	 */
	private String getStringFromNode(Node node){
		String text = node.getTextContent();
		return text.trim();
	}
	
	/**
	 * Read a resource from a node
	 * @param node Node to read
	 * @return value read
	 */
	private Resource getResourceFromNode(Node node){
		List<Node> list = getChildNodesFromNode(node);
		Resource resource = new Resource();
		list.forEach(type -> 
			resource.add(type.getNodeName(), Integer.parseInt(type.getTextContent())));
		return resource;
	}
	
	/**
	 * Loads bonus on all the spaces from the xml file
	 * @param root Root node
	 * @throws Exception The node has not a valid name
	 */
	private void uploadSpaceBonus(Node root) throws CustomConfigException{ 
		List<Node> typeOfSpaces = getChildNodesFromNode(root);
		for (Node node : typeOfSpaces){
			String name = node.getNodeName();
			List<Node> spaces = getChildNodesFromNode(node);
			if(!GC.SPACE_TYPE.contains(node.getNodeName()))
				throw new CustomConfigException();
			spaces.forEach(space -> SPACE_BONUS.get(name)
				.add(getEffectFromContainerNode(space)));
		}
	}
	
	/**
	 * Loads bonus on the player dashboard from the xml file
	 * @param root Root node
	 */
	private void uploadPlayerDashboardBonus(Node root){
		List<Node> options = getChildNodesFromNode(root);
		for (Node option : options){
			List<Node> work = getChildNodesFromNode(option);
			
			for(Node workNode : work){
				Resource bonus = getResourceFromNode(workNode);
				BONUS_PLAYER_DASHBOARD.get(workNode.getNodeName()).add(bonus);
			}
		}
	}
	
	/**
	 * Loads bonus of faith tracing from the xml file
	 * @param root Root node
	 */
	private void uploadFaithTracing(Node node){
		List<Integer> values = new ArrayList<>();
		List<Node> listNode = getChildNodesFromNode(node);
		listNode.forEach(lnode -> values.add(Integer.parseInt(lnode.getTextContent())));
		values.forEach(value -> BONUS_FAITH.add(value));
	}
	
	/**
	 * Loads territory cards from the xml file
	 * @param root Root node
	 */
	private void uploadTerritories(Node root){
		List<Node> territoryCards = getChildNodesFromNode(root);
		for(Node territory : territoryCards){
			List<Node> parameters = getChildNodesFromNode(territory);
			int age = getIntegerFromNode(parameters.get(0));
			String name = getStringFromNode(parameters.get(1));
			Effect immediate = getEffectFromContainerNode(parameters.get(2));
			Effect permanent = getEffectFromContainerNode(parameters.get(3));
			int dice = getIntegerFromNode(parameters.get(4));
			DEVELOPMENT_DECK.add(new Territory(name, age, immediate, permanent, dice));
		}
	}
	
	/**
	 * Loads building cards from the xml file
	 * @param root Root node
	 */
	private void uploadBuildings(Node root){
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
		}
	}
	
	/**
	 * Loads character cards from the xml file
	 * @param root Root node
	 */
	private void uploadCharacters(Node root){
		List<Node> characterCards = getChildNodesFromNode(root);
		for(Node character : characterCards){
			List<Node> parameters = getChildNodesFromNode(character);
			int age = getIntegerFromNode(parameters.get(0));
			String name = getStringFromNode(parameters.get(1));
			Resource cost = getResourceFromNode(parameters.get(2));
			List<Effect> immediate = getEffectListFromContainerNode(parameters.get(3));
			List<Effect> permanent = getEffectListFromContainerNode(parameters.get(4));
			DEVELOPMENT_DECK.add(new Character(age, name, cost, immediate, permanent));
		}
	}
	
	/**
	 * Loads venture cards from the xml file
	 * @param root Root node
	 */
	private void uploadVentures(Node root){/*ventures*/
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
		}
	}
	
	/**
	 * This method is used to read the requirement of venture cards
	 * @param requiresRoot Root node
	 * @return List of requires
	 */
	private List<Resource> lectureVentureRequires(Node requirementRoot){
		List<Node> options = getChildNodesFromNode(requirementRoot);
		List<Resource> requires = new ArrayList<>();
		for (Node option : options){
			List<Node> singleResource = getChildNodesFromNode(option);
			Node firstNode = singleResource.get(0);
			requires.add(new Resource(firstNode.getNodeName(), getIntegerFromNode(firstNode)));
		}
		return requires;
	}
	
	/**
	 * This method is used to read the cost of venture cards
	 * @param costRoot Root node
	 * @return List of costs
	 */
	private List<Resource> lectureVentureCost(Node costRoot){
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
	
	/**
	 * Loads the excommunication tiles from the xml file
	 * @param root Root node
	 */
	private void uploadExcommunicationTiles(Node root){ /*ban_cards*/
		List<Node> banCards = getChildNodesFromNode(root);
		for(Node banCard : banCards){
			List<Node> parameters = getChildNodesFromNode(banCard);
			int age = Integer.parseInt(parameters.get(0).getTextContent());
			Effect effect = factoryEffectBehavior(parameters.get(1));
			EXCOMMUNICATION_DECK.add(new ExcommunicationTile(age, effect));
		}
	}
	
	/**
	 * Upload start timer duration
	 * @param root Root
	 */
	private void uploadTimerStart(Node root){
		TIMEOUT_START = getIntegerFromNode(root);
	}
	
	/**
	 * Upload turn timer duration
	 * @param root Root
	 */
	private void uploadTimerTurn(Node root){
		TIMEOUT_TURN = getIntegerFromNode(root);
	}
	
	/**
	 * Gets a resource package from a list of nodes
	 * @param list List of nodes
	 * @return New resource package
	 */
	private static Resource getResourceFromNodeList(List<Node> list){
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
	private Effect factoryEffectBehavior(Node node){
		return EFFECTS.get(node.getNodeName()).apply(node);
	}
	
	/**
	 * Each element of the map corresponds to the pair string and function from the node
	 * to the effect
	 */
	private void buildMap(){
		EFFECTS.putIfAbsent("get_resources", ConfigFileHandler::getResources);
		EFFECTS.putIfAbsent("gain_for_every", ConfigFileHandler::gainForEvery);
		EFFECTS.putIfAbsent("gain_for_each", ConfigFileHandler::gainForEach);
		EFFECTS.putIfAbsent("get_discount", ConfigFileHandler::getDiscount);
		EFFECTS.putIfAbsent("block_floor",ConfigFileHandler::blockFloor);
		EFFECTS.putIfAbsent("get_card", ConfigFileHandler::getCard); //TODO
		EFFECTS.putIfAbsent("work", ConfigFileHandler::work);
		EFFECTS.putIfAbsent("trade", ConfigFileHandler::trade);
		EFFECTS.putIfAbsent("get_less_resources", ConfigFileHandler::getLessResources);
		EFFECTS.putIfAbsent("less_value_familiars", ConfigFileHandler::lessValueFamiliar);
		EFFECTS.putIfAbsent("increase_value_action", ConfigFileHandler::increaseValueAction);
		EFFECTS.putIfAbsent("no_market", ConfigFileHandler::noMarket);
		EFFECTS.putIfAbsent("pay_more_increase_worker", ConfigFileHandler::payMoreIncreaseWorker);
		EFFECTS.putIfAbsent("delay_first_action", ConfigFileHandler::delayFirstAction);
		EFFECTS.putIfAbsent("no_victory_points", ConfigFileHandler::noVictoryPoints);
		EFFECTS.putIfAbsent("lose_victory_points_depicted_resource", ConfigFileHandler::loseVictoryPointsDepictedResource);
		EFFECTS.putIfAbsent("lose_victory_points", ConfigFileHandler::loseVictoryPoints);
	}
	
	/**
	 * Generate an effect
	 * @Factory Create a new instance of EffectDelayFirstAction
	 * @param node Root node
	 * @return the triggered effect
	 */
	private static Effect delayFirstAction(Node node){
		IEffectBehavior behavior = new EffectDelayFirstAction();
		return new Effect(GC.IMMEDIATE, behavior);
	}
	
	/**
	 * Generate an effect
	 * @Factory Create a new instance of EffectGetResource
	 * @param node Root node
	 * @return the triggered effect
	 */
	private static Effect getResources(Node node){
		List<Node> parameters = getChildNodesFromNode(node);
		Resource resource = getResourceFromNodeList(parameters);
		IEffectBehavior behavior = new EffectGetResource(resource);
		return new Effect(GC.IMMEDIATE, behavior);
	}
	
	/**
	 * Generate an effect
	 * @Factory Create a new instance of EffectRecieveRewardForEach
	 * @param node Root node
	 * @return the triggered effect
	 */
	private static Effect gainForEvery(Node node){
		List<Node> parameters = getChildNodesFromNode(node);
		Node resNode = parameters.get(0);
		Resource resource = new Resource(
				resNode.getNodeName(), Integer.parseInt(resNode.getTextContent()));
		String typeOfCard = parameters.get(1).getTextContent();
		IEffectBehavior behavior = new EffectRecieveRewardForEach(resource, typeOfCard);
		return new Effect(GC.IMMEDIATE, behavior);
	}
	
	/**
	 * Generate an effect
	 * @Factory Create a new instance of EffectRecieveRewardForEach
	 * @param node Root node
	 * @return the triggered effect
	 */
	private static Effect gainForEach(Node node){
		List<Node> parameters = getChildNodesFromNode(node);
		List<Node> gain = getChildNodesFromNode(parameters.get(0));
		List<Node> each = getChildNodesFromNode(parameters.get(1));
		Resource gainResource = getResourceFromNodeList(gain);
		Resource eachResource = getResourceFromNodeList(each);
		IEffectBehavior behavior = new EffectRecieveRewardForEach(gainResource, eachResource);
		return new Effect(GC.IMMEDIATE, behavior);
	}
	
	/**
	 * Generate an effect
	 * @Factory Create a new instance of EffectDiscountResource
	 * @param node Root node
	 * @return the triggered effect
	 */
	private static Effect getDiscount(Node node){
		List<Node> parameters = getChildNodesFromNode(node);
		String action = parameters.get(0).getTextContent();
		parameters.remove(0);
		Resource resource = getResourceFromNodeList(parameters);
		IEffectBehavior behavior = new EffectDiscountResource(action, resource);
		return new Effect(GC.WHEN_FIND_COST_CARD, behavior);
	}
	
	/**
	 * Generate an effect
	 * @Factory Create a new instance of EffectOverruleObject
	 * @param node Root node
	 * @return the triggered effect
	 */
	private static Effect blockFloor(Node node){
		IEffectBehavior behavior = new EffectOverruleObject();
		return new Effect(GC.WHEN_GET_TOWER_BONUS, behavior);
	}
	
	/**
	 * Generate an effect
	 * @Factory Create a new instance of EffectGetACard
	 * @param node Root node
	 * @return the triggered effect
	 */
	private static Effect getCard(Node node){
		List<Node> parameters = getChildNodesFromNode(node);
		IEffectBehavior behavior;
		/*TODO
		<get_card>
		<type>building</type>
		<value>6</value>
		<wood>1</wood>
		<stones>1</stones>
	</get_card>*/
		switch(parameters.size()){
			case 1: behavior = new EffectGetACard(Integer.parseInt(parameters.get(0).getTextContent()));
				break;
			case 2: behavior = new EffectGetACard(parameters.get(0).getTextContent(), Integer.parseInt(parameters.get(1).getTextContent()));
				break;
			default: behavior = new EffectDoNothing();
		}
		if (parameters.size() > 2){
			String type = parameters.get(0).getTextContent();
			int value = Integer.parseInt(parameters.get(1).getTextContent());
			parameters.remove(0);
			parameters.remove(0);
			Resource discount = getResourceFromNodeList(parameters);
			behavior = new EffectGetACard(type, value, discount);
		}
		return new Effect(GC.IMMEDIATE, behavior);
	}
	
	/**
	 * Generate an effect
	 * @Factory Create a new instance of EffectWork
	 * @param node Root node
	 * @return the triggered effect
	 */
	private static Effect work(Node node){
		List<Node> parameters = getChildNodesFromNode(node);
		String action = parameters.get(0).getNodeName();
		int value = Integer.parseInt(parameters.get(0).getTextContent().trim());
		IEffectBehavior behavior = new EffectWork(action, value);
		return new Effect(GC.IMMEDIATE, behavior);
	}
	
	/**
	 * Generate an effect
	 * @Factory Create a new instance of EffectConvertResource
	 * @param node Root node
	 * @return the triggered effect
	 */
	private static Effect trade(Node node){
		List<Node> parameters = getChildNodesFromNode(node);
		List<Resource> payOptions = new ArrayList<>();
		List<Resource> gainOptions = new ArrayList<>();
		for (Node trade : parameters){
			List<Node> option = getChildNodesFromNode(trade);
			List<Node> pay = getChildNodesFromNode(option.get(0));
			List<Node> gain = getChildNodesFromNode(option.get(1));
			payOptions.add(getResourceFromNodeList(pay));
			gainOptions.add(getResourceFromNodeList(gain));
		}
		IEffectBehavior behavior = new EffectConvertResource(payOptions, gainOptions);
		return new Effect(GC.IMMEDIATE, behavior);
	}
	
	/**
	 * Generate an effect
	 * @Factory Create a new instance of EffectDiscountResource
	 * @param node Root node
	 * @return the triggered effect
	 */
	private static Effect getLessResources(Node node){
		List<Node> parameters = getChildNodesFromNode(node);
		Resource resource = getResourceFromNodeList(parameters);
		IEffectBehavior behavior = new EffectDiscountResource(resource);
		return new Effect(GC.WHEN_GAIN, behavior);
	}
	
	/**
	 * Generate an effect
	 * @Factory Create a new instance of EffectIncreaseFamiliarStartPower
	 * @param node Root node
	 * @return the triggered effect
	 */
	private static Effect lessValueFamiliar(Node node){
		int value = Integer.parseInt(node.getTextContent());
		IEffectBehavior behavior = new EffectIncreaseFamiliarStartPower(GC.FM_COLOR, value);
		return new Effect(GC.WHEN_FIND_VALUE_ACTION, behavior);
	}
	
	/**
	 * Generate an effect
	 * @Factory Create a new instance of EffectIncreaseActionPower
	 * @param node Root node
	 * @return the triggered effect
	 */
	private static Effect increaseValueAction(Node node){
		List<Node> parameters = getChildNodesFromNode(node);
		String action = parameters.get(0).getTextContent();
		int value = Integer.parseInt(parameters.get(1).getTextContent());
		IEffectBehavior behavior = new EffectIncreaseActionPower(action, value);
		return new Effect(GC.WHEN_FIND_VALUE_ACTION, behavior);
	}
	
	/**
	 * Generate an effect
	 * @Factory Create a new instance of EffectOverruleObject
	 * @param node Root node
	 * @return the triggered effect
	 */
	private static Effect noMarket(Node node){
		IEffectBehavior behavior = new EffectOverruleObject();
		return new Effect(GC.WHEN_PLACE_FAMILIAR_MARKET, behavior);
	}
	
	/**
	 * Generate an effect
	 * @Factory Create a new instance of EffectPayMoreForIncreaseWorker
	 * @param node Root node
	 * @return the triggered effect
	 */
	private static Effect payMoreIncreaseWorker(Node node){
		int malus = Integer.parseInt(node.getTextContent());
		IEffectBehavior behavior = new EffectPayMoreForIncreaseWorker(malus);
		return new Effect(GC.WHEN_INCREASE_WORKER, behavior);
	}
	
	/**
	 * Generate an effect
	 * @Factory Create a new instance of EffectDontGetVictory
	 * @param node Root node
	 * @return the triggered effect
	 */
	private static Effect noVictoryPoints(Node node){
		List<Node> parameters = getChildNodesFromNode(node);
		String typeOfCard = parameters.get(0).getTextContent();
		IEffectBehavior behavior = new EffectDontGetVictoryFor(typeOfCard);
		return new Effect(GC.WHEN_END, behavior);
	}
	
	/**
	 * Generate an effect
	 * @Factory Create a new instance of EffectLostVictoryDepicted
	 * @param node Root node
	 * @return the triggered effect
	 */
	private static Effect loseVictoryPointsDepictedResource(Node node){
		List<Node> parameters = getChildNodesFromNode(node);
		String typeOfCard = parameters.get(0).getTextContent();
		parameters.remove(0);
		Resource resource = getResourceFromNodeList(parameters);
		IEffectBehavior behavior = new EffectLostVictoryDepicted(typeOfCard, resource);
		return new Effect(GC.WHEN_END, behavior);
	}
	
	/**
	 * Generate an effect
	 * @Factory Create a new instance of EffectLostVictoryForEach
	 * @param node Root node
	 * @return the triggered effect
	 */
	private static Effect loseVictoryPoints(Node node){
		List<Node> parameters = getChildNodesFromNode(node);
		Resource resource = getResourceFromNodeList(parameters);
		IEffectBehavior behavior = new EffectLostVictoryForEach(resource);
		return new Effect(GC.WHEN_END, behavior);
	}
	
	/**
	 * Is equivalent to method org.w3c.dom.Node.getChildNodes() but gets only true nodes
	 * @param father Root node
	 * @return List of child nodes
	 */
	private static List<Node> getChildNodesFromNode(Node father){
		NodeList nodeList = father.getChildNodes(); 
		List<Node> listNode = new ArrayList<>();
		for (int i = 0; i < nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE)
				listNode.add(node);
		}
		return listNode;
	}
	
	/**
	 * Reads a single effect form a container node, like a root node
	 * @param container Node to read
	 * @return Effect Effect read
	 */
	private Effect getEffectFromContainerNode(Node container){
		List<Node> effects = getChildNodesFromNode(container);
		Effect effect = GC.NIX;
		if (!effects.isEmpty())
			effect = factoryEffectBehavior(effects.get(0));
		return effect;
	}
	
	/**
	 * Reads effects form a container node, like a root node
	 * @param container Node to read
	 * @return List of effects
	 */
	private List<Effect> getEffectListFromContainerNode(Node container){
		List<Effect> effectList = new ArrayList<>();
		List<Node> effects = getChildNodesFromNode(container);
		if (!effects.isEmpty())
			effects.forEach(node -> effectList.add(factoryEffectBehavior(node)));
		else
			effectList.add(GC.NIX);
		return effectList;
	}

	public Map<String, List<Effect>> getSpaceBonus() {
		return SPACE_BONUS;
	}

	public Map<String, List<Resource>> getBonusPlayerDashboard() {
		return BONUS_PLAYER_DASHBOARD;
	}

	public List<Integer> getBonusFaith() {
		return BONUS_FAITH;
	}

	public List<DevelopmentCard> getDevelopmentDeck() {
		return DEVELOPMENT_DECK;
	}

	public List<ExcommunicationTile> getExcommunicationDeck() {
		return EXCOMMUNICATION_DECK;
	}
	
}
