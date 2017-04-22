package game;

import java.util.ArrayList;
import java.util.List;

import util.Constants;

public class BoardGame {

	public BoardGame(UserConfig userConfig) {
		_userConfig = userConfig;
		
		_towers = new ArrayList<>();
		
		for(String c : Constants.DEVELOPMENT_COLOURS){
			Tower t = new Tower(c);
			_towers.add(t);
		}
	}
	
	private final List<Tower> _towers;
	private final UserConfig _userConfig;
	
/********************************************************************************************************************************************************************************************/
	private class Tower {
		
		public Tower(String color) {
			_color = color;
			
			_spaces = new ArrayList<>();
			
			for(int i : Constants.FLOORS){
				Space s = null;
				if(i==Constants.THIRD_FLOOR){
					s = new Space(i, _userConfig.getTowerRewardThirdFloor());
				} else if(i==Constants.FOURTH_FLOOR){
					s = new Space(i, _userConfig.getTowerRewardFourthFloor());
				} else{
					s = new Space(i, null);
				}
				_spaces.add(s);
			}
		}
		
		private final List<Space> _spaces;
		private final String _color;
		
/********************************************************************************************************************************************************************************************/
		private class Space{
			
			public Space(int cost, Resource resource) {
				_cost = cost;
				_resource = resource;
			}
			
			public void setCard(DevelopmentCard card){
				_card = card;
			}
			
			public int getCost(){
				return _cost;
			}
			
			public Resource getResource() throws NullPointerException{
				if(_resource == null){
					throw new NullPointerException();
				}
				
				return _resource;
			}
			
			private final Resource _resource;
			private DevelopmentCard _card;
			private final int _cost;
		}
	}
}