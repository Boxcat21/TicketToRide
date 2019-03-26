import java.awt.Point;
import java.util.ArrayList;

public class City {
	private Point point;
	private String name;
	private ArrayList<Edge> edges;
	
	public Point getPoint() {
		return point;
	}
	public String getName() {
		return name;
	}
	public ArrayList<Edge> getAllEdges() {
		return edges;
	}
	public ArrayList<Edge> getEdges(String s) {
		ArrayList<Edge> temp = new ArrayList<Edge>();
		
		for(Edge e : edges) {
			if(e.getColor().equals(s))
				temp.add(e);
		}
		return temp;
	}
}
