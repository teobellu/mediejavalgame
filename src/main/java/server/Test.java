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

import com.sun.media.jfxmedia.events.PlayerEvent;

import game.Player;
import game.Resource;
import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.effect.behaviors.EffectDiscountResource;
import game.effect.behaviors.EffectDontGetVictoryFor;
import game.effect.behaviors.EffectGetResource;
import game.effect.behaviors.EffectIncreaseActionPower;
import game.effect.behaviors.EffectLostVictoryForEach;

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
					IEffectBehavior d = factoryEffectBehavior(res_eff);
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
	 * This method reads a node, which represents an effect, and converts it into a effect behavior
	 * @param node The node which represents an effect
	 * @return Behavior of the effect, attention, not the actual effect
	 */
	public static IEffectBehavior factoryEffectBehavior(Node node){
		switch (node.getNodeName()){
			case "get_resources": {
				List<Node> typesOfResource = getNodesFromNodeList(node.getChildNodes());
				Resource resource = new Resource();
				typesOfResource.forEach(type -> 
					resource.add(type.getNodeName(), Integer.parseInt(type.getTextContent())));
				return new EffectGetResource(resource);
			}
			
			
			
			/*Excommunication tiles*/
			case "get_less_resources": {
				List<Node> typesOfResource = getNodesFromNodeList(node.getChildNodes());
				Resource resource = new Resource();
				typesOfResource.forEach(type -> 
					resource.add(type.getNodeName(), Integer.parseInt(type.getTextContent())));
				return new EffectDiscountResource(resource);
			}
			case "less_value_harvest_action": {
				int value = Integer.parseInt(node.getTextContent());
				return new EffectIncreaseActionPower("harvest", -value);
			}
			case "less_value_production_action": {
				int value = Integer.parseInt(node.getTextContent());
				return new EffectIncreaseActionPower("production", -value);
			}
			
			case "no_victory_points": {
				Node nodeWithTypeOfCard = getNodesFromNodeList(node.getChildNodes()).get(0);
				String typeOfCard = nodeWithTypeOfCard.getNodeName();
				return new EffectDontGetVictoryFor(typeOfCard);
			}
			case "lose_victory_points": {
				List<Node> typesOfResource = getNodesFromNodeList(node.getChildNodes());
				Resource resource = new Resource();
				typesOfResource.forEach(type -> 
					resource.add(type.getNodeName(), Integer.parseInt(type.getTextContent())));
				return new EffectLostVictoryForEach(resource);
			}
			
			default : {
				System.out.println(100);
			}
		}
		List<Node> effects = getNodesFromNodeList(node.getChildNodes());
		Node effect = effects.get(0);
		
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
		
	}
}
