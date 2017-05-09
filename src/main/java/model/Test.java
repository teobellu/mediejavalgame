package model;

import java.util.*;

import model.Resource.*;

public class Test {
	public static void main(String[] args) {
		
		GameBoard game = new GameBoard(null);
		
		game.roll();
		
		int a[] = new int[3];
		
		a = game.getdices();
		
		System.out.println(a[0] + " " + a[1] + " " + a[2]);
		
		game.roll();
		
		a = game.getdices();
		
		System.out.println(a[0] + " " + a[1] + " " + a[2]);
		
		Resource res = new Resource();
		
		res.add(type.COINS, 4);
		res.add(type.WOOD, 3);
		res.add(type.COINS, 2);
		
		System.out.println(res.get(type.COINS));
		System.out.println(res.get(type.WOOD));
		System.out.println(res.get(type.STONES));
		
		res.add(res);
		
		System.out.println(res.get(type.COINS));
		System.out.println(res.get(type.WOOD));
		System.out.println(res.get(type.STONES));
		
		Player g1 = new Player();
		Player g2 = new Player();
		
		g1.Gain(res);
		
		Resource h = new Resource();
		h.add(type.WOOD, 6);
		
		try {
			g1.Pay(h);
		} catch (GameException e) {
			e.printStackTrace();
		}
		
		
	}
}
