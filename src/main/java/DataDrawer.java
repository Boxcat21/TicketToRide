import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;


public class DataDrawer {
	
	public static ArrayList<Rectangle> clickableTrainCard;
	public static ArrayList<Rectangle> clickableContract;
	
	public DataDrawer() {
		clickableTrainCard = new ArrayList<>();
		clickableContract = new ArrayList<>();
		
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
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(4));
		g2.drawRect(1615, 650, 200, 100);
		g2.setColor(Color.WHITE);
		g2.fillRect(1615, 650, 200, 100);
		// add image for contract
		
		g2.setColor(Color.BLACK);
		g2.drawLine(1535, 150, 1920, 150);
		g2.drawLine(1535, 300, 1920, 300);
		g2.drawLine(1535, 450, 1920, 450);
		g2.drawLine(1535, 600, 1920, 600);
		g2.setStroke(new BasicStroke(4));
		
		int temp = 1575;
		for ( int i = 0; i < 3; i++) {  // 5 display and one deck
			g2.setColor(Color.BLACK);
			g2.drawRect(temp, 775, 70, 120);
			g2.setColor(Color.WHITE);
			g2.fillRect(temp, 775, 70, 120);
			temp += 110;
		}
		temp = 1575;
		for ( int i = 0; i < 3; i++) {  // 5 display and one deck
			g2.setColor(Color.BLACK);
			g2.drawRect(temp, 925, 70, 120);
			g2.setColor(Color.WHITE);
			g2.fillRect(temp, 925, 70, 120);
			temp += 110;
		}
	}
	
	public static void drawDisplayCards(Graphics g, ArrayList<TrainCard> display) {
		Graphics2D g2 = (Graphics2D) g;
			
	}

}
