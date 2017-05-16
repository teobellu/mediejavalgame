package game;

import java.util.*;

import game.Resource.*;
import game.effect.Effect;
import game.effect.what.EffectLostVictoryForEach;
import game.effect.what.EffectSufferRake;
import game.effect.when.EffectWhenEnd;
import game.effect.when.EffectWhenGain;

public class Test {
	public static void main(String[] args) {
		
		
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
