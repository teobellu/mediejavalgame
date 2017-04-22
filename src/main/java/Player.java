import java.util.List;

import game.DevelopmentCard;
import game.Domestic;
import game.LeaderCard;
import game.Resource;

public class Player {

	public Player(long id, String name) {
		_id = id;
		_name = name;
	}
	
	public void gain(Resource resource){
		for(Resource res : _resources){
			if(res.getType().equals(resource.getType())){
				res.addAmount(resource.getAmount());
				return;
			}
		}
	}
	
	public void spend(Resource resource){
		for(Resource res : _resources){
			if(res.getType().equals(resource.getType())){
				res.removeAmount(resource.getAmount());
				return;
			}
		}
	}
	
	public String getName(){
		return _name;
	}
	
	public long getID(){
		return _id;
	}
	
	public void addDevCard(DevelopmentCard card){
		_developmentCards.add(card);
	}
	
	public void addLeaderCard(LeaderCard card){
		_leaderCard.add(card);
	}
	
	private final long _id;
	private final String _name;
	private List<Resource> _resources;
	private List<DevelopmentCard> _developmentCards;
	private List<LeaderCard> _leaderCard;
	private List<Domestic> _domestics;
}
