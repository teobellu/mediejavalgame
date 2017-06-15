package server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.media.jfxmedia.events.MarkerEvent;
import com.sun.media.jfxmedia.events.PlayerEvent;

import game.ExcommunicationTile;
import game.GC;
import game.ICard;
import game.Player;
import game.Resource;
import game.development.Territory;
import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.effect.behaviors.EffectConvertResource;
import game.effect.behaviors.EffectDiscountResource;
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

public class Test {
	
	
	/**
	 * @0000 X ORA NON SPOSTARE I SEGUENTI METODI
	 */
	public static List<String> getValuesFromNode(Node node){
		List<String> values = new ArrayList<>();
		NodeList childList = node.getChildNodes();
		List<Node> listNode = getNodesFromNodeList(childList);
		listNode.forEach(lnode -> values.add(lnode.getTextContent()));
		return values;
	}
	
	public static void getBonusFaith(Node node){
		List<Integer> values = new ArrayList<>();
		NodeList childList = node.getChildNodes();
		List<Node> listNode = getNodesFromNodeList(childList);
		listNode.forEach(lnode -> values.add(Integer.parseInt(lnode.getTextContent())));
		values.forEach(value -> System.out.println(value));
	}
	
	public static void getSpaceBonus(Node root){ 
		List<Node> listNode = getNodesFromNodeList(root.getChildNodes());
		for (Node node : listNode){
			List<Node> sublist = getNodesFromNodeList(node.getChildNodes());
			switch(node.getNodeName()){
				case "market" :{
					System.out.println(1);
					break;
				}
				case "harvest" :{
					System.out.println(2);
					break;
				}
				case "production" :{
					Node pro_spa = getNodesFromNodeList(node.getChildNodes()).get(0);
					Node ins_eff = getNodesFromNodeList(pro_spa.getChildNodes()).get(0);
					//Node ins_eff = getNodesFromNodeList(node.getChildNodes()).get(0);
					break;
				}
				case "turn_space" :{
					Node ins_eff = getNodesFromNodeList(node.getChildNodes()).get(0);
					Node res_eff = getNodesFromNodeList(ins_eff.getChildNodes()).get(0);
					Effect effect = factoryEffectBehavior(res_eff);
					break;
				}
				case "towers" :{
					System.out.println(5);
					break;
				}
			}
		}
	}
	
	public static void uploadTerritory(Node root){/*territories*/
		List<ICard> deck = new ArrayList<>();
		List<Node> territoryCards = getNodesFromNodeList(root.getChildNodes());
		for(Node territory : territoryCards){
			List<Node> parameters = getNodesFromNodeList(territory.getChildNodes());
			int age = Integer.parseInt(parameters.get(0).getTextContent().trim());
			String name = parameters.get(1).getTextContent().trim();
			List<Node> immediateEffects = getNodesFromNodeList(parameters.get(2).getChildNodes());
			Effect immediate = new Effect("NONE", null);
			if (immediateEffects.size() >= 1){
				immediate = factoryEffectBehavior(immediateEffects.get(0));
			}
			List<Node> permanentEffects = getNodesFromNodeList(parameters.get(3).getChildNodes());
			Effect permanent = new Effect("NONE", null);
			if (permanentEffects.size() >= 1){
				permanent = factoryEffectBehavior(permanentEffects.get(0));
			}
			int dice = Integer.parseInt(parameters.get(4).getTextContent().trim());
			deck.add(new Territory(name, age, immediate, permanent, dice));
			System.out.println("add territory card, name = " + name + "age = " + age + 
					", immediate = " + immediate.getWhenActivate() + 
					", permanent = " + permanent.getWhenActivate() + ", dice = " + dice);
		}
	}
	
