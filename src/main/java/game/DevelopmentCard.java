package game;

import java.util.List;

public class DevelopmentCard implements Card {

	public DevelopmentCard(String color, int age, List<Resource> cost) {
		_color = color;
		_age = age;
		_cost = cost;
	}
	
	public List<Resource> getCost(){
		return _cost;
	}
	
	public int getAge(){
		return _age;
	}

	public String getColor(){
		return _color;
	}
	
	private final List<Resource> _cost;
	private final String _color;
	private final int _age;
}
