package model;

import model.Resource.type;

public class EffectLostVictoryForEach implements IEffectBehavior{

	private Resource forEach;		//paga 1 victory per ogni forEach
	private Resource toPay;			//quanto pagare effettivamente
	private int countVictoryTax;	//contatore punti da pagare
	private Resource playerRes;		//risorse possedute dal giocatore
	private Player player;
	
	@Override
	public void effect(Effect ref) {
		initializes(ref);
		establishTax();
		payTax();
	}
	
	public void initializes(Effect ref){
		countVictoryTax = 0;
		player = ref.getPlayer();
		playerRes = player.getResource();
		toPay = new Resource();
		forEach = (Resource) ref.getParameters();
	}

	public void establishTax() {
		int addTax;
		for (type i : type.values())
			if (playerRes.get(i)>=forEach.get(i) && forEach.get(i)>0){
				addTax = playerRes.get(i) / forEach.get(i);
				countVictoryTax += addTax;
			}
	}
	
	public void payTax() {
		int playerVictory = playerRes.get(type.VICTORYPOINTS);
		countVictoryTax = Math.min(countVictoryTax, playerVictory);
		toPay.add(type.VICTORYPOINTS, countVictoryTax);
		try {
			player.Pay(toPay);
		} catch (GameException e) {
			// Non entrerò mai qui dentro
			e.printStackTrace();
		}
	}
}
