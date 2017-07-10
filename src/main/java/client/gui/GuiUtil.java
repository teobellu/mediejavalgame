package client.gui;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

/**
 * Some utils
 *
 */
public class GuiUtil {
	
	/**
	 * Replace underscores with spaces, and put every work in camelcase
	 * @param str
	 * @return
	 */
	public static final String cleanUnderscoresCapsFirst(String str) {
		str = str.replaceAll("_", " ");
		
	    String[] words = str.split(" ");
	    StringBuilder ret = new StringBuilder();
	    for(int i = 0; i < words.length; i++) {
	        ret.append(Character.toUpperCase(words[i].charAt(0)));
	        ret.append(words[i].substring(1));
	        if(i < words.length - 1) {
	            ret.append(' ');
	        }
	    }
	    return ret.toString();
	}
	
	/**
	 * Gets a node from a gridpane, and returns it
	 * @param gridPane the gridpane
	 * @param col column
	 * @param row row
	 * @return the node
	 */
	public static final Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
	    for (Node node : gridPane.getChildren()) {
	    	
	    	if(GridPane.getColumnIndex(node)==null){
	    		GridPane.setColumnIndex(node, 0);
	    	}
	    	
	    	if(GridPane.getRowIndex(node)==null){
	    		GridPane.setRowIndex(node, 0);
	    	}
	    	
	        if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
	            return node;
	        }
	    }
	    return null;
	}
	
	/**
	 * Private constructor
	 */
	private GuiUtil(){
		
	}
}
