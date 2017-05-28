package game;

import java.util.*;

import game.Resource.*;
import game.effect.Effect;
import game.effect.what.*;
import game.effect.when.*;
import game.effect.when.EffectWhenFindValueAction;

public class Test {
	public static void main(String[] args) {
		
		Player p = new Player();
		DynamicAction joy = p.getDynamicBar();
		
		Resource ser3 = new Resource();		ser3.add(Resource.SERVANTS, 3);
		Resource ser2 = new Resource();		ser2.add(Resource.SERVANTS, 2);
		Resource ser1 = new Resource();		ser1.add(Resource.SERVANTS, 1);
		Resource vic3 = new Resource();		vic3.add(Resource.VICTORYPOINTS, 3);
		Resource vic1 = new Resource();		vic1.add(Resource.VICTORYPOINTS, 1);
		Resource oro1 = new Resource();		oro1.add(Resource.COINS, 1);
		
		joy.gain(ser3);
		joy.gain(ser3);
		joy.gain(vic3);
		joy.gain(vic3);
		joy.gain(vic3);
		
		
		
		
		
		
		
		
		List<Integer> list1 = new ArrayList<>();
		List<Integer> list2 = new ArrayList<>();
		
		list1.add(1);
		list1.add(3);
		list2.add(4);
		list2.add(5);
		list1.addAll(list2);
		System.out.println(list1.size());
		
		//9
		
		Effect eff6 = new EffectWhenEnd(new EffectLostVictoryForEach());
		eff6.setParameters(ser3);
		p.addEffect(eff6);
		
		joy.endGame();
		p.showRes();
		
		//7 ok -1 -1
		
		Effect eff8 = new EffectWhenEnd(new EffectLostVictoryBuilding());
		eff8.setParameters(ser2);
		p.addEffect(eff8);
		
		DevelopmentCard c1 = new Venture();
		DevelopmentCard c2 = new Building();
		c1.setCost(ser1);
		c2.setCost(ser2);
		p.addDevelopmentCard(c1);
		p.addDevelopmentCard(c2);
		
		Effect eff9 = new EffectWhenEnd(new EffectDontGetVictoryFor());
		eff9.setParameters("bf");
		
		p.addEffect(eff9);
		
		joy.endGame();
		p.showRes();
		
		
		Effect effx = new EffectWhenFindValueAction(new EffectReducePower());
		effx.setParameters(1);
		effx.setParameters("harvest");
		Effect effy = new EffectWhenFindValueAction(new EffectReducePower());
		effy.setParameters(2);
		effy.setParameters("production");
		
		p.addEffect(effx);
		p.addEffect(effy);
		p.addEffect(effy);
		
		joy.harvest(4);
		joy.product(100);
		//-1 da questo
		//-2 dall'effetto di prima
		
		/*
		Effect eff3 = new EffectWhenGain(new EffectSufferRake());
		eff3.setParameters(vic1);
		
		p.addEffect(eff3);
		
		joy.gain(vic3);
		p.showRes();
		
		FamilyMember c = new FamilyMember();
		c.setValue(1);
		c.setOwner(p);
		
		Effect eff5 = new EffectWhenIncreaseWorker(new EffectPayMoreForIncreaseWorker());
		eff5.setParameters(2);
		
		p.addEffect(eff5);
		joy.gain(ser3);
		
		try {
			joy.increaseWorker(c, 2);
		} catch (GameException e1) {
			System.out.println("nnn");
		}
		
		p.showRes();
		
		Effect eff6 = new EffectWhenEnd(new EffectLostVictoryForEach());
		eff6.setParameters(ser3);
		p.addEffect(eff6);
		
		joy.endGame();
		
		Effect eff8 = new EffectWhenEnd(new EffectLostVictoryBuilding());
		eff8.setParameters(ser1);
		p.addEffect(eff8);
		
		DevelopmentCard c1 = new Venture();
		DevelopmentCard c2 = new Building();
		c1.setCost(ser1);
		c2.setCost(ser3);
		p.addDevelopmentCard(c1);
		p.addDevelopmentCard(c2);
		
		joy.endGame();
		p.showRes();
		
		String g = "1h";
		try{
		System.out.println(Integer.parseInt(g) + " e ");}
		catch(NumberFormatException e){
			
		}
		//GameBoard game = new GameBoard(null);
/*
		Player g1 = new Player();
		Player g2 = new Player();
		
		Resource vic1 = new Resource();		vic1.add(type.VICTORYPOINTS, 1);
		Resource vic3 = new Resource();		vic3.add(type.VICTORYPOINTS, 3);
		Resource oro1 = new Resource();		oro1.add(type.COINS, 1);
		Resource oro2 = new Resource();		oro2.add(type.COINS, 2);
		Resource wood3 = new Resource();	wood3.add(type.WOOD, 3);
		Resource wood4 = new Resource();	wood4.add(type.WOOD, 4);
		Resource wood1 = new Resource();	wood1.add(type.WOOD, 1);
		Resource wood1stone1 = new Resource();	wood1stone1.add(type.WOOD, 1); wood1stone1.add(type.STONES, 1);
		
		g1.Gain(wood4);
		g1.Gain(vic3);
		g1.Gain(vic3);

		System.out.println("*********");
		
		Effect eff2 = new EffectWhenGain(new EffectSufferRake());
		eff2.setParameters(wood1stone1);
		
		Effect eff3 = new EffectWhenGain(new EffectSufferRake());
		eff3.setParameters(wood3);
		
		g1.addEffect(eff2);
		g1.addEffect(eff3);
		
		Effect eff5 = new EffectWhenEnd(new EffectLostVictoryForEach());
		eff5.setParameters(wood1);
		
		g1.addEffect(eff5);
		
		g1.controlGain(wood1);

		System.out.println("*********");

		
		Resource multi = new Resource();
		multi.add(type.COINS, 1);
		multi.add(type.STONES, 1);
		multi.add(type.WOOD, 1);
		multi.add(type.SERVANTS, 1);*/
		
		/**********************************************
		**************************************************************/
		
		FamilyMember fm = new FamilyMember();
		fm.setValue(1); 

		GameBoard t = new GameBoard(null);
	}
}
