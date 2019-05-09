import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;

import javax.imageio.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	
	private static int[][] connectedData; //201 indecies of points
	private static int[] lengths; //201
	private static String[] colors; //201
	private static ArrayList<Shape> rotatedRects; //201
	private static int doubleEdgeCheck;
	private static int size;
	private static void init() {
		size = 12;
		//thing
		doubleEdgeCheck = 0;
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
			sc = new Scanner(new File("FixedCon"));
		} catch (FileNotFoundException e) {e.printStackTrace();}
		
		connectedData = new int[100][2];
		lengths = new int[100];
		colors = new String[100];
		
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
		try {
			sc = new Scanner(new File("Doubles.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static int findCitysIndex(String name) {
		for(int i = 0; i < citys.length; i++) {
			if(citys[i].equals(name))
				return i;
		}
		return -1;
	}
	public static void drawBoard(Graphics g, ArrayList<Integer> eds, ArrayList<Player> playerColors) {
		//temporary
		ArrayList<String> colours = new ArrayList<String>();
		DataDrawer.init();
		init();
		//find edges indecies
		for(int i = 0; i < citys.length; i++) {
			//String ct1 = edges.get(arg0)
		}
		//
		BufferedImage map = null;
		try { 
			map = ImageIO.read(new File("Map.png"));
		}
		catch (Exception e) {}
		//g.drawImage(map, 0, 0, 1535, 755, null);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 1535, 755);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, 1535, 755);
		// connecting edges
		for (int i = 0; i < connectedData.length; i++) {
			int x1 = (int) points[connectedData[i][0]].getX();
			int y1 = (int) points[connectedData[i][0]].getY();

			int x2 = (int) points[connectedData[i][1]].getX();
			int y2 = (int) points[connectedData[i][1]].getY();
			
			int nx1, nx2, ny1, ny2;
			if(i+1 < connectedData.length) {
				nx1 = (int) points[connectedData[i+1][0]].getX();
				ny1 = (int) points[connectedData[i+1][0]].getY();
				
				nx2 = (int) points[connectedData[i+1][1]].getX();
				ny2 = (int) points[connectedData[i+1][1]].getY();
			}
			else {
				nx1 = -1; ny1 = -1; nx2 = -1; ny2 = -1;
			}
			if(x1 == nx1 && y1 == ny1 && x2 == nx2 && y2 == ny2) {
				int distance = (int) Math.round(Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
				double angle = (double) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
				if (angle < 0)
					angle += 360;
				Color color;
				try {
					Field field = Class.forName("java.awt.Color").getField(colors[i]);
					color = (Color)field.get(null);
				} catch (Exception e) { color = null;}
				
				doubleEdgeCheck = 1;
				
				if(!eds.contains(i))
					drawRotatedRect(g, x1, y1, angle, distance, size, color, false, x2, y2);
				else {
					String s = playerColors.get(eds.indexOf(i)).getTrainColor();
					int index  = DataDrawer.colors.indexOf(s);
					Color colour = DataDrawer.playerColors[index];
					drawRotatedRect(g, x1, y1, angle, distance, size, colour, true,  x2, y2);
				}
				i++;
				try {
					Field field = Class.forName("java.awt.Color").getField(colors[i]);
					color = (Color)field.get(null);
				} catch (Exception e) { color = null;}
				
				doubleEdgeCheck = 2;
				if(!eds.contains(i))
					drawRotatedRect(g, x1, y1, angle, distance, size, color, false, x2, y2);
				else {
					String s = playerColors.get(eds.indexOf(i)).getTrainColor();
					int index  = DataDrawer.colors.indexOf(s);
					Color colour = DataDrawer.playerColors[index];
					drawRotatedRect(g, x1, y1, angle, distance, size, colour, true, x2, y2);
				}
				doubleEdgeCheck = 0;
			}
			else {
				int distance = (int) Math.round(Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
				// oppositeRectPoint(x1, x2, distance, 20, )
				double angle = (double) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));

				if (angle < 0)
					angle += 360;
				//
				Color color;
				try 
				{
					Field field = Class.forName("java.awt.Color").getField(colors[i]);
					color = (Color)field.get(null);
				} catch (Exception e) { color = null;}
				//
				if(!eds.contains(i))
					drawRotatedRect(g, x1, y1, angle, distance, size, color, false, x2, y2);
				else {
					String s = playerColors.get(eds.indexOf(i)).getTrainColor();
					int index  = DataDrawer.colors.indexOf(s);
					Color colour = DataDrawer.playerColors[index];
					drawRotatedRect(g, x1, y1, angle, distance, size, colour, true, x2, y2);
				}
			}
		}
		int r = 20;
		for(int i = 0; i < citys.length; i++) {
			g.setColor(new Color(153, 76, 0)); //dark orange
			g.fillOval((int)points[i].getX()-r/2, (int)points[i].getY()-r/2, r, r);
			
			g.setColor(Color.BLACK);
			g.drawOval((int)points[i].getX()-r/2, (int)points[i].getY()-r/2, r, r);
		}
		BufferedImage numbers = null;
		BufferedImage label = null;
		try {
			numbers = ImageIO.read(new File("numbers.png"));
			label = ImageIO.read(new File("CityLabels.png"));
		}catch(IOException e) {}
		
		g.drawImage(numbers, 0, 0, null);
		g.drawImage(label, 0, 0, null);
		
		//g.fillOval(1535, 755, 10, 10);
		
	}
	private static void drawRotatedRect(Graphics g, int x, int y, double angle, int length, int width, Color color, boolean check, int x2, int y2) {
		double theta = Math.toRadians(angle);
		Rectangle2D rect = new Rectangle2D.Double(-length/2.,-width/2.,length, width);
		
		AffineTransform tran = new AffineTransform();
		
		double alpha = Math.toRadians(90 - angle);
		int trigX = (int) (Math.round(Math.cos(alpha)*(width/2)));
		int trigY = (int) (Math.round(Math.sin(alpha)*(width/2)));
		
		if(doubleEdgeCheck == 2) {
			trigX = (int) (Math.round(Math.cos(alpha)*(width)));
			trigY = (int) (Math.round(Math.sin(alpha)*(width)));
			tran.translate(trigX+x,-trigY+y);
		}
		else if(doubleEdgeCheck == 1) 
			tran.translate(x, y);
		else 
			tran.translate(trigX+x,-trigY+y);
		tran.rotate(theta);
		tran.translate(length/2, width/2);
		
		Shape rotatedRect = tran.createTransformedShape(rect);
		
		Rectangle re = rotatedRect.getBounds();
		int minX = (int)(re.getMinX());
		int minY = (int)(re.getMinY());
		int maxX = (int)(re.getMaxX());
		int maxY = (int)(re.getMaxX());
		
		Graphics2D g2d = (Graphics2D) g;
		Composite a = g2d.getComposite();
		
		Stroke s = g2d.getStroke();
		if(check) {
			g2d.setColor(color);
			Stroke dashed = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{12}, 0);
			g2d.setStroke(dashed);
		}
		else {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 9 * 0.1f));
			g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(),60));
		}
		g2d.fill(rotatedRect);
		g2d.setComposite(a);
		g.setColor(Color.BLACK);
		//if(!g2d.getStroke().equals(s)) {
			//g2d.drawLine(minX, minY, maxX, maxY);
		//}
		//g2d.setStroke(s);
		g2d.draw(rotatedRect);
		rotatedRects.add(rotatedRect);
		
		g2d.setStroke(s);
	}
	public static void drawTrains(Graphics g, ArrayList<Edge> trainEdges) {
		
	}
	public static int edgeClick(MouseEvent e, GameState game) {
		Point p = e.getPoint();
		String c1 = null, c2 = null,color = null;
		for(int i = 0; i < rotatedRects.size(); i++) {
			if(rotatedRects.get(i).contains(e.getPoint())) {
				int i1 = connectedData[i][0];
				int i2 = connectedData[i][1];
				
				c1 = citys[i1];
				c2 = citys[i2];
				
				return i;
			}
		}
		return -1;
	}
}
