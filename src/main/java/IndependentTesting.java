import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class IndependentTesting {
	static GameState g;
	static Scanner scan;

	public static void main(String[] args) throws FileNotFoundException {
	g = new GameState();
	ArrayList<Edge> edges = g.getEdges();
	System.out.println(edges.size());
	for(Edge e:edges)
		System.out.println(e);
	
	
	}
	
}
