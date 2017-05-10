package model;

import java.util.*;

import model.Resource.*;

public class Test {
	public static void main(String[] args) {
		
		GameBoard game = new GameBoard(null);
		/*
		game.roll();
		int a[] = new int[3];
		a = game.getdices();
		System.out.println(a[0] + " " + a[1] + " " + a[2]);
		game.roll();
		a = game.getdices();
		System.out.println(a[0] + " " + a[1] + " " + a[2]);*/
		
		Player g1 = new Player();
		Player g2 = new Player();
		
		Resource wood4vic1 = new Resource();
		wood4vic1.add(type.WOOD, 4);
		wood4vic1.add(type.STONES, 0);
		wood4vic1.add(type.VICTORYPOINTS, 1);
		
		Resource oro2 = new Resource();		oro2.add(type.COINS, 2);
		Resource wood3 = new Resource();	wood3.add(type.WOOD, 3);
		Resource wood1 = new Resource();	wood1.add(type.WOOD, 1);
		Effect eff = new EffectAbstract(g2);
		eff = new EffectGetResource(eff, oro2);
		eff = new EffectGetResource(eff, wood3);
		eff = new EffectGetResource(eff, wood1);
		eff = new EffectVictoryForEach(eff, wood1);
		eff.effect();
		
		try {
			g2.Pay(wood4vic1);
			System.out.println("il giocatore ha pagato");
		} catch (GameException e) {
			System.out.println("il giocatore non può pagare");
		}
	}
}
