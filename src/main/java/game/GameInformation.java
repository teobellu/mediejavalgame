package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class GameInformation{

	private GameBoard board;
	
	/**
	 * Used for activate a specific leader card TODO
	 */
	private HashMap<LeaderCard, Player> discardedLeader;
	private CardDeck<DevelopmentCard> developmentDeck;
	private CardDeck<ExcommunicationCard> excommunicationDeck;
	private CardDeck<LeaderCard> leaderDeck;
	
	private List<? extends ICard> qdevelopmentDeck;
	private List<? extends ICard> qexcommunicationDeck;
	private List<? extends ICard> qleaderDeck;
	
	
	public GameInformation(){
		discardedLeader = new HashMap<LeaderCard, Player>();
		developmentDeck = new CardDeck<>();
		excommunicationDeck = new CardDeck<>();
		leaderDeck = new CardDeck<>();
	}
	
	public void deckBuilder(UserConfig userConfig){
		//TODO
		//riempio i deck
	}

	
	public void setExcommunicationTitlesOnBoard(){
		ExcommunicationCard[] exCard = new ExcommunicationCard[3];
		Optional<ExcommunicationCard> c;
		for (int i = 0; i < 3; i++){
			c = excommunicationDeck.getDeck().stream()
				.filter(card -> card.getAge() == 1) //TODO
				.findFirst();
			if (!c.isPresent()) ; //TODO
			exCard[i] = c.get();
		}
	}

	public HashMap<LeaderCard, Player> getDiscardedLeader() {
		return discardedLeader;
	}

	public void addDiscardedLeader(LeaderCard discardedLeader, Player owner) {
		this.discardedLeader.putIfAbsent(discardedLeader, owner);
	}

}