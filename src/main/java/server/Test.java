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

import game.GC;
import game.Player;
import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;
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
	
	public static void getSpaceBonus(Node node){
		List<Integer> values = new ArrayList<>();
		NodeList childList = node.getChildNodes();
		List<Node> listNode = getNodesFromNodeList(childList);
		for (Node gettedNode : listNode){
			NodeList sublist = gettedNode.getChildNodes();
			switch(gettedNode.getNodeName()){
				case "market" :{
					System.out.println(1);
					break;
				}
				case "harvest" :{
					System.out.println(2);
					break;
				}
				case "production" :{
					System.out.println(3);
					break;
				}
				case "turn_space" :{
					Node ins_eff = getNodesFromNodeList(gettedNode.getChildNodes()).get(0);
					Node res_eff = getNodesFromNodeList(ins_eff.getChildNodes()).get(0);
					System.out.println(res_eff.getNodeName());
					Effect d = factoryEffectBehavior(res_eff);
					System.out.println(4);
					break;
				}
				case "towers" :{
					System.out.println(5);
					break;
				}
			}
		}
	}
	
	public static Node openSingleNode(Node node){
		return getNodesFromNodeList(node.getChildNodes()).get(0);
	}
	
	public static Effect getEffectFromNode(Node node){ 
		Node effect = getNodesFromNodeList(node.getChildNodes()).get(0);
		System.out.println(effect.getNodeName().toString());
		//effect.getNodeName();
		return null;
	}
	
	/**
	 * This method reads a node, which represents an effect, and converts it into a effect
	 * @param node The node which represents an effect
	 * @return Effect with his behavior
	 */
	public static Effect factoryEffectBehavior(Node node){
		IEffectBehavior behavior;
		switch (node.getNodeName()){
		
			case "get_resources": {
				List<Node> typesOfResource = getNodesFromNodeList(node.getChildNodes());
				Resource resource = new Resource();
				typesOfResource.forEach(type -> 
					resource.add(type.getNodeName(), Integer.parseInt(type.getTextContent())));
				behavior = new EffectGetResource(resource);
				
			}
			case "gain_for_every":{
				List<Node> parameters = getNodesFromNodeList(node.getChildNodes());
				Node resNode = parameters.get(0);
				Resource resource = new Resource(
						resNode.getNodeName(), Integer.parseInt(resNode.getTextContent()));
				String typeOfCard = parameters.get(1).getTextContent();
				behavior = new EffectRecieveRewardForEach(resource, typeOfCard);
				return new Effect(GC.IMMEDIATE, behavior);
			}
			case "get_discount":{
				List<Node> parameters = getNodesFromNodeList(node.getChildNodes());
				String action = parameters.get(0).getTextContent();
				parameters.remove(0);
				Resource resource = new Resource();
				parameters.forEach(type -> 
					resource.add(type.getNodeName(), Integer.parseInt(type.getTextContent())));
				behavior = new EffectDiscountResource(action, resource);
				return new Effect(GC.WHEN_FIND_COST_CARD, behavior);
			}
			case "block_floor":{
				behavior = new EffectOverruleObject();
				return new Effect(GC.WHEN_GET_TOWER_BONUS, behavior);
			}
			case "get_card":{
				List<Node> parameters = getNodesFromNodeList(node.getChildNodes());
				switch(parameters.size()){
					case 1: behavior = new EffectGetACard(Integer.parseInt(parameters.get(0).getTextContent()));
						break;
					case 2: behavior = new EffectGetACard(parameters.get(0).getTextContent(), Integer.parseInt(parameters.get(1).getTextContent()));
						break;
					default : return null; //TODO generate exception
				}
			}
			
			
			/*Excommunication tiles*/
			
			/**
			 * First Age
			 */
			case "get_less_resources": {
				List<Node> typesOfResource = getNodesFromNodeList(node.getChildNodes());
				Resource resource = new Resource();
				typesOfResource.forEach(type -> 
					resource.add(type.getNodeName(), Integer.parseInt(type.getTextContent())));
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
				List<Node> nodes = getNodesFromNodeList(node.getChildNodes());
				String action = nodes.get(0).getTextContent();
				int value = Integer.parseInt(nodes.get(1).getTextContent());
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
				//TODO Come lo vogliamo gestire?
				
			}
			
			/**
			 * Third Age
			 */
			case "no_victory_points": {
				Node nodeWithTypeOfCard = getNodesFromNodeList(node.getChildNodes()).get(0);
				String typeOfCard = nodeWithTypeOfCard.getTextContent();
				behavior = new EffectDontGetVictoryFor(typeOfCard);
				return new Effect(GC.WHEN_END, behavior);
			}
			case "lose_victory_points_depicted_resource": {
				List<Node> nodes = getNodesFromNodeList(node.getChildNodes());
				String typeOfCard = nodes.get(0).getTextContent();
				nodes.remove(0);
				Resource resource = new Resource();
				nodes.forEach(type -> 
					resource.add(type.getNodeName(), Integer.parseInt(type.getTextContent())));
				behavior = new EffectLostVictoryDepicted(typeOfCard, resource);
				return new Effect(GC.WHEN_END, behavior);
			}
			case "lose_victory_points": {
				List<Node> typesOfResource = getNodesFromNodeList(node.getChildNodes());
				Resource resource = new Resource();
				typesOfResource.forEach(type -> 
					resource.add(type.getNodeName(), Integer.parseInt(type.getTextContent())));
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
		
		getSpaceBonus(listNode.get(0));
		//getBonusFaith(listNode.get(2));
		
		/*ban_cards*/
		Node x = listNode.get(4);
		/*ban_card 19*/
		Node y = getNodesFromNodeList(x.getChildNodes()).get(19);
		/*effect*/
		Node w = getNodesFromNodeList(y.getChildNodes()).get(1);
		
		Effect z = factoryEffectBehavior(w);
		
		System.out.println(w.getNodeName());
		System.out.println(z.getWhenActivate());
		
	}
}