	public static void uploadExcommunicationTiles(Node root){ /*ban_cards*/
		List<ICard> deck = new ArrayList<>();
		List<Node> banCards = getNodesFromNodeList(root.getChildNodes());
		for(Node banCard : banCards){
			List<Node> parameters = getNodesFromNodeList(banCard.getChildNodes());
			int age = Integer.parseInt(parameters.get(0).getTextContent());
			Effect effect = factoryEffectBehavior(parameters.get(1));
			deck.add(new ExcommunicationTile(age, effect));
			System.out.println("add ban card, age = " + age + ", effect = " + effect.getWhenActivate());
		}
	}
	
	public static Node openSingleNode(Node node){
		return getNodesFromNodeList(node.getChildNodes()).get(0);
	}
	
	public static void stamparisorse(Resource w){
		GC.RES_TYPES.stream()
			.filter(t -> w.get(t) > 0)
			.forEach(t -> System.out.println(t + " " + w.get(t)));
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
		List<Node> parameters = getNodesFromNodeList(node.getChildNodes());
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
				List<Node> gain = getNodesFromNodeList(parameters.get(0).getChildNodes());
				List<Node> each = getNodesFromNodeList(parameters.get(1).getChildNodes());
				Resource gainResource = getResourceFromNodeList(gain);
				Resource eachResource = getResourceFromNodeList(each);
				//TODO
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
					default : return null; //TODO generate exception
				}
			}
			case "work":{
				String action = parameters.get(0).getNodeName();
				int value = Integer.parseInt(parameters.get(0).getTextContent());
				behavior = new EffectWork(action, value);
				return new Effect(GC.IMMEDIATE, behavior);
			}
			case "trade":{
				List<Resource> payOptions = new ArrayList<>();
				List<Resource> gainOptions = new ArrayList<>();
				for (Node trade : parameters){
					List<Node> option = getNodesFromNodeList(trade.getChildNodes());
					List<Node> pay = getNodesFromNodeList(option.get(0).getChildNodes());
					List<Node> gain = getNodesFromNodeList(option.get(1).getChildNodes());
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
				System.out.println(100);
			}
		}
		List<Node> effects = getNodesFromNodeList(node.getChildNodes());
		Node effect = effects.get(0);
		System.err.println("parsing impossible");
		return null;
		
	}
	
	public static List<Node> getNodesFromNodeList(NodeList nodeList){
		List<Node> listNode = new ArrayList<>();
		for (int i = 0; i < nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE)
				listNode.add(node);
		}
		return listNode;
	}
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		System.out.println("test main");
		
		File fileXML = new File("default_settings.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		Document doc = db.parse(fileXML);
		
		Node root = doc.getDocumentElement();
		
		root.normalize();
		
		NodeList rootChildren = root.getChildNodes();
		System.out.println(root.getNodeName());
		
		List<Node> listNode = getNodesFromNodeList(rootChildren);
		
		//getSpaceBonus(listNode.get(0));
		//getBonusFaith(listNode.get(2));
		
		/*ban_cards*/
		Node x = listNode.get(4);
		
		uploadExcommunicationTiles(x);
		
		Node m = getNodesFromNodeList(listNode.get(3).getChildNodes()).get(0);
		
		uploadTerritory(m);
		
		System.out.println("finish");
		/*ban_card 19*/
		Node y = getNodesFromNodeList(x.getChildNodes()).get(19);
		/*effect*/
		Node w = getNodesFromNodeList(y.getChildNodes()).get(1);
		
		Effect z = factoryEffectBehavior(w);
		
		System.out.println(w.getNodeName());
		System.out.println(z.getWhenActivate());
		
		
		/*******************/
		
		/*dev_cards*/
		Node a = listNode.get(3);
		/*buildings*/
		Node a2 = getNodesFromNodeList(a.getChildNodes()).get(1);
		System.out.println(a2.getNodeName());
		/*dev_card 29*/
		Node b = getNodesFromNodeList(a2.getChildNodes()).get(4);
		/*effect_p*/
		Node c = getNodesFromNodeList(b.getChildNodes()).get(4);
		/*effect*/
		Node d = getNodesFromNodeList(c.getChildNodes()).get(0);
		
		Effect e = factoryEffectBehavior(d);
		
	}
}
