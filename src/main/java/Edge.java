import java.util.ArrayList;

public class Edge {
	private int serial;
	private int length;
	private String color;
	private String trainColor;
	private Player player;
	private boolean hasTrains;
	private ArrayList<City> cities;
	
	public Edge(int l, String s, ArrayList<City> c, int ser) 
	{
		trainColor = "none";
		serial = ser;
		length = l;
		color = s;
		cities= c;
	}
	public Edge() {
		
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
	public void setTrainColor(String s) {
		trainColor = s;
	}
	public String getTrainColor() {
		return trainColor;
	}
	public boolean setHasTrains(String color) {
		if (hasTrains) 
			return false;
		hasTrains = true;
		trainColor = color;
		return true;
		
	}
	public boolean getHasTrains() {
		return hasTrains;
	}
	public int getSerial() {
		return serial;
	}
	public City getOtherCity(City c) {
		if(c.getName().equals(cities.get(0).getName()))
			return cities.get(1);
		else if(c.getName().equals(cities.get(1).getName()))
			return cities.get(0);
		else
			return null;
	}
	public ArrayList<Edge> getSamePlayerEdges() {
		
		for(City c : this.cities) {
			
		}
		return null;
	}
	public boolean equals(Edge other) {
		for(int i = 0; i < this.cities.size(); i++) {
			if(!(other.cities.contains(cities.get(i))))
				return false;
		}
		if(!(this.color.equals(other.color)))
			return false;
		return true;
	}
	public boolean compare(Edge e) {
		if(this.cities.equals(e.getCities()))
			if(this.getColor().equals(e.getColor()))
				if(this.getTrainColor().equals(e.getTrainColor()))
					if(this.serial == e.getSerial())
						if(this.length == e.getLength())
							if(this.hasTrains == e.getHasTrains())
								return true;
		return false;
	}
	public String toString() {
		return "|" + cities.get(0) + " to " + cities.get(1) + "|"+ getTrainColor();
//		return "";
	}
}
