import java.util.ArrayList;

public class Edge {
	private int length;
	private String color;
	private Player player;
	private boolean hasTrains;
	private ArrayList<City> cities;
	
	public Edge(int l, String s, ArrayList<City> c) 
	{
		length = l;
		color = s;
		cities= c;
	}
	public Edge() {
		// TODO Auto-generated constructor stub
	}
	public int getLength() {
		return length;
	}
	public String getColor() {
		return color;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player p) {
		player = p;
	}
	public ArrayList<City> getCities() {
		return cities;
	}
	public boolean setHasTrains() {
		if ( hasTrains) 
			return false;
		hasTrains = true;
		return true;
		
	}
	public boolean getHasTrains() {
		return hasTrains;
	}

	public City getOtherCity(City c) {
		if(c.equals(cities.get(0)))
			return cities.get(1);
		else if(c.equals(cities.get(1)))
			return cities.get(0);
		else
			return null;
	}

}
