package game;

public class Resource {

	public Resource(String type, int amount) {
		_amount = amount;
		_type = type;
	}
	
	public int getAmount(){
		return _amount;
	}
	
	public String getType(){
		return _type;
	}
	
	public void addAmount(int amount){
		_amount += amount;
	}
	
	public void removeAmount(int amount){
		_amount -= amount;
	}
	
	private int _amount;
	private String _type;
	
}
