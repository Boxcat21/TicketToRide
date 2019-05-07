import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class DataDrawer {
	
	public static ArrayList<Rectangle> clickableTrainCard = new ArrayList<>();
	public static ArrayList<Rectangle> clickableContract = new ArrayList<>();
	
	private final static Color[] playerColors = {Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE};
	private static ArrayList<String> colors = new ArrayList<>();
	public static void init() {
		
		
		colors.add("Red");
		colors.add("Green");
		colors.add("Yellow");
		colors.add("Blue");
		int temp = 1575;
		for ( int i = 0; i < 3; i++) {  // 5 display and one deck
			clickableTrainCard.add(new Rectangle(temp, 775, 70, 120));
			temp += 110;
		}
		temp = 1575;
		for ( int i = 0; i < 3; i++) {  // 5 display and one deck
			clickableTrainCard.add(new Rectangle(temp, 925, 70, 120));
			temp += 110;
		}
		
		clickableContract.add(new Rectangle(1615, 650, 200, 100));
		
	}
	
	public static void drawData(Graphics g, ArrayList<Player> data) {
		
		Graphics2D g2 = (Graphics2D) g;
		Collections.sort(data, new Comparator<Player>() {
			@Override
			public int compare(Player arg0, Player arg1) {
				return arg0.getTrainColor().compareTo(arg1.getTrainColor());
			}
		});
		// add image for contract
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 24)); 
		
		
		int x = 1545, y = 40;
		int xAdd = 150;
		for ( int i = 0; i < 4; i++) {		
			g2.setColor(playerColors[i]);
			g2.drawString("Trains: " + data.get(i).getTrains()+"", x, y);
			g2.drawString("Points: " + data.get(i).getPoints()+"", x+190, y);
			g2.drawString("#Contracts: " + data.get(i).getContracts().size()+"", x, y+50);
			g2.drawString("#TrainCards: " + data.get(i).getTrainCards().size()+"", x+190, y+50);
			g2.setColor(Color.BLACK);
			g2.drawLine(1535, xAdd, 1920, xAdd);
			xAdd += 150;
			y += 150;
		}
		
		
	}
	
	public static void drawDisplayCards(Graphics g, ArrayList<TrainCard> display) {
	
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(4));
		g2.drawRect(1615, 650, 200, 100);
		g2.setColor(Color.WHITE);
		g2.fillRect(1615, 650, 200, 100);
		int temp = 1575;
		for ( int i = 0; i < 3; i++) {  // 5 display and one deck
			g2.setColor(Color.BLACK);
			g2.drawRect(temp, 775, 70, 120);
			g2.setColor(Color.WHITE);
			g2.fillRect(temp, 775, 70, 120);
			temp += 110;
		}
		temp = 1575;
		for ( int i = 0; i < 3; i++) { 
			g2.setColor(Color.BLACK);
			g2.drawRect(temp, 925, 70, 120);
			g2.setColor(Color.WHITE);
			g2.fillRect(temp, 925, 70, 120);
			temp += 110;
		}
			
	}
	
	public static void drawCurPlayer(Graphics g, Player p) {
		
		//System.out.println(p.getTrainColor());
		Graphics2D g2 = (Graphics2D) g;
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 24)); 
		g2.setColor(playerColors[colors.indexOf(p.getTrainColor())]);
		g2.drawString(GameState.PLAYER_COLORS[colors.indexOf(p.getTrainColor())] + " Turn", 200, 780);
	}

}
