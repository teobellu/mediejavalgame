package game;

import java.util.Stack;

public class UserConfig {

	public UserConfig() {
		_towerRewardsThirdFloor = new Stack<>();
		_towerRewardsFourthFloor = new Stack<>();
	}
	
	public Resource getTowerRewardThirdFloor(){
		return _towerRewardsThirdFloor.pop();
	}
	
	public Resource getTowerRewardFourthFloor() {
		return _towerRewardsFourthFloor.pop();
	}
	
	private Stack<Resource> _towerRewardsThirdFloor;
	private Stack<Resource> _towerRewardsFourthFloor;
}
