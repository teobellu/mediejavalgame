package controller;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import exceptions.GameException;
import game.DynamicAction;
import game.FamilyMember;
import game.GC;
import game.Game;
import game.GameBoard;
import game.effect.Effect;
import game.effect.behaviors.EffectOverruleObject;
import server.Room;
import util.FakePlayer;

public class TestDynamicAction {
	
	private Room room;
	private Game game;
	private DynamicAction dynamicA;
	private FakePlayer player;
	private FamilyMember familiar;
	
	public TestDynamicAction() throws Exception{
//		room = new Room(null);
//		room.run();
//		game = room.getGame();
		
		dynamicA = new DynamicAction(new Game(new Room(null)));
		
		//create a player
		player = new FakePlayer(null, GC.PLAYER_RED);
		familiar = new FamilyMember(GC.FM_BLACK);
		familiar.setOwner(player);
		
//		player.getClient().setRoom(room);
//		dynamicA = room.getGame().getDynamicBar();
//		dynamicA.setPlayer(player);
	}
	
	@Test
	public void createDynamicAction() throws Exception {
		//private DynamicAction dynamicA;
	}
	
	@Test
	public void operations() throws Exception {
		
	}
}
