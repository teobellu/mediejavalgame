package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardDeck<T extends ICard> implements Serializable{
	
	private List<T> deck;
	
	public CardDeck(){
		deck = new ArrayList<>();
	}

	public List<T> getDeck() {
		return deck;
	}

	public void setDeck(List<T> deck) {
		this.deck = deck;
	}
	
	public void shuffle() {
		Collections.shuffle(deck);
	}
	
	public void getCardByEra(int i){
		//TODO
	}
		
}
