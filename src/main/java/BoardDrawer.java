import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class BoardDrawer {
	/*FIX FOR PROGRESS IN THIS CLASS--
	 * 
	 * typos in connectedcities.txt - actually i think i got rid of them all
	 * add pheonix in cities
	 * remove redundancies in connectedcities IF POSSIBLE -- i think i can do that in code, idk if there were some connections left out
	 * ^we might have to check that too
	 * */
	private static String[] citys;//35
	private static Point[] points;//35
	private static int[][] connectedData; //201
	private static int[] lengths; //201
	private static String[] colors; //201
	private static ArrayList<Shape> rotatedRects;
	private static void init() {
		//rotato bato
		rotatedRects = new ArrayList<Shape>();
		//city points
		Scanner sc = null;
		try {
			sc = new Scanner(new File("Cities"));
		} catch (FileNotFoundException e) {e.printStackTrace();}
		
		citys = new String[36];
		points = new Point[36]; 
		
		int cnt = 0;
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			
			int x = Integer.parseInt(line.substring(line.indexOf(",")+1,line.lastIndexOf(",")))-50;
			int y = Integer.parseInt(line.substring(line.lastIndexOf(",")+1));
			citys[cnt] = line.substring(0, line.indexOf(","));
			points[cnt] = new Point(x,y);
			cnt++;
		}
		
		//connected cities
		try {
			sc = new Scanner(new File("ConnectedCities.txt"));
		} catch (FileNotFoundException e) {e.printStackTrace();}
		
		connectedData = new int[201][2];
		lengths = new int[201];
		colors = new String[201];
		
		cnt = 0;
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			
			int index1 = findCitysIndex(line.substring(0, line.indexOf(",")));
			int index2 = findCitysIndex(line.substring(line.indexOf(",")+1,line.indexOf("|")));
			
			connectedData[cnt][0] = index1;
			connectedData[cnt][1] = index2;
			
			lengths[cnt] = Integer.parseInt(line.substring(line.indexOf("|")+1,line.lastIndexOf(",")));
			colors[cnt] = line.substring(line.lastIndexOf(",")+1);
			cnt++;
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
		//
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 1535, 755);
		// connecting edges
		for (int i = 0; i < connectedData.length; i++) {
			int x1 = (int) points[connectedData[i][0]].getX();
			int y1 = (int) points[connectedData[i][0]].getY();

			int x2 = (int) points[connectedData[i][1]].getX();
			int y2 = (int) points[connectedData[i][1]].getY();

			int distance = (int) Math.round(Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
			// oppositeRectPoint(x1, x2, distance, 20, )
			double angle = (double) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));

			if (angle < 0)
				angle += 360;
			Color color;
			try {
			    Field field = Class.forName("java.awt.Color").getField(colors[i]);
			    color = (Color)field.get(null);
			} catch (Exception e) {
			    color = Color.BLACK; // Not defined
			}
			drawRotatedRect(g, x1, y1, angle, distance, 10, color);
		}
		//City points from text file (prob in constructor)
		//Cities NEED TO LABEL THE CITIES
		int r = 20;
		for(int i = 0; i < citys.length; i++) {
			g.setColor(new Color(153, 76, 0)); //dark orange
			g.fillOval((int)points[i].getX()-r/2, (int)points[i].getY()-r/2, r, r);
			
			g.setColor(Color.BLACK);
			g.drawOval((int)points[i].getX()-r/2, (int)points[i].getY()-r/2, r, r);
			System.out.println();
		}
		//g.fillOval(1535, 755, 10, 10);
		
	}
	private static void drawRotatedRect(Graphics g, int x, int y, double angle, int length, int width, Color color) {
		double theta = Math.toRadians(angle);
		Rectangle2D rect = new Rectangle2D.Double(-length/2.,-width/2.,length, width);
		
		AffineTransform tran = new AffineTransform();
		
		double alpha = Math.toRadians(90 - angle);
		int trigX = (int) (Math.round(Math.cos(alpha)*(width/2)));
		int trigY = (int) (Math.round(Math.sin(alpha)*(width/2)));
		
		tran.translate(trigX+x,-trigY+y);
		tran.rotate(theta);
		tran.translate(length/2, width/2);
		
		Shape rotatedRect = tran.createTransformedShape(rect);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(color);
		g2d.fill(rotatedRect);
		g.setColor(Color.BLACK);
		g2d.draw(rotatedRect);
		rotatedRects.add(rotatedRect);
	}
	public static void drawTrains(Graphics g, ArrayList<Edge> trainEdges) {
		
	}
}
