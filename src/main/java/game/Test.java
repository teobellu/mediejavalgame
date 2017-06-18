package game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import exceptions.GameException;
import game.development.Building;
import game.development.DevelopmentCard;
import game.development.Venture;
import game.effect.Effect;
import game.effect.behaviors.EffectDiscountResource;
import game.effect.behaviors.EffectDontGetVictoryFor;
import game.effect.behaviors.EffectGetACard;
import game.effect.behaviors.EffectGetResource;
import game.effect.behaviors.EffectIncreaseActionPower;
import game.effect.behaviors.EffectLostVictoryDepicted;
import game.effect.behaviors.EffectLostVictoryForEach;
import game.effect.behaviors.EffectOverruleObject;
import game.effect.behaviors.EffectPayMoreForIncreaseWorker;
import game.effect.behaviors.EffectRecieveRewardForEach;
import game.effect.behaviors.EffectSantaRita;
import game.effect.behaviors.EffectSetFamiliarStartPower;

/**
 * classe per testare i metodi
 *
 */
public class Test {
	
	public static final Resource ser3 = new Resource(GC.RES_SERVANTS, 3);
	public static final Resource ser2 = new Resource(GC.RES_SERVANTS, 2);
	public static final Resource ser1 = new Resource(GC.RES_SERVANTS, 1);
	public static final Resource sto1 = new Resource(GC.RES_STONES, 1);
	public static final Resource coi3 = new Resource(GC.RES_COINS, 3);
	public static final Resource coi1 = new Resource(GC.RES_COINS, 1);
	public static final Resource vic3 = new Resource(GC.RES_VICTORYPOINTS, 3);
	public static final Resource vic1 = new Resource(GC.RES_VICTORYPOINTS, 1);
	public static final Resource mil3 = new Resource(GC.RES_MILITARYPOINTS, 3);
	
	
	public static void main(String[] args) throws GameException {
		
		Player serial = new Player(null);
		
		serial.gain(coi1);
		
		serial.addDevelopmentCard(new Building(1, "x", null, GC.NIX, GC.NIX, 2));
		
		serial.addDevelopmentCard(new Venture(3, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 9));
		
		
		FileOutputStream out;
		ObjectOutputStream oos;
		FileInputStream in;
		ObjectInputStream ois;
		Player deserial = null;
		try {
			out = new FileOutputStream("save.ser");
			oos = new ObjectOutputStream( out );
			oos.writeObject(serial);
			oos.close();
			in = new FileInputStream("save.ser");
			ois = new ObjectInputStream( in );
			deserial = (Player) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		deserial.showRes();
		int cc = deserial.getDevelopmentCards(GC.DEV_BUILDING).get(0).getDice();
		String pp = deserial.getDevelopmentCards(GC.DEV_BUILDING).get(0).getImmediateEffect().get(0).getIEffectBehavior().getClass().toString();
		System.out.print(cc + "  " + pp);
		
		DynamicAction dd = new DynamicAction(null);
		dd.setPlayer(deserial);

		dd.endGame();
		deserial.showRes();
		
		
		
		Resource santa = new Resource(GC.RES_COINS, 1);
		Resource rita = new Resource(GC.RES_COINS, 2);
		santa.add(GC.RES_WOOD, 1);
		santa.add(GC.RES_STONES, 1);
		santa.add(GC.RES_SERVANTS, 1);
		rita.add(GC.RES_MILITARYPOINTS, 1);
		/**
		 * TEST_OF_TOWER
		 */
		GameInformation gi = new GameInformation(null);
		List<LeaderCard> ld = gi.getLeaderDeck();
		
		Player p = new Player(null);
		DynamicAction joy = new DynamicAction(null);
		joy.setPlayer(p);
		GameBoard b = new GameBoard(null);
		joy.setBoardForTestOnly(b);
		
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
		
		//b.getWorkLongSpace(GC.HARVEST)args;
		
		Player g1 = new Player(null);
		Player g2 = new Player(null);
		Player g3 = new Player(null);
		Player g4 = new Player(null);
		
		g1.gain(new Resource(GC.RES_COINS, 1));
		g2.gain(new Resource(GC.RES_COINS, 2));
		g3.gain(new Resource(GC.RES_COINS, 3));
		g4.gain(new Resource(GC.RES_COINS, 4));
		
		g1.gain(new Resource(GC.RES_MILITARYPOINTS, 7));
		g2.gain(new Resource(GC.RES_MILITARYPOINTS, 6));
		g4.gain(new Resource(GC.RES_MILITARYPOINTS, 6));
		
		List<Player> list = Arrays.asList(g1, g2, g3, g4);
		
		//gi.awardPrizeMilitary(list);
		
		list.forEach(xp -> xp.showRes());
		
		System.out.println("**************");
		
		gi.getPlayersTurn().addAll(Arrays.asList(g1, g2, g3, g4));
		
		DynamicAction d1 = new DynamicAction(null);
		DynamicAction d2 = new DynamicAction(null);
		DynamicAction d3 = new DynamicAction(null);
		DynamicAction d4 = new DynamicAction(null);
		
		d1.setPlayer(g1);
		d2.setPlayer(g2);
		d3.setPlayer(g3);
		d4.setPlayer(g4);
		
		gi.getHeadPlayersTurn().add(g2);
		gi.getHeadPlayersTurn().add(g3);
		gi.getHeadPlayersTurn().add(g2);
		gi.getTailPlayersTurn().add(g3);
		
		//gi.nextPlayersTurn();
		
		for (Player x : gi.getPlayersTurn())
			if (x == g1) System.out.println(1);
			else if (x == g2) System.out.println(2);
			else if (x == g3) System.out.println(3);
			else if (x == g4) System.out.println(4);
		
		
		p.addEffect(new Effect(GC.IMMEDIATE, new EffectGetACard(GC.DEV_TERRITORY, 1, sto1)));
		
		joy.placeWork(f3, GC.HARVEST);
		joy.placeWork(f4, GC.HARVEST);
		
		joy.placeCouncilPalace(f2);
		
		p.showRes();
		
		System.out.println("\n\n\n\n\n\n\n\n");
		
		
		
		
		
		Effect effLudovicoAriosto = new Effect(GC.WHEN_JOINING_SPACE, new EffectOverruleObject());
		
		p.addEffect(effLudovicoAriosto);
		
		//p.addEffect(new Effect(GC.WHEN_PLACE_FAMILIAR_MARKET, new EffectOverruleObject()));
		
		p.addEffect(new Effect(GC.WHEN_GAIN,new EffectDiscountResource(null)));
		p.addEffect(new Effect(GC.WHEN_GAIN,new EffectSantaRita(santa)));
		Effect x1 = new Effect(GC.IMMEDIATE, new EffectGetResource(santa));
		x1.setSource(GC.DEV_BUILDING);
		
		p.addEffect(x1);
		p.showRes();
		
		p.gain(coi3);
		p.gain(coi3);
		p.gain(coi3);
		p.gain(mil3);
		p.gain(sto1);
		p.gain(sto1);
		p.gain(sto1);
		p.gain(sto1);
		p.gain(sto1);
		p.addEffect(new Effect(GC.WHEN_FIND_VALUE_ACTION, new EffectIncreaseActionPower(GC.DEV_TERRITORY, 2)));
		p.addEffect(new Effect(GC.WHEN_FIND_COST_CARD, new EffectDiscountResource(GC.DEV_TYPES, sto1)));
		p.addEffect(new Effect(GC.WHEN_PAY_REQUIREMENT, new EffectOverruleObject(GC.DEV_TERRITORY)));
		p.addEffect(new Effect(GC.WHEN_SHOW_SUPPORT, new EffectGetResource(ser3)));
		
		p.addEffect(new Effect(GC.WHEN_GET_TOWER_BONUS, new EffectOverruleObject()));
		
		joy.placeInTower(f4, 3, 0);
		joy.showVaticanSupport(1);
		joy.visitTower(1, 1, 0);
		joy.placeInTower(f4, 2, 0);
		
		joy.placeMarket(f2, 0);
		joy.placeMarket(f4, 0);
		p.showRes();
		System.out.println("************************************");
		
		
		
		
		
		

		p.gain(coi3);
		
		DevelopmentCard b1 = new Building(1, "A", null, null, null, 3);
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
			//e1.printStackTrace();
		}
		
		p.showRes();
		
		
		
		Effect yellow = new Effect(GC.WHEN_SET_FAMILIAR_START_POWER, new EffectSetFamiliarStartPower(GC.FM_COLOR, 1));
		
		p.addEffect(yellow);

		joy.activateEffect(GC.WHEN_SET_FAMILIAR_START_POWER);
		
		p.getFreeMember().stream()
			.forEach(fam -> System.out.println("value " + fam.getValue()));
		
		
		
		
		
		
		
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
		
		Effect eff8 = new Effect(GC.WHEN_END,new EffectLostVictoryDepicted(GC.DEV_BUILDING, ser2));
		p.addEffect(eff8);
		
		DevelopmentCard c1 = new Venture(2, "a", null, null, null, 3);
		DevelopmentCard c2 = new Building(3, "b", null, null, null, 1);
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
		
		joy.launchesWork(4, "harvest");
		joy.launchesWork(100, "production");
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
		 * immediate != OPT = ONCE PER TURN
		 * **
		 */
	
	}
	
	//

}
