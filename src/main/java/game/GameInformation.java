package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import game.development.DevelopmentCard;
import game.effect.Effect;
import game.effect.IEffectBehavior;
import game.effect.behaviors.EffectCopyLeader;
import game.effect.behaviors.EffectDiscountResource;
import game.effect.behaviors.EffectGetResource;
import game.effect.behaviors.EffectIncreaseFamiliarStartPower;
import game.effect.behaviors.EffectOverruleObject;
import game.effect.behaviors.EffectSantaRita;
import game.effect.behaviors.EffectSelectAndSetFamiliarStartPower;
import game.effect.behaviors.EffectSetFamiliarStartPower;
import game.effect.behaviors.EffectWork;

public class GameInformation{

	private GameBoard board;
	private Game game;
	/**
	 * Used for activate a specific leader card TODO
	 */
	private Map<LeaderCard, Player> discardedLeader;
	
	private List<LeaderCard> leaderDeck;
	private List<DevelopmentCard> developmentDeck;
	private List<ExcommunicationCard> excommunicationDeck;
	
	private List<Player> playersTurn;
	private List<Player> headPlayersTurn;
	private List<Player> tailPlayersTurn;
	
	/**
	 * TODO trova il prossimo ordine dei turni
	 */
	public void nextPlayersTurn(){
		List<Player> nextList = new ArrayList<>();
		headPlayersTurn.forEach(player -> nextList.add(player));
		playersTurn.stream().forEach(player -> nextList.add(player));
		playersTurn.clear();
		nextList.stream()
			.filter(player -> !playersTurn.contains(player))
			.forEach(player -> playersTurn.add(player));
		headPlayersTurn.clear();
	}
	
	/**
	 * TODO VERIFICA SE IL GIOCATORE DEVE SALTARE IL TURNO
	 * @param player
	 * @return
	 */
	public boolean hasToJumpTurn(Player player){
		if (!tailPlayersTurn.contains(player))
			return false;
		tailPlayersTurn.removeIf(item -> item == player); //TODO ogni turno di gioco => non posso
		return true;
	}
	
	/**
	 * Assigns prizes at the top of the military points track
	 * @param players All the players in the room
	 */
	public void awardPrizeMilitary(List<Player> players){
		List<Player> competitors = new ArrayList<>();
		competitors.addAll(players);
		List<Player> winners = getTheRichest(competitors, GC.RES_MILITARYPOINTS);
		winners.forEach(player -> player.gain(GC.FIRST_PRIZE_MILITARY));
		if (winners.size() > 1)
			return;
		competitors.removeAll(winners);
		winners = getTheRichest(competitors, GC.RES_MILITARYPOINTS);
		winners.forEach(player -> player.gain(GC.SECOND_PRIZE_MILITARY));
	}
	
	/**
	 * This method returns the winners of the game
	 * @param players Players in the room
	 * @return winners of the game
	 */
	public List<Player> getWinners(List<Player> players){
		return getTheRichest(players, GC.RES_VICTORYPOINTS);
	}
	
	/**
	 * Returns players who have more resources of a certain type than others
	 * @param players Competitors of the prize
	 * @param resourceType Type of resource
	 * @return Winners, the richest player of that type of resource
	 */
	private List<Player> getTheRichest(List<Player> players, String resourceType){
		int max = 0;
		for (Player player : players)
			if (max < player.getResource(resourceType))
				max = player.getResource(resourceType);
		final int requirement = max;
		return players.stream()
				.filter(player -> player.getResource(resourceType) == requirement)
				.collect(Collectors.toList());
	}
	
	public List<LeaderCard> getLeaderDeck() {
		return leaderDeck;
	}
	
	public GameInformation(){
		discardedLeader = new HashMap<>();
		playersTurn = new ArrayList<>();
		headPlayersTurn = new ArrayList<>();
		tailPlayersTurn = new ArrayList<>();
		generateLeaderCard();
	}
	
	public void deckBuilder(UserConfig userConfig){
		//TODO
		//riempio i deck
	}

