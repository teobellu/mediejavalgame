package server;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConfigFileHandler {

	public static void main(String[] args) {
		validate();
	}
	
	public static boolean validate(){
		try {
			File fileXML = new File("default_settings.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			Document doc = db.parse(fileXML);
			
			Node root = doc.getDocumentElement();
			
			root.normalize();
			
			NodeList rootChildren = root.getChildNodes();
			
			for(int i = 0;i<rootChildren.getLength();i++){
				if(rootChildren.item(i).getNodeType() != Node.TEXT_NODE){
					if(!validateNodes(rootChildren.item(i))){
						return false;
					}
				}
			}
			
			int a = Integer.parseInt(rootChildren.item(2).getChildNodes().item(0).getNodeValue());
			List<Integer> bonus = new ArrayList<>();
			for (int i = 0; i < 13; i++)
				bonus.add(Integer.parseInt(rootChildren.item(2).getChildNodes().item(i).getNodeValue()));
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return false;
	}
	
	public void generateBonusFaith(NodeList rootBonusFaith){
		List<Integer> bonus = new ArrayList<>();
		for (int i = 0; i < rootBonusFaith.getLength(); i++){
			String value = rootBonusFaith.item(i).getNodeValue();
			bonus.add(Integer.parseInt(value));
		}
	}
	
	public void read(File xml){
		
	}
	
	public static boolean validateNodes(Node rootChild){
		
		System.out.println("rootChild: " + rootChild.getNodeName());
		if(Arrays.asList(ROOT_CHILDRENS).contains(rootChild.getNodeName())){
			if(rootChild.getNodeName() != TIMEOUT_START && rootChild.getNodeName() != TIMEOUT_TURN){
				if(rootChild.getNodeName() == DEV_CARDS){
					validateDevCards(rootChild);
				}
				
				return true;
			} else {
				return validateTimeout(rootChild);
			}
		} else {
			System.out.println("ERROR: wrong value at root level");
			return false;
		}
	}
	
	public static boolean validateTimeout(Node rootChild){
		try {
			Integer value = Integer.decode(rootChild.getFirstChild().getNodeValue().trim());
			//TODO posso salvare sto valore per usarlo dopo?
			return true;
		} catch (NumberFormatException e) {
			System.out.println("ERROR: Timeout must be an integer value");
			return false;
		}
	}
	
	public static boolean validateDevCards(Node rootChild){
		
		if(Arrays.asList(DEV_CARDS_CHILDREN).contains(rootChild.getNodeName())){
			
		}
		
		return false;
	}
	
	private static final String BONUS_ACTION = "bonus_actions";
	private static final String BONUS_BRIDGE = "bonus_bridge";
	private static final String BONUS_FAITH = "bonus_faith";
	private static final String DEV_CARDS = "dev_cards";
	private static final String BAN_CARDS = "ban_cards";
	private static final String TIMEOUT_START = "timeout_start";
	private static final String TIMEOUT_TURN = "timeout_turn";
	
	private static final String[] ROOT_CHILDRENS = new String[]{
			BONUS_ACTION,
			BONUS_BRIDGE,
			BONUS_FAITH,
			DEV_CARDS,
			BAN_CARDS,
			TIMEOUT_START,
			TIMEOUT_TURN
	};
	
	private static final String TERRITORIES = "territories";
	private static final String BUILDINGS = "buildings";
	private static final String CHARACTERS = "characters";
	private static final String ACHIEVEMENTS = "achievements";
	private static final String[] DEV_CARDS_CHILDREN = new String[]{
			TERRITORIES,
			BUILDINGS,
			CHARACTERS,
			ACHIEVEMENTS
	};
}
