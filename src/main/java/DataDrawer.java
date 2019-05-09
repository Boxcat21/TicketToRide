import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.imageio.ImageIO;


public class DataDrawer {
	
	public static ArrayList<Rectangle> clickableTrainCard;
	public static ArrayList<Rectangle> clickableContract;
	public final static Color[] playerColors = {Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE};
	private static ArrayList<BufferedImage> images;
	public static ArrayList<String> colors;
	public static void init() {
		images = new ArrayList<BufferedImage>();
		clickableTrainCard = new ArrayList<>();
		clickableContract = new ArrayList<>();
		colors = new ArrayList<>(); // corresponding colors to players
		
		colors.add("Red");
		colors.add("Green");
		colors.add("Yellow");
		colors.add("Blue");
		int temp = 1575;
		for ( int i = 0; i < 3; i++) {  // 5 display and one deck
			clickableTrainCard.add(new Rectangle(temp, 775, 70, 120)); // display cards from the deck. The last index is the deck itself
			temp += 110;
		}
		temp = 1575;
		for ( int i = 0; i < 3; i++) { 
			clickableTrainCard.add(new Rectangle(temp, 925, 70, 120));
			temp += 110;
		}
		
		clickableContract.add(new Rectangle(1615, 650, 200, 100));
		
		for(int i = 0; i < GameState.TRAIN_COLORS_LIST.size(); i++) {
			BufferedImage card = null; // image of train cards
			try { 
				card = ImageIO.read(new File(GameState.TRAIN_COLORS[i]+".png"));
			}
			catch (Exception e) {}
			images.add(card);
		}
	}
	
	public static void drawData(Graphics g, ArrayList<Player> data) {
		
		Graphics2D g2 = (Graphics2D) g;
		ArrayList<String> names = new ArrayList<>();
		for ( Player p : data) 
			names.add(p.getTrainColor());
		
		// add image for contract
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 24)); 
		
		// draws the player's current stats
		int x = 1545, y = 40;
		int xAdd = 150;
		for ( int i = 0; i < 4; i++) {		
			g2.setColor(playerColors[i]);
			int n = names.indexOf(GameState.PLAYER_COLORS[i]);
			g2.drawString("Trains: " + data.get(n).getTrains()+"", x, y);
			g2.drawString("Points: " + data.get(n).getPoints()+"", x+190, y);
			g2.drawString("#Contracts: " + data.get(n).getContracts().size()+"", x, y+50);
			g2.drawString("#TrainCards: " + data.get(n).getTrainCards().size()+"", x+190, y+50);
			g2.setColor(Color.BLACK);
			g2.drawLine(1535, xAdd, 1920, xAdd);
			xAdd += 150;
			y += 150;
		}
		
		
	}
	
	public static void drawDisplayCards(Graphics g, ArrayList<TrainCard> display) {
		// draws the passed in display of train cards
		// *NOTE We probably need to pass in the deck as well
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
			int index = GameState.TRAIN_COLORS_LIST.indexOf(display.get(i).getColor());
			g2.drawImage(images.get(index),temp, 775, 70, 120, null);
			temp += 110;
		}
		temp = 1575+55;
		for ( int i = 0; i < 2; i++) { 
			g2.setColor(Color.BLACK);
			g2.drawRect(temp, 925, 70, 120);
			int index = GameState.TRAIN_COLORS_LIST.indexOf(display.get(i).getColor());
			g2.drawImage(images.get(index),temp, 925, 70, 120, null);
			//g2.fillRect(temp, 925, 70, 120);
			temp += 110;
		}
			
	}
	
	public static void drawCurPlayer(Graphics g, Player p) {
		// draws the crrent player in the corner. We might need to add a slot for the number of trains selected for each track placement
		
		Graphics2D g2 = (Graphics2D) g;
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 50)); 
		g2.setColor(playerColors[colors.indexOf(p.getTrainColor())]);
		g2.drawString(p.getTrainColor() + " Player", 200, 800);
	}

}
