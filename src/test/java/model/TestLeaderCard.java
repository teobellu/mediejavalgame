package model;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * Test model: Leader Card
 * @author M
 *
 */
public class TestLeaderCard {
	
	@Test
    public void createLeaderCard() throws Exception {
		Function<Player, Boolean> trueFunction = player -> true;
		LeaderCard leader = new LeaderCard("Name", GC.NIX, trueFunction);
        assertTrue(leader.getName().equals("Name"));
        assertTrue(leader.getEffect().equals(GC.NIX));
        
        leader = new LeaderCard("Name2", null, null);
        assertTrue(leader.canPlayThis(null));
    }
	
	@Test
    public void activableLeaderCard() throws Exception {
		Function<Player, Boolean> trueFunction = player -> true;
		Function<Player, Boolean> falseFunction = player -> false;
        List<LeaderCard> list = new ArrayList<>();
        list.add(new LeaderCard("Name1", GC.NIX, trueFunction));
        list.add(new LeaderCard("Name2", GC.NIX, falseFunction));
        assertTrue(list.size() == 2);
        //I filter from list only activable leader cards
        list = list.stream()
        	.filter(leader -> leader.canPlayThis(null))
        	.collect(Collectors.toList());
        assertTrue(list.size() == 1);
    }
	
}
