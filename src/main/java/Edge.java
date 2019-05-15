import java.util.ArrayList;

public class Edge implements Comparable{
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
	public ArrayList<Edge> getConnectedPlayerEdges() { //gets all edges linked to this one (directly) that have the same player
		ArrayList<Edge> temp = new ArrayList<Edge>();
		for(City c : cities) {
			for(int i = 0; i < c.getAllEdges().size(); i++) {
				if(c.getAllEdges().get(i).getTrainColor().equals(this.trainColor))
					temp.add(c.getAllEdges().get(i));
			}
		}
		return temp; //if none, will return an empty array list
	}
	public ArrayList<Edge> getConnectedPlayerEdges(int x) { //based only on one city
		ArrayList<Edge> temp = new ArrayList<Edge>();
		
		System.out.println("x: " + x);//
		
		City c = cities.get(x);
		
		System.out.println("City: " + c.getName() + "");//
		System.out.println(cities.get(0).getAllEdges());
		for(int i = 0; i < c.getAllEdges().size(); i++) {
			System.out.println("\n" + i);
			System.out.println("COLORS:: " + c.getAllEdges().get(i).getTrainColor() + " " + this.trainColor);
			if(c.getAllEdges().get(i).getTrainColor().equals(this.trainColor)) {
				temp.add(c.getAllEdges().get(i));
			}
		}
		return temp;
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
	@Override
	public int compareTo(Object o) {
		Edge e = (Edge) o;
		if(this.cities.equals(e.getCities()))
			if(this.getColor().equals(e.getColor()))
				if(this.getTrainColor().equals(e.getTrainColor()))
					if(this.serial == e.getSerial())
						if(this.length == e.getLength())
							if(this.hasTrains == e.getHasTrains())
								return 0;
		return 1;
	}
	@Override
	public boolean equals(Object arg0) {
		Edge e = (Edge) arg0;
		return compare(e);
	}
}
