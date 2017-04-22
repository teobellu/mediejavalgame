package game;

public class Domestic {

	public String getColor(){
		return _color;
	}
	
	public int getValue(){
		return _value;
	}
	
	public void setValue(int value){
		_value = value;
	}
	
	private String _color;
	private int _value;
}
