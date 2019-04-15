import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class BoardDrawer {
	
	private static String[] citys;
	private static Point[] points;
	
	public BoardDrawer() throws FileNotFoundException {
		Scanner sc = new Scanner(new File("Cities"));
		
		citys = new String[35];
		points = new Point[35]; 
		
		int cnt = 0;
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			
			int x = Integer.parseInt(line.substring(line.indexOf(",")+1,line.lastIndexOf(",")));
			int y = Integer.parseInt(line.substring(line.lastIndexOf(",")+1));
			citys[cnt] = line.substring(0, line.indexOf(","));
			points[cnt] = new Point(x,y);
			cnt++;
		}
	}
	
	public static void drawBoard(Graphics g, ArrayList<City> cities, ArrayList<Edge> edges) {
		//City points from text file (prob in constructor)
		//Cities
		for(int i = 0; i < citys.length; i++) {
			g.fillOval((int)points[i].getX(), (int)points[i].getY(), 10, 10);
		}
	}
	
	public static void drawTrains(Graphics g, ArrayList<Edge> trainEdges) {
		
	}
}
