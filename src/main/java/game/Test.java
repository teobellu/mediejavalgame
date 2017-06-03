package game;

import java.util.*;

import game.effect.Effect;
import game.effect.behaviors.*;

public class Test {
	public static void main(String[] args) {
		
		Player p = new Player();
		DynamicAction joy = p.getDynamicBar();
		
		Resource ser3 = new Resource(GC.RES_SERVANTS, 3);
		Resource ser2 = new Resource(GC.RES_SERVANTS, 2);
		Resource ser1 = new Resource(GC.RES_SERVANTS, 1);
		Resource coi3 = new Resource(GC.RES_COINS, 3);
		Resource vic3 = new Resource(GC.RES_VICTORYPOINTS, 3);
		Resource vic1 = new Resource(GC.RES_VICTORYPOINTS, 1);

		p.gain(coi3);
		
		DevelopmentCard b1 = new Building();
		b1.setCost(null);
		
		p.addDevelopmentCard(b1);
		p.addDevelopmentCard(b1);
		p.addDevelopmentCard(b1);
		
		Effect effA4 = new Effect(GC.IMMEDIATE, new EffectRecieveRewardForEach(vic3, GC.RES_COINS));
		
		p.addEffect(effA4);
		
		Effect imm1 = new Effect(GC.IMMEDIATE, new EffectGetResource(ser1));
		Effect imm2 = new Effect(GC.IMMEDIATE, new EffectGetResource(ser1));
		
		p.addEffect(imm1);
		p.addEffect(imm2);
		try {
			p.pay(ser2);
		} catch (GameException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		p.showRes();
		
		List<FamilyMember> f = new ArrayList<>();
		FamilyMember f1 = new FamilyMember(GC.FM_BLACK);
		FamilyMember f2 = new FamilyMember(GC.FM_ORANGE);
		FamilyMember f3 = new FamilyMember(GC.FM_WHITE);
		FamilyMember f4 = new FamilyMember(GC.FM_TRANSPARENT);
		f1.setValue(1);
		f2.setValue(3);
		f3.setValue(5);
		f4.setValue(6);
		f1.setOwner(p);
		f2.setOwner(p);
		f3.setOwner(p);
		f4.setOwner(p);
		f.add(f1);
		f.add(f2);
		f.add(f3);
		f.add(f4);
		p.setFreeMember((ArrayList<FamilyMember>) f);
		
		Effect yellow = new Effect(GC.WHEN_SET_FAMILIAR_START_POWER, new EffectSetFamiliarStartPower(GC.FM_COLOR, 1));
		
		p.addEffect(yellow);

		joy.activateEffect(GC.WHEN_SET_FAMILIAR_START_POWER);
		
		p.getFreeMember().stream()
			.forEach(fam -> System.out.println("value " + fam.getValue()));
		
		
		
		/**
		 * Deck of leaders cards
		 */
		List<ICard> leaderDeck = new ArrayList<>();
		/**
		 * Ludovico Ariosto
		 */
		Effect effLudovicoAriosto = new Effect(GC.WHEN_JOINING_SPACE, new EffectPositiveCheck());
		leaderDeck.add(new LeaderCard("Ludovico Ariosto", null, 0, 5, 0, 0, effLudovicoAriosto, true));
		/**
		 * Filippo Brunelleschi
		 */
		Effect effFilippoBrunelleschi = new Effect(GC.WHEN_PAY_TAX_TOWER, new EffectDiscountResource(GC.TAX_TOWER));
		leaderDeck.add(new LeaderCard("Filippo Brunelleschi", null, 0, 0, 5, 0, effFilippoBrunelleschi, true));
		
		
		
		p.addEffect(effFilippoBrunelleschi);
		p.addEffect(effFilippoBrunelleschi);
		
		Resource tax = new Resource();	
		tax.add(GC.TAX_TOWER);
		tax = (Resource) joy.activateEffect(tax, GC.WHEN_PAY_TAX_TOWER);
		p.gain(tax);
		p.gain(coi3);
		p.showRes();
		System.out.println("AA2");
		
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
		
		Effect eff6 = new Effect(GC.WHEN_END,new EffectLostVictoryForEach(ser3));
		p.addEffect(eff6);
		
		joy.endGame();
		p.showRes();
		
		//7 ok -1 -1
		
		Effect eff8 = new Effect(GC.WHEN_END,new EffectLostVictoryBuilding(ser2));
		p.addEffect(eff8);
		
		DevelopmentCard c1 = new Venture();
		DevelopmentCard c2 = new Building();
		c1.setCost(ser1);
		c2.setCost(ser2);
		p.addDevelopmentCard(c1);
		p.addDevelopmentCard(c2);
		
		Effect eff9 = new Effect(GC.WHEN_END, new EffectDontGetVictoryFor(GC.DEV_BUILDING));
		
		p.addEffect(eff9);
		
		joy.endGame();
		p.showRes();
		
		
		Effect effx = new Effect(GC.WHEN_FIND_VALUE_ACTION, new EffectIncreaseActionPower("harvest", -1));
		Effect effy = new Effect(GC.WHEN_FIND_VALUE_ACTION, new EffectIncreaseActionPower("production", -2));
		
		p.addEffect(effx);
		p.addEffect(effy);
		p.addEffect(effy);
		
		joy.harvest(4);
		joy.product(100);
		//-1 da questo
		//-2 dall'effetto di prima
		
		
		Effect eff3 = new Effect(GC.WHEN_GAIN, new EffectDiscountResource(vic1));
		
		p.addEffect(eff3);
		
		joy.gain(vic3);
		p.showRes();
		
		
		Effect eff5 = new Effect(GC.WHEN_INCREASE_WORKER, new EffectPayMoreForIncreaseWorker(2));
		
		p.addEffect(eff5);
		joy.gain(ser3);
		
		p.showRes();
		
		Effect eff60 = new Effect(GC.WHEN_END, new EffectLostVictoryForEach(ser3));
		p.addEffect(eff60);
		
		joy.endGame();
		
		Effect eff80 = new Effect(GC.WHEN_END, new EffectLostVictoryBuilding(ser1));
		p.addEffect(eff80);
		
		DevelopmentCard c10 = new Venture();
		DevelopmentCard c20 = new Building();
		c10.setCost(ser1);
		c20.setCost(ser3);
		p.addDevelopmentCard(c1);
		p.addDevelopmentCard(c2);
		
		joy.endGame();
		p.showRes();
		
		p.gain(vic3);
		
		p.addEffect(eff60);
		
		String g = "1h";
		try{
		System.out.println(Integer.parseInt(g) + " e ");}
		catch(NumberFormatException e){
		
			
			System.out.println("error by string g");
		}
		//GameBoard game = new GameBoard(null);
/*
		Player g1 = new Player();
		Player g2 = new Player();
		
		Resource vic1 = new Resource();		
		vic1.add(type.VICTORYPOINTS, 1);
		
		Resource vic3 = new Resource();		
		vic3.add(type.VICTORYPOINTS, 3);
		
		Resource oro1 = new Resource();		
		oro1.add(type.COINS, 1);
		
		Resource oro2 = new Resource();		
		oro2.add(type.COINS, 2);
		
		Resource wood3 = new Resource();	
		wood3.add(type.WOOD, 3);
		
		Resource wood4 = new Resource();	
		wood4.add(type.WOOD, 4);
		
		Resource wood1 = new Resource();	
		wood1.add(type.WOOD, 1);
		
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


		
		/***
		 * Creating card type
		 * **
		 * OPT = ONCE PER TURN
		 * **
		 */
	
	}
	
	//

}
