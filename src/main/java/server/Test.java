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
		
		
		System.out.println(getValuesFromNode(listNode.get(2)).toString());
	}
}
