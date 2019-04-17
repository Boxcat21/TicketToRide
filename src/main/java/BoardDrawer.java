import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class BoardDrawer {
	
	private static String[] citys;//35
	private static Point[] points;//35
	private static int[][] connectedData; //201
	private static void init() {
		//city points
		Scanner sc = null;
		try {
			sc = new Scanner(new File("Cities"));
		} catch (FileNotFoundException e) {e.printStackTrace();}
		
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
		
		//connected cities
		try {
			sc = new Scanner(new File("ConnectedCities.txt"));
		} catch (FileNotFoundException e) {e.printStackTrace();}
		
		connectedData = new int[201][4];
		
		cnt = 0;
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			
			int index1 = findCitysIndex(line.substring(0, line.indexOf(",")));
			int index2 = findCitysIndex(line.substring(line.indexOf(",")+1,line.indexOf("|")));
			
			connectedData[cnt][0] = index1;
			connectedData[cnt][1] = index2;
			
			
		}
		
		
	}
	private static int findCitysIndex(String name) {
		for(int i = 0; i < citys.length; i++) {
			if(citys[i].equals(name))
				return i;
		}
		return -1;
	}
	public static void drawBoard(Graphics g, ArrayList<City> cities, ArrayList<Edge> edges) {
		init();
		//City points from text file (prob in constructor)
		//Cities NEED TO LABEL THE CITIES
		int r = 10;
		for(int i = 0; i < citys.length; i++) {
			g.setColor(new Color(153, 76, 0)); //dark orange
			g.fillOval((int)points[i].getX()-r, (int)points[i].getY(), r, r);
			
			g.setColor(Color.BLACK);
			g.drawOval((int)points[i].getX()-r, (int)points[i].getY(), r, r);
		}
		//connecting edges
		
		
	}
	
	public static void drawTrains(Graphics g, ArrayList<Edge> trainEdges) {
		
	}
}
