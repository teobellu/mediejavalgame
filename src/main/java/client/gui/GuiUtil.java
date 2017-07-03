package client.gui;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class GuiUtil {
	
	public static final Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
	    for (Node node : gridPane.getChildren()) {
	    	
	    	if(GridPane.getColumnIndex(node)==null){
	    		System.out.println("\n###TROVATO UN NODO CON COLONNA A NULL###\n");
	    		GridPane.setColumnIndex(node, 0);
	    	}
	    	
	    	if(GridPane.getRowIndex(node)==null){
	    		System.out.println("\n###TROVATO UN NODO CON RIGA A NULL###\n");
	    		GridPane.setRowIndex(node, 0);
	    	}
	    	
	        if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
	            return node;
	        }
	    }
	    return null;
	}
	
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
	
	private GuiUtil(){
		
	}
}
