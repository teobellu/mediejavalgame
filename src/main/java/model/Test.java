package model;

import java.util.*;

import model.Resource.*;

public class Test {
	public static void main(String[] args) {
		
		GameBoard game = new GameBoard(null);

		Player g1 = new Player();
		Player g2 = new Player();
		
		Resource wood4vic1 = new Resource();
		wood4vic1.add(type.WOOD, 4);
		wood4vic1.add(type.STONES, 0);
		wood4vic1.add(type.VICTORYPOINTS, 1);
		
		Resource vic1 = new Resource();		vic1.add(type.VICTORYPOINTS, 1);
		Resource vic3 = new Resource();		vic3.add(type.VICTORYPOINTS, 3);
		Resource oro1 = new Resource();		oro1.add(type.COINS, 1);
		Resource oro2 = new Resource();		oro2.add(type.COINS, 2);
		Resource wood3 = new Resource();	wood3.add(type.WOOD, 3);
		Resource wood4 = new Resource();	wood4.add(type.WOOD, 4);
		Resource wood1 = new Resource();	wood1.add(type.WOOD, 1);
		Resource wood1stone1 = new Resource();	wood1stone1.add(type.WOOD, 1); wood1stone1.add(type.STONES, 1);
		
		Resource multi = new Resource();
		multi.add(type.COINS, 1);
		multi.add(type.STONES, 1);
		multi.add(type.WOOD, 1);
		multi.add(type.SERVANTS, 1);
		
		
		
		Effect eff1 = new Effect(new EffectGetResource());
		eff1.setParameters(wood4vic1);
		
		Effect eff2 = new Effect(new EffectSufferRake());
		eff2.setParameters(wood1stone1);
		
		Effect eff3 = new Effect(new EffectGetResource());
		eff3.setParameters(wood1stone1);
		
		Effect eff4 = new Effect(new EffectSufferRake());
		eff4.setParameters(wood1);
		
		Effect eff5 = new Effect(new EffectLostVictoryForEach());
		eff5.setParameters(multi);
		
		Effect eff6 = new Effect(new EffectLostVictoryForEach());
		eff6.setParameters(oro1);
		
		//g1.addEffect(eff1);
		//g1.addEffect(eff2);
		//g1.addEffect(eff3);
		//g1.addEffect(eff4);
		//g1.addEffect(eff5);
		//g1.addEffect(eff6);
		
		g1.activateEffect();
		g1.activateEffect();
		
		g1.Gain(wood4);
		g1.Gain(oro2);
		g1.Gain(oro2);
		g1.Gain(wood1stone1);
		for(int c : new int[9]) g1.Gain(vic3);
		
		g1.showRes();
		
		System.out.println("separatore");
		
		g1.endGame();
		
		g1.showRes();
		
		Effect eff7 = new Effect(new EffectDoHarvest());
		eff7.setParameters(4);
		
		DevelopmentCard c1 = new Territory();
		c1.addPermanentEffect(eff1);
		c1.setDice(5);
		
		DevelopmentCard c2 = new Building();
		c2.addPermanentEffect(eff1);
		c2.setDice(1);
		
		try {
			g1.addDevelopmentCard(c1);
			g1.addDevelopmentCard(c2);
		} catch (GameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		g1.addEffect(eff7);
		g1.activateEffect();

		System.out.println("separatore");
		g1.showRes();
		
	}
}
