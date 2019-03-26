
public class Edge {
	private int length;
	private String color;
	private Player player;
	private boolean hasPassed;
	private City[] cities;
	
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
	public City[] getCities() {
		return cities;
	}
}