	/*
	
	public void setExcommunicationTitlesOnBoard(){
		ExcommunicationCard[] exCard = new ExcommunicationCard[3];
		Optional<ExcommunicationCard> c;
		for (int i = 0; i < 3; i++){
			c = excommunicationDeck.getDeck().stream()
				.filter(card -> card.getAge() == 1) //TODO
				.findFirst();
			if (c.isPresent()) //TODO
				exCard[i] = c.get();
		}
	}*/

	public Map<LeaderCard, Player> getDiscardedLeader() {
		return discardedLeader;
	}

	public void addDiscardedLeader(LeaderCard discardedLeader, Player owner) {
		//PLAYER = LEADER.GET EFFECT. GET OWNER ? TODO
		this.discardedLeader.putIfAbsent(discardedLeader, owner);
	}
	
	/**
	 * This method generates the leader card deck using Lambda Expression and Function class;
	 * In fact, by specifying, we do not have to load them from file.
	 * We have separated all leader cards to make the game as configurable as possible 
	 * (Programmer side)
	 */
	private void generateLeaderCard(){
		leaderDeck = new ArrayList<>();
		
		Function<Player, Boolean> requirement;
		IEffectBehavior behavior;
		Effect effect;
		
		Resource resource;
		
		/**
		 * Francesco Sforza
		 */
		
		requirement = player -> player.getDevelopmentCards(GC.DEV_VENTURE).size() >= 5;
		behavior = new EffectWork(GC.HARVEST, 1);
		effect = new Effect(GC.ONCE_PER_TURN, behavior);
		leaderDeck.add(new LeaderCard("Francesco Sforza", effect, requirement));
		
		/**
		 * Ludovico Ariosto
		 */
		
		requirement = player -> player.getDevelopmentCards(GC.DEV_CHARACTER).size() >= 5;
		behavior = new EffectOverruleObject();
		effect = new Effect(GC.WHEN_JOINING_SPACE, behavior);
		leaderDeck.add(new LeaderCard("Ludovico Ariosto", effect, requirement));
		
		/**
		 * Filippo Brunelleschi
		 */
		
		requirement = player -> player.getDevelopmentCards(GC.DEV_BUILDING).size() >= 5;
		behavior = new EffectOverruleObject();
		effect = new Effect(GC.WHEN_PAY_TAX_TOWER, behavior);
		leaderDeck.add(new LeaderCard("Filippo Brunelleschi", effect, requirement));
		
		/**
		 * Sigismondo Malatesta
		 */
		
		requirement = player -> 
			player.getResource().get(GC.RES_MILITARYPOINTS) >= 7 &&
			player.getResource().get(GC.RES_FAITHPOINTS) >= 3;
		behavior = new EffectIncreaseFamiliarStartPower(GC.FM_TRANSPARENT, 3);
		effect = new Effect(GC.WHEN_ROLL, behavior);
		leaderDeck.add(new LeaderCard("Sigismondo Malatesta", effect, requirement));
		
		/**
		 * Girolamo Savonarola
		 */
		
		requirement = player -> player.getResource().get(GC.RES_COINS) >= 18;
		resource = new Resource(GC.RES_FAITHPOINTS, 1);
		behavior = new EffectGetResource(resource);
		effect = new Effect(GC.ONCE_PER_TURN, behavior);
		leaderDeck.add(new LeaderCard("Girolamo Savonarola", effect, requirement));
		
		/**
		 * Michelangelo Buonarroti
		 */
		
		requirement = player -> player.getResource().get(GC.RES_STONES) >= 10;
		resource = new Resource(GC.RES_COINS, 3);
		behavior = new EffectGetResource(resource);
		effect = new Effect(GC.ONCE_PER_TURN, behavior);
		leaderDeck.add(new LeaderCard("Michelangelo Buonarroti", effect, requirement));
		
		/**
		 * Giovanni dalle Bande Nere
		 */
		
		requirement = player -> player.getResource().get(GC.RES_MILITARYPOINTS) >= 12;
		resource = new Resource();
		resource.add(GC.RES_WOOD, 1);
		resource.add(GC.RES_STONES, 1);
		resource.add(GC.RES_COINS, 1);
		behavior = new EffectGetResource(resource);
		effect = new Effect(GC.ONCE_PER_TURN, behavior);
		leaderDeck.add(new LeaderCard("Giovanni dalle Bande Nere", effect, requirement));
		
		/**
		 * Leonardo da Vinci
		 */
		
		requirement = player -> 
			player.getDevelopmentCards(GC.DEV_CHARACTER).size() >= 4 &&
			player.getDevelopmentCards(GC.DEV_TERRITORY).size() >= 2;
		behavior = new EffectWork(GC.PRODUCTION, 0);
		effect = new Effect(GC.ONCE_PER_TURN, behavior);
		leaderDeck.add(new LeaderCard("Leonardo da Vinci", effect, requirement));
		
		/**
		 * Sandro Botticelli
		 */
			
		requirement = player -> player.getResource().get(GC.RES_WOOD) >= 10;
		resource = new Resource();
		resource.add(GC.RES_MILITARYPOINTS, 2);
		resource.add(GC.RES_VICTORYPOINTS, 1);
		behavior = new EffectGetResource(resource);
		effect = new Effect(GC.ONCE_PER_TURN, behavior);
		leaderDeck.add(new LeaderCard("Sandro Botticelli", effect, requirement));
		
		/**
		 * Ludovico il Moro
		 */
		
		requirement = player -> 
			player.getDevelopmentCards(GC.DEV_TERRITORY).size() >= 2 &&
			player.getDevelopmentCards(GC.DEV_CHARACTER).size() >= 2 &&
			player.getDevelopmentCards(GC.DEV_BUILDING).size() >= 2 &&
			player.getDevelopmentCards(GC.DEV_VENTURE).size() >= 2;
		behavior = new EffectSetFamiliarStartPower(GC.FM_COLOR, 5);
		effect = new Effect(GC.WHEN_ROLL, behavior);
		leaderDeck.add(new LeaderCard("Ludovico il Moro", effect, requirement));
		
		/**
		 * Lucrezia Borgia
		 */
		
		requirement = player -> 
			player.getDevelopmentCards(GC.DEV_TERRITORY).size() == 6 ||
			player.getDevelopmentCards(GC.DEV_CHARACTER).size() == 6 ||
			player.getDevelopmentCards(GC.DEV_BUILDING).size() == 6 ||
			player.getDevelopmentCards(GC.DEV_VENTURE).size() == 6;
		behavior = new EffectIncreaseFamiliarStartPower(GC.FM_COLOR, 2);
		effect = new Effect(GC.WHEN_ROLL, behavior);
		leaderDeck.add(new LeaderCard("Lucrezia Borgia", effect, requirement));
		
		/**
		 * Federico da Montefeltro
		 */
		
		requirement = player -> player.getDevelopmentCards(GC.DEV_TERRITORY).size() >= 5; 
		behavior = new EffectSelectAndSetFamiliarStartPower(GC.FM_COLOR, 6);
		effect = new Effect(GC.ONCE_PER_TURN, behavior);
		leaderDeck.add(new LeaderCard("Federico da Montefeltro", effect, requirement));
		
		/**
		 * Lorenzo de� Medici
		 */
		
		requirement = player -> player.getResource().get(GC.RES_VICTORYPOINTS) >= 35;
		behavior = new EffectCopyLeader();
		effect = new Effect(GC.IMMEDIATE, behavior);
		leaderDeck.add(new LeaderCard("Lorenzo de� Medici", effect, requirement));
		
		/**
		 * Sisto IV
		 */
		
		requirement = player -> 
			player.getResource().get(GC.RES_WOOD) >= 6 &&
			player.getResource().get(GC.RES_STONES) >= 6 &&
			player.getResource().get(GC.RES_COINS) >= 6 &&
			player.getResource().get(GC.RES_SERVANTS) >= 6;
		resource = new Resource(GC.RES_VICTORYPOINTS, 5);
		behavior = new EffectGetResource(resource);
		effect = new Effect(GC.WHEN_SHOW_SUPPORT, behavior);
		leaderDeck.add(new LeaderCard("Sisto IV", effect, requirement));
		
		/**
		 * Cesare Borgia
		 */
		
		requirement = player -> 
			player.getDevelopmentCards(GC.DEV_BUILDING).size() >= 3 &&
			player.getResource().get(GC.RES_COINS) >= 12 &&
			player.getResource().get(GC.RES_FAITHPOINTS) >= 2;
		behavior = new EffectOverruleObject(GC.DEV_TERRITORY);
		effect = new Effect(GC.WHEN_PAY_REQUIREMENT, behavior);
		leaderDeck.add(new LeaderCard("Cesare Borgia", effect, requirement));
		
		/**
		 * Santa Rita
		 */
		
		requirement = player -> player.getResource().get(GC.RES_FAITHPOINTS) >= 8;
		resource = new Resource();
		resource.add(GC.RES_WOOD, 1);
		resource.add(GC.RES_STONES, 1);
		resource.add(GC.RES_COINS, 1);
		resource.add(GC.RES_SERVANTS, 1);
		behavior = new EffectSantaRita(resource);
		effect = new Effect(GC.WHEN_GAIN, behavior);
		leaderDeck.add(new LeaderCard("Santa Rita", effect, requirement));
		
		/**
		 * Cosimo de� Medici
		 */
		
		requirement = player -> 
			player.getDevelopmentCards(GC.DEV_BUILDING).size() >= 4 &&
			player.getDevelopmentCards(GC.DEV_CHARACTER).size() >= 2;
		resource = new Resource();
		resource.add(GC.RES_SERVANTS, 3);
		resource.add(GC.RES_VICTORYPOINTS, 1);
		behavior = new EffectGetResource(resource);
		effect = new Effect(GC.ONCE_PER_TURN, behavior);
		leaderDeck.add(new LeaderCard("Cosimo de� Medici", effect, requirement));	
		
		/**
		 * Bartolomeo Colleoni
		 */
		
		requirement = player -> 
			player.getDevelopmentCards(GC.DEV_TERRITORY).size() >= 4 &&
			player.getDevelopmentCards(GC.DEV_VENTURE).size() >= 2;
		resource = new Resource(GC.RES_VICTORYPOINTS, 4);
		behavior = new EffectGetResource(resource);
		effect = new Effect(GC.ONCE_PER_TURN, behavior);
		leaderDeck.add(new LeaderCard("Bartolomeo Colleoni", effect, requirement));
		
		/**
		 * Ludovico III Gonzaga
		 */
		
		requirement = player -> player.getResource().get(GC.RES_SERVANTS) >= 15;
		resource = new Resource(GC.RES_COUNCIL, 1);
		behavior = new EffectGetResource(resource);
		effect = new Effect(GC.ONCE_PER_TURN, behavior);
		leaderDeck.add(new LeaderCard("Ludovico III Gonzaga", effect, requirement));
		
		/**
		 * Pico della Mirandola
		 */
		
		requirement = player -> 
			player.getDevelopmentCards(GC.DEV_VENTURE).size() >= 4 &&
			player.getDevelopmentCards(GC.DEV_BUILDING).size() >= 2;
		resource = new Resource(GC.RES_COINS, 3);
		behavior = new EffectDiscountResource(GC.DEV_TYPES, resource);
		effect = new Effect(GC.WHEN_FIND_COST_CARD, behavior);
		leaderDeck.add(new LeaderCard("Pico della Mirandola", effect, requirement));
		
		Collections.shuffle(leaderDeck);
	}

	public List<Player> getPlayersTurn() {
		return playersTurn;
	}

	public void setPlayersTurn(List<Player> playersTurn) {
		this.playersTurn = playersTurn;
	}

	public List<Player> getHeadPlayersTurn() {
		return headPlayersTurn;
	}

	public void setHeadPlayersTurn(List<Player> headPlayersTurn) {
		this.headPlayersTurn = headPlayersTurn;
	}

	public List<Player> getTailPlayersTurn() {
		return tailPlayersTurn;
	}

	public void setTailPlayersTurn(List<Player> tailPlayersTurn) {
		this.tailPlayersTurn = tailPlayersTurn;
	}

}