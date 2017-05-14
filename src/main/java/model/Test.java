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
		
		Resource oro2 = new Resource();		oro2.add(type.COINS, 2);
		Resource wood3 = new Resource();	wood3.add(type.WOOD, 3);
		Resource wood1 = new Resource();	wood1.add(type.WOOD, 1);
		Resource wood1stone1 = new Resource();	wood1stone1.add(type.WOOD, 1); wood1stone1.add(type.STONES, 1);
		
		
		Effect eff1 = new Effect(new EffectGetResource());
		eff1.setResource(wood3);
		
		Effect eff2 = new Effect(new EffectSufferRake());
		eff2.setResource(wood1stone1);
		
		Effect eff3 = new Effect(new EffectGetResource());
		eff3.setResource(wood1stone1);
		
		Effect eff4 = new Effect(new EffectSufferRake());
		eff4.setResource(wood1);
		
		g1.addEffect(eff1);
		g1.addEffect(eff2);
		g1.addEffect(eff3);
		g1.addEffect(eff4);
		
		g1.activateEffect();
		g1.activateEffect();
		
		try {
			//g1.controlPay(wood3);
			g1.controlPay(wood1);
			//g1.controlPay(wood1);
			g1.Pay(wood1stone1);
			//g1.controlPay(wood1);
			System.out.println("pagamento ok");
		} catch (GameException e) {
			System.out.println("pagamento ko");
		}
		
		
	}
}
